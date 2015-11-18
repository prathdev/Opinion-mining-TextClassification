set terminal jpeg
set output "graphs/OtherPOSLast.jpg"
set title "Other POS last sentence approach"
set xlabel "Threshold"
set ylabel "Accuracy"
plot  'graphs/OtherPOSLast.txt' using 1:2 with lines title 'Other POS Last sentence' lw 3
