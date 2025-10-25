import problems.scqbf.solvers.TSSCQBF;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 5 && args.length != 6) {
            System.out.println("Wrong number of arguments, should provide the instanceName, methodName and seed, e.g: exact_n25 std 1");
            return;
        }
        System.out.println("Press ctrl+c to cancel the execution and see the result so far in the output file");

        var outputFolder = args[0];
        var instance = args[1];
        var method = args[2];
        var maxTimeSecsStr = args[3];
        var maxTimeSecs = Integer.parseInt(maxTimeSecsStr);
        var seedStr = args[4];
        var seed = Integer.parseInt(seedStr);

        var target = Double.NEGATIVE_INFINITY;
        if (args.length == 6) {
            target = Double.parseDouble(args[5]);
        }

        var fileName = "instances/" + instance + ".txt";

        //tabu tenure
        int t1 = 10, t2 = 5;
        //in case of std+int (intensification) tenure is null
        //then in the constructor the value will be set proportionally with the instance size

        var stdOut = System.out;

        //redirect the output to a file
        var outputPath = "results/" + outputFolder + "/" + instance + "_t" + maxTimeSecs + "_seed" + seed + ".txt";
        PrintStream out = new PrintStream(new FileOutputStream(outputPath, false)); // true = append
        System.setOut(out);

        try {
            if (method.equals("std")) {
                printHeader(fileName, method);
                var solver = new TSSCQBF(seed, target, t1, maxTimeSecs, false, fileName, false, false);
                solver.solve();
            } else if (method.equals("std+t2")) {
                printHeader(fileName, method);
                var solver = new TSSCQBF(seed, target, t2, maxTimeSecs, false, fileName, false, false);
                solver.solve();
            } else if (method.equals("std+best")) {
                printHeader(fileName, method);
                var solver = new TSSCQBF(seed, target, t1, maxTimeSecs, true, fileName, false, false);
                solver.solve();
            } else if (method.equals("std+div")) {
                printHeader(fileName, method);
                var solver = new TSSCQBF(seed, target, t1, maxTimeSecs, false, fileName, true, false);
                solver.solve();
            } else if (method.equals("std+int")) {
                printHeader(fileName, method);
                var solver = new TSSCQBF(seed, target, null, maxTimeSecs, false, fileName, false, true);
                solver.solve();
            }
        } catch (FileNotFoundException e) {
            System.setOut(stdOut);
            System.out.println("Wrong instance name");
        }
    }

    private static void printHeader(String instance, String method) {
        System.out.printf("instance=%s method=%s\n", instance, method);
    }
}