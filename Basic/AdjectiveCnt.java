package Basic;

import java.io.*;
import java.util.*;
import Basic.hashing.*;
import montytagger.JMontyTagger;
import Basic.input.infile;

class infileAdjectiveCnt extends infile
{


    infileAdjectiveCnt(int size)
    {
        sentenceCount=new int[size];
    }


    public String fread(int i) throws IOException
    {
        str.delete(0,str.length());
        //if the character is a delimiter like . or ! and is not preceded by a delimiter, increment sentence count

        while (((ch=f.read())!=-1) && (!delimiter(ch)) )
        {
            str.append((char)ch);
            delimit = false;
        }
        if (ch==-1) return null;
        if (delimit == false && (ch == '.' || ch == '!' || ch == ';' || ch == '?'))
            sentenceCount[i]++;

        delimit=true;
        return str.toString().toLowerCase();
    }


    String f_storeToString(String filename)	throws IOException
    {
     BufferedReader reader = new BufferedReader( new FileReader (filename));
    String line  = null;
    StringBuilder stringBuilder = new StringBuilder();
    String ls = System.getProperty("line.separator");
    while( ( line = reader.readLine() ) != null )
    	{
        	stringBuilder.append( line );
        	stringBuilder.append( ls );
    	}
    return stringBuilder.toString();
    }


}




class AdjectiveCnt
{

    //take input from command line as a directory containing the reviews and articles

    public static void main(String v[]) throws IOException
    {
	
		//using the monty tagger for POS tagging   	  
		JMontyTagger j = new JMontyTagger();
    	   
        infileAdjectiveCnt input=new infileAdjectiveCnt(v.length);
  	   FileWriter data = new FileWriter("graphs/AdjectiveCnt.txt");
	   FileWriter plot = new FileWriter("graphs/AdjectiveCnt.gnu");
	   FileWriter record=new FileWriter("graphs/AdjectiveCntRec.txt",true);
        String s=new String();
        int i;
        int[] adjectiveCount=new int[v.length];
        float finalThreshold = 0.0f,threshold,from,to;
        float adjectiveSentenceRatio[] = new float[v.length];
        int matchCount = 0;
        float accuracy,maxAccuracy=0.0f,minAdjSentenceRatio=10000,maxAdjSentenceRatio=0,avgAdjSentenceRatio;
	  boolean delimit=false;
		
        hashing h1 =  new hashing("positive.txt");
        hashing h2 =  new hashing("negative.txt");
        
		hashing factual = new hashing("input50/Factual.txt");
        hashing opinion = new hashing("input50/Opinion.txt");

        //iterating over every file in the directories
        for (i=0; i<v.length; i++)
        {
            input.f_open(v[i]);
			
			adjectiveCount[i] = 0;
			if((new File(v[i]).getName()).compareTo("opinion.txt")==0 || (new File(v[i]).getName()).compareTo("Factual.txt")==0)
			{
				adjectiveCount[i]=-1;
			}
			
			else
			{
            //storing the contents of the file in a string to be used by the Monty Tagger
	    	String inputString = input.f_storeToString(v[i]);
			String outputString = j.Tag(inputString);
			Scanner sc = new Scanner(outputString);
            while (sc.hasNext())
            {	
            	//Use scanner to extract words from the tagged text.
                //if the word read is an adjective and is in positive.txt or negative.txt icrement adjective count
                //threshold for each file is calc as no of adj/ no of sent 
                //threshold to be set to different values by the program and prog to be run on the files
                s = sc.next();
                if((s.contains("/.")) && (s.contains(".") || s.contains("!") || s.contains(";")|| s.contains("?"))  && delimit == false)
                {
                delimit = true;
                input.sentenceCount[i]++;
                }
                else
                delimit = false;
                if(s.matches(".*/JJ.*"))
                {
                	//removing the tag part to compare with the positive and negative lists
               	String candidateAdj = s.substring(0,s.indexOf('/'));
                    if (h1.check(candidateAdj)|| h2.check(candidateAdj))
                    {
                        adjectiveCount[i]++;
                       
                    }
                }
               
            }
            adjectiveSentenceRatio[i] = (float)adjectiveCount[i]/input.sentenceCount[i];
            if (adjectiveSentenceRatio[i]<minAdjSentenceRatio)
            {
                minAdjSentenceRatio = adjectiveSentenceRatio[i];
            }
            if (adjectiveSentenceRatio[i]>maxAdjSentenceRatio)
            {
                maxAdjSentenceRatio = adjectiveSentenceRatio[i];
            }
           /* System.out.println("\nAdjective count = " + adjectiveCount[i] + "\tSentence Count = " + input.sentenceCount[i]);

            System.out.println("File :"+v[i]+" Adjective sentence ratio " + adjectiveSentenceRatio[i]);*/


		}
        }//end for


        avgAdjSentenceRatio = (minAdjSentenceRatio + maxAdjSentenceRatio)/2;
        //System.out.println("Average adjective sentence ratio: "+avgAdjSentenceRatio);
        from = 0;
        to = avgAdjSentenceRatio + 0.5f;
        for (threshold = from; threshold<= to; threshold+=0.1)
        {
        	matchCount=0;
            for (i=0; i<v.length; i++)
            {
                /*if ((adjectiveSentenceRatio[i] < threshold && v[i].matches("factual.*")) || (adjectiveSentenceRatio[i] >= threshold && v[i].matches("opinionated.*")))*/
String path = v[i];
		   String filename = new File(path).getName();
		  
               if ((adjectiveSentenceRatio[i] < threshold && factual.check(filename)) || (adjectiveSentenceRatio[i] >= threshold && opinion.check(filename)))

                    matchCount++;
            }
			if(v[0].indexOf("input50")>=0)
            accuracy = (float)matchCount/(v.length-2);
			else
			accuracy = (float)matchCount/(v.length);
            if (maxAccuracy < accuracy)
            {
                maxAccuracy = accuracy;
                finalThreshold = threshold;
            }if (maxAccuracy < accuracy)
                {
				if(maxAccuracy==accuracy)
				{
				float diff1=avgAdjSentenceRatio-threshold;
				float diff2=avgAdjSentenceRatio-finalThreshold;
				if(diff1<0.0) diff1=-diff1;
				if(diff2<0.0) diff2=-diff2;
				if(diff1<diff2)
				{
				finalThreshold=threshold;
				maxAccuracy=accuracy;
				}
				}
			
			else
				{
                    maxAccuracy = accuracy;
                    finalThreshold = threshold;
				}
			}

            //System.out.println("Threshold : "+threshold+"    Accuracy : "+accuracy);
data.write(threshold+" "+accuracy+"\n");
        }
        //System.out.println("Final threshold : "+finalThreshold +"\n Max Accuracy : "+maxAccuracy);


plot.write("set terminal jpeg\n");
	plot.write("set output \"graphs/AdjectiveCnt.jpg\"\n");
	plot.write("set title \"Adjective Count approach\"\n");
	plot.write("set xlabel \"Threshold\"\n");
	plot.write("set ylabel \"Accuracy\"\n");
	plot.write("plot  \'graphs/AdjectiveCnt.txt\' using 1:2 with lines title \'Adjective Count tagged approach/\' lw 3\n");


record.write(v.length+" "+maxAccuracy+"\n");
record.close();
plot.close();
data.close();    }//end main
    
}//end class



