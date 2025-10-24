package problems.qbf.solvers;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import metaheuristics.ga.AbstractGA;
import problems.qbf.MAX_SC_QBF;
import solutions.Solution;

/**
 * Metaheuristic GA (Genetic Algorithm) for
 * obtaining an optimal solution to a QBF (Quadractive Binary Function --
 * {@link #QuadracticBinaryFunction}). 
 * 
 * @author ccavellucci, fusberti
 */
public class GA_QBF_SC extends AbstractGA<Integer, Integer> {

	/**
	 * Constructor for the GA_QBF class. The QBF objective function is passed as
	 * argument for the superclass constructor.
	 * 
	 * @param generations
	 *            Maximum number of generations.
	 * @param popSize
	 *            Size of the population.
	 * @param mutationRate
	 *            The mutation rate.
	 * @param filename
	 *            Name of the file for which the objective function parameters
	 *            should be read.
	 * @throws IOException
	 *             Necessary for I/O operations.
	 */
	public GA_QBF_SC(Integer seed, Integer generations, Integer popSize, Double mutationRate, String filename) throws IOException {
		super(new MAX_SC_QBF(filename), seed, generations, popSize, mutationRate);
	}

    /**
     * Initiate the population using Latin Hypercube Sampling (LHS) to guarantee
     * a more uniform distribution of individuals.
     *
     * @return the initial population.
     */
    @Override
    protected Population initializePopulationLHS() {
        Population population = new Population();

        double[][] lhsMatrix = new double[popSize][chromosomeSize];

        for (int j = 0; j < chromosomeSize; j++) {
            List<Double> columnValues = new ArrayList<>();
            for (int i = 0; i < popSize; i++) {
                double val = (i + rng.nextDouble()) / popSize;
                columnValues.add(val);
            }

            Collections.shuffle(columnValues, rng);

            for (int i = 0; i < popSize; i++) {
                lhsMatrix[i][j] = columnValues.get(i);
            }
        }

        for (int i = 0; i < popSize; i++) {
            Chromosome chromosome = new Chromosome();
            for (int j = 0; j < chromosomeSize; j++) {
                if (lhsMatrix[i][j] > 0.5) {
                    chromosome.add(1);
                } else {
                    chromosome.add(0);
                }
            }

            fixChromosome(chromosome);
            population.add(chromosome);
        }

        return population;
    }



	/**
	 * {@inheritDoc}
	 * 
	 * This createEmptySol instantiates an empty solution and it attributes a
	 * zero cost, since it is known that a QBF solution with all variables set
	 * to zero has also zero cost.
	 */
	@Override
	public Solution<Integer> createEmptySol() {
		Solution<Integer> sol = new Solution<Integer>();
		sol.cost = 0.0;
		return sol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see metaheuristics.ga.AbstractGA#decode(metaheuristics.ga.AbstractGA.
	 * Chromosome)
	 */
	@Override
	protected Solution<Integer> decode(Chromosome chromosome) {

		Solution<Integer> solution = createEmptySol();
		for (int locus = 0; locus < chromosome.size(); locus++) {
			if (chromosome.get(locus) == 1) {
				solution.add(new Integer(locus));
			}
		}

		ObjFunction.evaluate(solution);
		return solution;
	}

