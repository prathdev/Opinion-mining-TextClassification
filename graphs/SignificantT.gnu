set terminal jpeg
set output "SignificantT.jpg"
set title "Significant Tagged Method"
set xlabel  "threshold"
set ylabel  "accuracy"
plot 'SignificantT.txt' using 1:2 with lines title 'Significant Tagged Method' lw 3
