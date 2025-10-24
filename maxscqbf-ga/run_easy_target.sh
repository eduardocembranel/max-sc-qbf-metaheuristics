source ./functions.sh

for seed in {1..50}; do
  run_experiment "easy-target" "exact_n200" 600 "$seed" 20186.235
done