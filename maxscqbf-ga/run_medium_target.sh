source ./functions.sh

for seed in {1..50}; do
  run_experiment "medium-target" "exact_n200" 600 "$seed" 21307.6925
done