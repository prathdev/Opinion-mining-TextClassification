set terminal jpeg
set output "graphs/FirstSentence.jpg"
set title "First Sentence Based approach"
set xlabel "Threshold"
set ylabel "Accuracy"
plot  'graphs/FirstSentence.txt' using 1:2 with lines title 'First Sentence Based' lw 3
