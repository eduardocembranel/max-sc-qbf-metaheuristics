source ./functions.sh

for seed in {1..50}; do
  run_experiment "hard-target" "exact_n200.txt" 600 "$seed" 22204.8585
done
