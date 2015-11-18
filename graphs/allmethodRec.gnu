set terminal jpeg
set output "allmethodRec.jpg"
set title "allmethod Method for all input"
set xlabel "No. of Input Files"
set ylabel "Maximum Accuracy"
plot 'allmethodRec.txt' using 1:2 with lines title 'allmethod Method for all input/' lw 3
