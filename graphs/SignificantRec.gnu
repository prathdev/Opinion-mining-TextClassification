set terminal jpeg
set output "SignificantRec.jpg"
set title "Significant Method for all input"
set xlabel "No. of Input Files"
set ylabel "Maximum Accuracy"
plot 'SignificantRec.txt' using 1:2 with lines title 'Significant Method for all input/' lw 3
