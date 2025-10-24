for file in instances/*.txt; do
    filename=$(basename "$file")
    echo "running for instance $filename..."
    python maxscqbf_ilp.py < "instances/$filename" > "results/$filename"
done
