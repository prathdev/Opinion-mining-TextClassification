set terminal jpeg
set output "AdjectiveCntRec.jpg"
set title "AdjectiveCnt Method for all input"
set xlabel "No. of Input Files"
set ylabel "Maximum Accuracy"
plot 'AdjectiveCntRec.txt' using 1:2 with lines title 'AdjectiveCnt Method for all input/' lw 3
