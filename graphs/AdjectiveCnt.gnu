set terminal jpeg
set output "graphs/AdjectiveCnt.jpg"
set title "Adjective Count approach"
set xlabel "Threshold"
set ylabel "Accuracy"
plot  'graphs/AdjectiveCnt.txt' using 1:2 with lines title 'Adjective Count tagged approach/' lw 3
