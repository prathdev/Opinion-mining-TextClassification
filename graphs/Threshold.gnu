set terminal jpeg
set output "graphs/Threshold.jpg"
set title "Document based Voting approach"
set xlabel "Threshold"
set ylabel "Accuracy"
plot  'graphs/Threshold.txt' using 1:2 with lines title 'Document based Voting approach' lw 3
