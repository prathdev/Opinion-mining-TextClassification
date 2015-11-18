set terminal jpeg
set output "ThresholdRec.jpg"
set title "Threshold Method for all input"
set xlabel "No. of Input Files"
set ylabel "Maximum Accuracy"
plot 'ThresholdRec.txt' using 1:2 with lines title 'Threshold Method for all input/' lw 3
