set terminal jpeg
set output "graphs/CueBased.jpg"
set title "Cue Based approach"
set xlabel "Threshold"
set ylabel "Accuracy"
plot  'graphs/CueBased.txt' using 1:2 with lines title 'Cue Based approach/' lw 3
