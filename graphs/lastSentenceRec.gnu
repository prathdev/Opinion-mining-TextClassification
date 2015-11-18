set terminal jpeg
set output "lastSentenceRec.jpg"
set title "lastSentence Method for all input"
set xlabel "No. of Input Files"
set ylabel "Maximum Accuracy"
plot 'lastSentenceRec.txt' using 1:2 with lines title 'lastSentence Method for all input/' lw 3
