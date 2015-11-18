set terminal jpeg
set output "FirstSentenceVRec.jpg"
set title "FirstSentenceV Method for all input"
set xlabel "No. of Input Files"
set ylabel "Maximum Accuracy"
plot 'FirstSentenceVRec.txt' using 1:2 with lines title 'FirstSentenceV Method for all input/' lw 3
