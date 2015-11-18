set terminal jpeg
set output "OtherPOSAllRec.jpg"
set title "OtherPOSAll Method for all input"
set xlabel "No. of Input Files"
set ylabel "Maximum Accuracy"
plot 'OtherPOSAllRec.txt' using 1:2 with lines title 'OtherPOSAll Method for all input/' lw 3
