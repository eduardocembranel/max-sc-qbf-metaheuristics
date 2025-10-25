# GRASP

A modular implementation of the **GRASP** (Greedy Randomized Adaptive Search Procedure) algorithm to solve the **MAX-SC-QBF** problem.

## Requirements

- Python 3
- Unix-based operating system

## 🚀 How to Use

### Run experiments

To run the `sampled_greedy` heuristic along all the instances
```bash
cd src/grasp_maxsc_qbf
./run_all_instances.sh
````

To run `sampled_greedy` heuristic with an spectific target with 50 independent executions for the instance `exact_n200`, in order to have empirical data for later analasys
```
cd src/grasp_maxsc_qbf

# With an "easy" target
./run_easy_target.sh

# With an "medium" target
./run_easy_target.sh

# With an "hard" target
./run_easy_target.sh
```

### Basic Execution
```bash
# Run with a specific instance
python main.py instance.txt

# With custom parameters
python main.py instance.txt 0.3 100 standard first_improving

# Experiments - runs all instances in the 'instances' folder
python main.py --experiment
```

## ⚙️ Available Configurations

### Construction Methods
- **`standard`**: Classic GRASP with RCL based on α
- **`random_plus_greedy`**: 30% random selection + greedy completion
- **`sampled_greedy`**: Samples 50% of the candidates and selects the best
- **`pop_in_construction`**: Performs local search steps throughout the classic construction

### Local Search Methods
- **`first_improving`**: Stops at the first improving move
- **`best_improving`**: Evaluates all moves and selects the best

### Parameters
- **`alpha`** (0.0–1.0): Controls greediness vs randomness (0 = greedy, 1 = random)
- **`iterations`**: Number of GRASP iterations
