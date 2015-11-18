#!/bin/bash
i=2;
for(( i = 2 ; i < 51 ; i++ ))
do
vi `3dd` `wq` $i.txt 
done
