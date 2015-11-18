set terminal jpeg
set output "OtherPOSLastRec.jpg"
set title "OtherPOSLast Method for all input"
set xlabel "No. of Input Files"
set ylabel "Maximum Accuracy"
plot 'OtherPOSLastRec.txt' using 1:2 with lines title 'OtherPOSLast Method for all input/' lw 3
