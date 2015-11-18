set terminal jpeg
set output "OtherPOSRec.jpg"
set title "OtherPOS Method for all input"
set xlabel "No. of Input Files"
set ylabel "Maximum Accuracy"
plot 'OtherPOSRec.txt' using 1:2 with lines title 'OtherPOS Method for all input/' lw 3
