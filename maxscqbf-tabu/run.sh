#!/bin/bash

# Maximum number of parallel jobs
MAX_JOBS=3

# List of instances
INSTANCES=(
  normal_n200
  exact_n200
  exp_n200
)

# Loop over all instances
for inst in "${INSTANCES[@]}"; do
  echo "Starting execution for $inst ..."

  java -cp out Main "$inst" std+int 0 &

  # Check how many jobs are running and wait if limit reached
  while (( $(jobs -r | wc -l) >= MAX_JOBS )); do
    sleep 10
  done
done

# Wait for all background jobs to finish
wait
echo "All executions finished!"

#ps aux | grep java
#pkill -f "java -cp out Main"
