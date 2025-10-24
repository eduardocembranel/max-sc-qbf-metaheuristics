run_experiment() {
  local res_folder="$1"
  local file_name="$2"
  local time_max="$3"
  local seed="$4"
  local target="${5:-}" # optional argument

  local output_file="../../results/${res_folder}/${file_name%.*}_t${time_max}_seed${seed}.txt"

  if [ -z "$target" ]; then
    python3 main.py "$file_name" "$time_max" "$seed" > "$output_file"
  else
    python3 main.py "$file_name" "$time_max" "$seed" "$target" > "$output_file"
  fi
}
