set terminal jpeg
set output "graphs/OtherPOSSig.jpg"
set title "Other POS significant sentence approach"
set xlabel "Threshold"
set ylabel "Accuracy"
plot  'graphs/OtherPOSSig.txt' using 1:2 with lines title 'Other POS significant sentence/' lw 3
