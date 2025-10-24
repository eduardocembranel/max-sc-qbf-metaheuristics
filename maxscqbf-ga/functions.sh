run_experiment() {
  local res_folder="$1"
  local inst="$2"
  local time_max="$3"
  local seed="$4"
  local target="${5:-}" # optional argument

  local output_file="./results/${res_folder}/${inst}_t${time_max}_seed${seed}.txt"

  if [ -z "$target" ]; then
    java -cp out problems.qbf.solvers.GA_QBF_SC --instance "instances/max_sc_qbf/${inst}.txt" --seed $seed --generations 1000 --popSize 100 --mutation 0.01 --useLHS false --useUC false --timeLimit $time_max > "$output_file"
  else
    java -cp out problems.qbf.solvers.GA_QBF_SC --instance "instances/max_sc_qbf/${inst}.txt" --seed $seed --generations 1000 --popSize 100 --mutation 0.01 --useLHS false --useUC false --timeLimit $time_max --target $target > "$output_file"
  fi
}
