set terminal jpeg
set output "OtherPOSSigRec.jpg"
set title "OtherPOSSig Method for all input"
set xlabel "No. of Input Files"
set ylabel "Maximum Accuracy"
plot 'OtherPOSSigRec.txt' using 1:2 with lines title 'OtherPOSSig Method for all input/' lw 3
