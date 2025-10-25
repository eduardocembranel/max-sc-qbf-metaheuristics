# MAX-SC-QBF Solver

This project implements a program to solve the **MAX-SC-QBF** (Maximum Set Covering Quadratic Binary Function) problem using **Integer Linear Programming (ILP)** with the **Gurobi** solver.

## Requirements

- Unix-based operating system
- Python 3.11 or higher  
- [Gurobi Optimizer](https://www.gurobi.com/) version **12.0.3**  
- Valid Gurobi license

## Installation

### 1. Clone the repository

```bash
git clone https://github.com/eduardocembranel/max-sc-qbf.git
cd max-sc-qbf
```

### 2. Create and activate a virtual environment

```bash
python3 -m venv venv
source venv/bin/activate
```

### 3. Install the packages

```bash
pip install -r requirements.txt
```

## Run experiments

To run the `solver` along all the instances with time limit of 30 minutes
```bash
./run_all_instances.sh
````

## Running for a single instance

```bash
python maxscqbf_ilp.py < instances/<file_name>
````

