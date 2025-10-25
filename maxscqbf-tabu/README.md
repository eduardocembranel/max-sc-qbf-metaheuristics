# Tabu Search

This project implements five methods of Tabu Search variations to solve the **MAX-SC-QBF** problem

## Requirements

- Java JDK 17+
- Unix based operational system (optional)

## Compile (Linux or Mac)

```
javac -d out $(find src -name "*.java")
```

## Run experiments

To run the `intensification` heuristic along all the instances
```bash
./run_all_instances.sh
````

To run `intensification` heuristic with an spectific target with 50 independent executions for the instance `exact_n200`, in order to have empirical data for later analasys
```
# With an "easy" target
./run_easy_target.sh

# With an "medium" target
./run_easy_target.sh

# With an "hard" target
./run_easy_target.sh
```

## Run for a single instance

```
java -cp out Main <instance_name> <method>
```

####  Run example for the instance *exact_n25* with method *std*
```
java -cp out Main exact_n25 std
```

#### Available instances are those present in the `./instances` and the <instance_name> is the name of the file (without the extension)

#### Available methods are:
* std
* std+t2
* std+best
* std+div
* std+int

## See the results

The result output will be available in the file `./results/<method>/<instance_name>.txt`
