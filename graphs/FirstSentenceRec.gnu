set terminal jpeg
set output "FirstSentenceRec.jpg"
set title "FirstSentence Method for all input"
set xlabel "No. of Input Files"
set ylabel "Maximum Accuracy"
plot 'FirstSentenceRec.txt' using 1:2 with lines title 'FirstSentence Method for all input/' lw 3
