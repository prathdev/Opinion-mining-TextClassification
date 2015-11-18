#!/bin/bash
i=10;
for(( i = 10 ; i < =50 ; i+=10 ))
do
javac AdjectiveCnt.java
java AdjectiveCnt input$i/*.txt 
javac allmethod.java
java allmethod input$i/*.txt 
javac FirstSentence.java
java FirstSentence input$i/*.txt
javac FirstSentenceV.java 
java FirstSentenceV input$i/*.txt
javac lastSentence.java 
java lastSentence input$i/*.txt 
javac lastSentenceV.java
java lastSentenceV input$i/*.txt 
javac OtherPOS.java
java OtherPOS input$i/*.txt 
javac OtherPOSAll.java
java OtherPOSAll input$i/*.txt 
javac OtherPOSFirst.java
java OtherPOSFirst input$i/*.txt
javac OtherPOSLast.java 
java OtherPOSLast input$i/*.txt
javac OtherPOSSig.java 
java OtherPOSSig input$i/*.txt 
javac Significant.java
java Significant input$i/*.txt
javac Threshold.java 
java Threshold input$i/*.txt  
done 

