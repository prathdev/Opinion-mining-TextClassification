
#!/bin/bash

lines=`wc -l $1 | cut -c 1-2`
echo $lines
three=3;
echo $three
lines2=`dc -e "$lines 3 - n"`

echo $lines2
tail -$lines2 $1 > $1.txt
