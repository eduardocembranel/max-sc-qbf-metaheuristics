source ./functions.sh

run_experiment "all-instances" "exact_n400.txt" 1800 0
run_experiment "all-instances" "normal_n400.txt" 1800 0
run_experiment "all-instances" "exp_n400.txt" 1800 0

run_experiment "all-instances" "normal_n200.txt" 1800 0
run_experiment "all-instances" "exact_n200.txt" 1800 0
run_experiment "all-instances" "exp_n200.txt" 1800 0

# for smaller instances that we know the optimal value, we can set the target
# to stop the heuristic when the optimal value is found
run_experiment "all-instances" "exact_n100.txt" 1800 0 54155
run_experiment "all-instances" "normal_n100.txt" 1800 0 48902
run_experiment "all-instances" "exp_n100.txt" 1800 0 49715

run_experiment "all-instances" "exact_n50.txt" 1800 0 2284
run_experiment "all-instances" "normal_n50.txt" 1800 0 2896
run_experiment "all-instances" "exp_n50.txt" 1800 0 2244.87

run_experiment "all-instances" "exact_n25.txt" 1800 0 1004.74
run_experiment "all-instances" "normal_n25.txt" 1800 0 966.04
run_experiment "all-instances" "exp_n25.txt" 1800 0 751.9
