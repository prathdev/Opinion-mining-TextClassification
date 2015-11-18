set terminal jpeg
set output "graphs/OtherPOSFirst.jpg"
set title "Other POS first sentence approach"
set xlabel "Threshold"
set ylabel "Accuracy"
plot  'graphs/OtherPOSFirst.txt' using 1:2 with lines title 'Other POS First sentence/' lw 3