	protected void encode(Solution<Integer> solution, Chromosome chromosome) {
		for (int locus = 0; locus < chromosome.size(); locus++) {
			if (solution.contains(locus)) {
				chromosome.set(locus, 1);
			} else {
				chromosome.set(locus, 0);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see metaheuristics.ga.AbstractGA#generateRandomChromosome()
	 */
	@Override
	protected Chromosome generateRandomChromosome() {

		Chromosome chromosome = new Chromosome();
		for (int i = 0; i < chromosomeSize; i++) {
			chromosome.add(rng.nextInt(2));
		}

		fixChromosome(chromosome);

		return chromosome;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see metaheuristics.ga.AbstractGA#fitness(metaheuristics.ga.AbstractGA.
	 * Chromosome)
	 */
	@Override
	protected Double fitness(Chromosome chromosome) {

		fixChromosome(chromosome);
		return decode(chromosome).cost;

	}

	/*
	 * Garantees that children chromosomes respect the SC restrictions.
	 * If not, it fixes them using genes from the parents.
	 */
	@Override
	protected void fixChildren(Chromosome child1, Chromosome child2) {
		
		Solution<Integer> solChild1 = decode(child1);
		Solution<Integer> solChild2 = decode(child2);

		ArrayList<Integer> setsChild1 = ObjFunction.evaluateSetCoverage(solChild1);
		ArrayList<Integer> setsChild2 = ObjFunction.evaluateSetCoverage(solChild2);

		//Makes child1 feasible
		for (int s = 0; s < setsChild1.size(); s++) {
			if (setsChild1.get(s) < 1) { //If child1 does not cover set s
				//Tries to fix child1 using genes from parents that when to the siblings
				for(int i = child2.size() - 1; i >= 0; i--) {
					Integer e = child2.get(i);
					if (child1.get(e) < 1 && ObjFunction.elementInSet(e, s)) {
						child1.set(e, 1);
						solChild1.add(e);
						setsChild1 = ObjFunction.evaluateSetCoverage(decode(child1));

						//Tries to remove element e from child2 if it is does not violate SC
						boolean removeFromChild2 = true;
						for (int s2 = 0; s2 < setsChild2.size(); s2++) {
							if (ObjFunction.elementInSet(e, s2) && setsChild2.get(s2) < 2){
								removeFromChild2 = false;
								break;
							} 
						}
						if (removeFromChild2){
							child2.set(e, 0);
							solChild2.remove(e);
							setsChild2 = ObjFunction.evaluateSetCoverage(solChild2);
						}

						if (setsChild1.get(s) > 0) break; //If set s is covered, break
					}
				}
			}
		}

		//Makes child2 feasible
		for (int s = 0; s < setsChild2.size(); s++) {
			if (setsChild2.get(s) < 1) { //If child2 does not cover set s
				//Tries to fix child2 using genes from parents that when to the siblings
				for(int i = child1.size() - 1; i >= 0; i--) {
					Integer e = child1.get(i);
					if (child2.get(e) < 1 && ObjFunction.elementInSet(e, s)) {
						child2.set(e, 1);
						solChild2.add(e);
						setsChild2 = ObjFunction.evaluateSetCoverage(decode(child2));

						//Tries to remove element e from child1 if it is does not violate SC
						boolean removeFromChild1 = true;
						for (int s2 = 0; s2 < setsChild1.size(); s2++) {
							if (ObjFunction.elementInSet(e, s2) && setsChild1.get(s2) < 2){
								removeFromChild1 = false;
								break;
							} 
						}
						if (removeFromChild1){
							child1.set(e, 0);
							solChild1.remove(e);
							setsChild1 = ObjFunction.evaluateSetCoverage(solChild1);
						}

						if (setsChild2.get(s) > 0) break; //If set s is covered, break
					}
				}
			}
		}

		fixChromosome(child1);
		fixChromosome(child2);

		encode(solChild1, child1);
		encode(solChild2, child2);
	}

	/*
	 * Garantees that a chromosome respects the SC restrictions.
	 * If not, it fixes it by randomly flipping genes that cause feasibility.
	 */
	protected void fixChromosome(Chromosome chromosome) {
		ArrayList<Integer> setsCoverage = ObjFunction.evaluateSetCoverage(decode(chromosome));

		//While there is a set that is not covered
		for(Integer s=0; s<setsCoverage.size(); s++) {
			if (setsCoverage.get(s) < 1) { //If set s is not covered
				//Selects a random element that covers set s and is not in the solution
				ArrayList<Integer> candidates = new ArrayList<Integer>();
				for (int e = 0; e < chromosome.size(); e++) {
					if (ObjFunction.elementInSet(e, s)) {
						candidates.add(e);
					}
				}

				Integer selected = candidates.get(rng.nextInt(candidates.size()));
				chromosome.set(selected, 1); //Inserts the selected element into the solution
				setsCoverage = ObjFunction.evaluateSetCoverage(decode(chromosome));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * metaheuristics.ga.AbstractGA#mutateGene(metaheuristics.ga.AbstractGA.
	 * Chromosome, java.lang.Integer)
	 */
	@Override
	protected void mutateGene(Chromosome chromosome, Integer locus) {

		chromosome.set(locus, 1 - chromosome.get(locus));
	}

	/**
	 * A main method used for testing the GA metaheuristic.
	 * 
	 */
	public static void main(String[] args) throws IOException {
        ArrayList<String> argsList = new ArrayList(List.of(args));

        String instanceFilePath = argsList.get(argsList.indexOf("--instance") + 1);
        Integer generations = Integer.parseInt(argsList.get(argsList.indexOf("--generations") + 1));
        Integer popSize = Integer.parseInt(argsList.get(argsList.indexOf("--popSize") + 1));
        Double mutationRate = Double.valueOf(argsList.get(argsList.indexOf("--mutation") + 1));
        Boolean useLHS  = Boolean.parseBoolean(argsList.get(argsList.indexOf("--useLHS") + 1));
        Boolean useUC  = Boolean.parseBoolean(argsList.get(argsList.indexOf("--useUC") + 1));
        Integer timeLimit  = Integer.parseInt(argsList.get(argsList.indexOf("--timeLimit") + 1));
        Integer seed  = Integer.parseInt(argsList.get(argsList.indexOf("--seed") + 1));

        var target = Double.POSITIVE_INFINITY;
        if (argsList.contains("--target")) {
            target = Double.parseDouble(argsList.get(argsList.indexOf("--target") + 1));
        }

        System.out.println("Executando GA na instância " + instanceFilePath +  " com População = " +
                popSize + ", Taxa de Mutação = " + mutationRate + " por " + generations + " gerações");
        System.out.println("Método de inicialização: " + (useLHS ? "LHS" : "Default"));

        long startTime = System.currentTimeMillis();
		GA_QBF_SC ga = new GA_QBF_SC(seed, generations, popSize, mutationRate, instanceFilePath);
		Solution<Integer> bestSol = ga.solve(useLHS, useUC, timeLimit, target);
		System.out.println("Set coverage = " + ga.ObjFunction.evaluateSetCoverage(bestSol));
		System.out.println("maxVal = " + bestSol);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Time = " + (double) totalTime / (double) 1000 + " seg");

//		String instanceName = new File(instanceFilePath).getName().split("\\.")[0];
//		String outputFilePath = "results/" + instanceName + "_" + popSize + "_" + mutationRate + "_" + (useLHS ? "LHS" : "noLHS") + "_" + (useUC ? "UC" : "noUC") + ".out";
//
//		Files.write(
//            Paths.get(outputFilePath),
//            (ga.getStats()).getBytes(),
//            StandardOpenOption.CREATE,
//            StandardOpenOption.TRUNCATE_EXISTING
//        );
	}

}
