set terminal jpeg
set output "graphs/OtherPOS.jpg"
set title "Other POS Patterns Based approach"
set xlabel "Threshold"
set ylabel "Accuracy"
plot  'graphs/OtherPOS.txt' using 1:2 with lines title 'Other POS Patterns' lw 3
