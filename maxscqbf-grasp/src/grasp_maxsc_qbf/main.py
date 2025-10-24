"""
Main execution script for GRASP MAX-SC-QBF
"""

import sys
import os
import time

# Adicionar o diretório atual ao path para importar os módulos
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

# Importar os módulos locais
from algorithms.grasp_qbf_sc import GRASP_QBF_SC

def main():
    file_path = "../../instances/"
    maxTimeSec = 0
    seed = 0
    target = float('inf')

    if len(sys.argv) == 4 or len(sys.argv) == 5:
        file_name = sys.argv[1]
        file_path += file_name
        maxTimeSec = int(sys.argv[2])
        seed = int(sys.argv[3])

        if len(sys.argv) == 5:
            target = float(sys.argv[4])
    else:
        raise "wrong number of arguments"
    
    print(f"Running for instance={file_path}, maxTime={maxTimeSec}, seed={seed}, target={target}")
    print("=" * 90)

    alpha = 0.1
    local_search = "first_improving"
    construction = "sampled_greedy"
    #construction = "standard" #random_plus_greedy or sampled_greedy

    start_time = time.time()
    
    grasp = GRASP_QBF_SC(
        seed=seed,
        target=target,
        alpha=alpha, 
        maxTimeSec=maxTimeSec,
        filename=file_path,
        construction_method=construction,
        local_search_method=local_search
    )
    
    best_sol = grasp.solve()
    
    end_time = time.time()
    
    print(f"Cost: {best_sol.cost}, Size: {len(best_sol)}, Time: {end_time - start_time:.3f}s")

if __name__ == "__main__":
    main()