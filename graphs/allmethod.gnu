set terminal jpeg
set output "graphs/allmethod.jpg"
set title "All tagged methods"
set xlabel  "threshold"
set ylabel  "accuracy"
plot 'graphs/allmethod.txt' using 1:2 with lines title 'All tagged methods' lw 3
