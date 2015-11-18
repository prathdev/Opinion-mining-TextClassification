set terminal jpeg
set output "OtherPOSFirstRec.jpg"
set title "OtherPOSFirst Method for all input"
set xlabel "No. of Input Files"
set ylabel "Maximum Accuracy"
plot 'OtherPOSFirstRec.txt' using 1:2 with lines title 'OtherPOSFirst Method for all input/' lw 3
