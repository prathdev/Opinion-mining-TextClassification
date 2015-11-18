set terminal jpeg
set output "CueBasedRec.jpg"
set title "CueBased Method for all input"
set xlabel "No. of Input Files"
set ylabel "Maximum Accuracy"
plot 'CueBasedRec.txt' using 1:2 with lines title 'CueBased Method for all input/' lw 3
