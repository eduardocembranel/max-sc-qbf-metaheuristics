# Genetic Algorithms

This project implements Genetic Algorithm's variations to solve the **MAX-SC-QBF** problem.

## Requirements

- Java JDK 17+
- Unix based operational system

## Run experiments

To run the `standard` heuristic along all the instances
```bash
./run_all_instances.sh
````

To run `standard` heuristic with an spectific target with 50 independent executions for the instance `exact_n200`, in order to have empirical data for later analasys
```
# With an "easy" target
./run_easy_target.sh

# With an "medium" target
./run_easy_target.sh

# With an "hard" target
./run_easy_target.sh
```

## Run for a single instance

To run the algorithm, use the command below with the desired parameters:

```
java -cp out problems.qbf.solvers.GA_QBF_SC --instance <file path> --seed <seed> --generations <maximum generations> --popSize <population size> --mutation <mutation rate> --useLHS <true|false> --useUC <true|false> --timeLimit <time limit seconds> > "$output_file"
```

## Run (for all instances with predefined parameter settings)

There are five Bash scripts named `script_config1.sh` through `script_config5.sh`, each corresponding to a different configuration setup.

```
./bash_configs/script_config1.sh
```

## Available parameters

<table>
  <thead>
    <tr>
      <th style="width: 120px; text-align: left;">Parameter</th>
      <th style="text-align: left;">Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><code>--instance</code></td>
      <td>Instance file name. Must be located in the path <code>./instances/max_sc_qbf/</code>.</td>
    </tr>
    <tr>
      <td><code>--generations</code></td>
      <td>Number of generations for the genetic algorithm to run.</td>
    </tr>
    <tr>
      <td><code>--popSize</code></td>
      <td>Size of the population in each generation.</td>
    </tr>
    <tr>
      <td><code>--mutation</code></td>
      <td>Mutation rate applied to genes during the evolution process.</td>
    </tr>
    <tr>
      <td><code>--useLHS</code></td>
      <td>Boolean flag to enable or disable Latin Hypercube Sampling (LHS).</td>
    </tr>
    <tr>
      <td><code>--useUC</code></td>
      <td>Boolean flag to enable or disable Uniform Crossover (UC).</td>
    </tr>
    <tr>
      <td><code>--timeLimit</code></td>
      <td>Maximum execution time allowed for the algorithm, in seconds.</td>
    </tr>
  </tbody>
</table>

## See the results

The result output will be available in the path `./results`
