set terminal jpeg
set output "lastSentenceVRec.jpg"
set title "lastSentenceV Method for all input"
set xlabel "No. of Input Files"
set ylabel "Maximum Accuracy"
plot 'lastSentenceVRec.txt' using 1:2 with lines title 'lastSentenceV Method for all input/' lw 3
