set terminal jpeg
set output "graphs/OtherPOSAll.jpg"
set title "Other POS First,Last and Significant approach"
set xlabel "Threshold"
set ylabel "Accuracy"
plot  'graphs/OtherPOSAll.txt' using 1:2 with lines title 'Other POS Patterns' lw 3
