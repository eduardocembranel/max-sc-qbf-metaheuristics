run_experiment() {
  local res_folder="$1"
  local inst="$2"
  local time_max="$3"
  local seed="$4"
  local target="${5:-}" # optional argument

  if [ -z "$target" ]; then
    java -cp out Main "$res_folder" "$inst" std+int "$time_max" "$seed"
  else
    java -cp out Main "$res_folder" "$inst" std+int "$time_max" "$seed" "$target"
  fi
}
