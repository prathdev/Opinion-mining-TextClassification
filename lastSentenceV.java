package Basic;

import java.io.*;
import java.util.*;
import Basic.hashing.*;
import Basic.input.infile;

class infilelastSentenceV extends infile
{


    infilelastSentenceV(int size)
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

int getWordCount(String st) throws IOException
{
	
	int i=0,count=0;
	
	for(i=0;i<st.length();i++)
	{
	 if(st.charAt(i) == ',' || st.charAt(i)=='!' || st.charAt(i)=='?' || st.charAt(i)==' ')
	 count++;
	 }
	//System.out.println("count of words: "+count);
	return count;
}


    String getLastSentence(int i) throws IOException
    {
    	String lastSentence = new String();
        str.delete(0,str.length());
        //if the character is a delimiter like . or ! and is not preceded by a delimiter, increment sentence count
	  if ((ch=f.read())==-1) return null;
	  
        while (((ch=f.read())!=-1)  )
        {
        	if(!(ch == '.' || ch == '!' || ch == ';' || ch == '?'))
        	{
            str.append((char)ch);
            delimit = false;
        	}
        
        if (delimit == false && (ch == '.' || ch == '!' || ch == ';' || ch == '?'))
        {
            //sentenceCount[i]++;
            lastSentence = str.toString();
             str.delete(0,str.length());
             delimit=true;
        }
	}
        
        return lastSentence;
    }
 }

    class lastSentenceV
    {

        //take input from command line as a directory containing the reviews and articles

        public static void main(String v[]) throws IOException
        {

            infilelastSentenceV input=new infilelastSentenceV(v.length);

            String s=new String();
            int i;
            float[] adjectiveCount=new float[v.length];
            float finalThreshold = 0.0f,threshold,from,to;
            float adjectiveSentenceRatio[] = new float[v.length];
            int matchCount = 0;
            float accuracy,maxAccuracy=0.0f,minAdjectiveCount=10000,maxAdjectiveCount=0,avgAdjectiveCount;
            boolean delimit=false;
	    FileWriter data = new FileWriter("graphs/lastSentenceV.txt");	
	    FileWriter plot = new FileWriter("graphs/lastSentenceV.gnu");	
		FileWriter record=new FileWriter("graphs/lastSentenceVRec.txt",true);
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
                String inputString = input.getLastSentence(i);

			int wordCount=input.getWordCount(inputString)+1;
                //System.out.println("File: "+v[i]+"input last sentence:"+inputString);
              
                //System.out.println("output last sentence:"+outputString);
                Scanner sc = new Scanner(inputString);
                while (sc.hasNext())
                {
                    //Use scanner to extract words from the tagged text.
                    //if the word read is an adjective and is in positive.txt or negative.txt icrement adjective count
                    //threshold for each file is calc as no of adj/ no of sent
                    //threshold to be set to different values by the program and prog to be run on the files
                    s = sc.next();

                     
               if(h1.check(s) || h2.check(s))
               adjectiveCount[i]++;
               

                }
			adjectiveCount[i]/=wordCount;

                if (adjectiveCount[i]<minAdjectiveCount)
                {
                    minAdjectiveCount = adjectiveCount[i];
                }
                if (adjectiveCount[i]>maxAdjectiveCount)
                {
                    maxAdjectiveCount = adjectiveCount[i];
                }
                //System.out.println("\nFile :"+v[i]+" Adjective count = " + adjectiveCount[i]+" String:"+inputString);
			}
			


            }//end for


            avgAdjectiveCount = (minAdjectiveCount + maxAdjectiveCount)/2;
            //System.out.println("Average adjective count: "+avgAdjectiveCount);
            from = 0;
            to = avgAdjectiveCount + 0.5f;
            for (threshold = from; threshold<= to; threshold+=0.1)
            {
                matchCount=0;
                for (i=0; i<v.length; i++)
                {
                   /* if ((adjectiveCount[i] < threshold && v[i].matches("factual.*")) || (adjectiveCount[i] >= threshold && v[i].matches("opinionated.*")))
					String path = v[i];
					String filename = new File(path).getName();*/
		  
				if(adjectiveCount[i]!=-1)
				{
					if ((adjectiveCount[i] < threshold && factual.check(new File(v[i]).getName())) || (adjectiveCount[i] >= threshold && opinion.check(new File(v[i]).getName())))
					//if ((adjectiveCount[i] < threshold && v[i].matches("factual.*")) || (adjectiveCount[i] >= threshold && v[i].matches("opinionated.*")))
						matchCount++;
				}
                }
            if(v[0].indexOf("input50")>=0)
            accuracy = (float)matchCount/(v.length-2);
			else
			accuracy = (float)matchCount/(v.length);
                if (maxAccuracy < accuracy)
                {
				if(maxAccuracy==accuracy)
				{
				float diff1=avgAdjectiveCount-threshold;
				float diff2=avgAdjectiveCount-finalThreshold;
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
	plot.write("set output \"graphs/lastSentenceV.jpg\"\n");
	plot.write("set title \"Last Voting Sentence Based approach\"\n");
	plot.write("set xlabel \"Threshold\"\n");
	plot.write("set ylabel \"Accuracy\"\n");
	plot.write("plot \'graphs/lastSentenceV.txt\' using 1:2 with lines title \'Last Voting Sentence Based\' lw 3\n");

	record.write(v.length+" "+maxAccuracy+"\n");

record.close();

	plot.close();
	data.close();


        }//end main

    }//end class



