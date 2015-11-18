set terminal jpeg
set output "graphs/lastSentence.jpg"
set title "Last Sentence Based approach"
set xlabel "Threshold"
set ylabel "Accuracy"
plot  'graphs/lastSentence.txt' using 1:2 with lines title 'Last Sentence Based' lw 3
