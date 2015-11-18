set terminal jpeg
set output "graphs/Significant.jpg"
set title "Significant Tagged Method"
set xlabel  "threshold"
set ylabel  "accuracy"
plot 'graphs/Significant.txt' using 1:2 with lines title 'Significant Tagged Method' lw 3
