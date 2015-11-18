package Basic;

import java.io.*;
import java.util.*;
import Basic.hashing.*;
import montytagger.JMontyTagger;
import Basic.input.infile;


class infileFirstSentence extends infile
{

    infileFirstSentence(int size)
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

	String f_getFirstSentence(String filename) throws IOException
	{
	str.delete(0,str.length());
	
	  while (((ch=f.read())!=-1) && !(ch == '.' || ch == '!' || ch == ';' || ch == '?')) 
        {
            str.append((char)ch);
        }
        return str.toString();
	
	}


}



public class FirstSentence
{
	public float find(String v,JMontyTagger j) throws IOException
	{
		infileFirstSentence input=new infileFirstSentence(1);
  
        String s=new String();
        int i;
        int adjectiveCount=0; 
	   int wordCount;	
        hashing h1 =  new hashing("positive.txt");
        hashing h2 =  new hashing("negative.txt");
        
            input.f_open(v);
            //storing the contents of the file in a string to be used by the Monty Tagger
	    	String inputString = input.f_getFirstSentence(v);
			//System.out.println(v+"    "+inputString);
		wordCount = input.getWordCount(inputString)+1;
		//System.out.println("wordcount = "+wordCount);
			String outputString = j.Tag(inputString);
			Scanner sc = new Scanner(outputString);
            while (sc.hasNext())
            {	
            	//Use scanner to extract words from the tagged text.
                //if the word read is an adjective and is in positive.txt or negative.txt icrement adjective count
                //threshold for each file is calc as no of adj/ no of sent 
                //threshold to be set to different values by the program and prog to be run on the files
                s = sc.next();
				
				
				
               
                if(s.matches(".*/JJ.*"))
                {
                	//removing the tag part to compare with the positive and negative lists
               	String candidateAdj = s.substring(0,s.indexOf('/'));
                    if (h1.check(candidateAdj)|| h2.check(candidateAdj))
                    {
                        adjectiveCount++;
                       
                    }
                }
               
            }
			//System.out.println("adjective count = "+adjectiveCount);
   return (float)adjectiveCount/(float)wordCount;
	}
}
    




class FirstSentenceMain
{

    //take input from command line as a directory containing the reviews and articles

    public static void main(String v[]) throws IOException
    {
	
		//using the monty tagger for POS tagging   	  
		JMontyTagger j = new JMontyTagger();
    	   
        //infile input=new infile(v.length);
  
        String s=new String();
        int i;
		int wordCount=1;
        float[] adjectiveCount=new float[v.length];
        float finalThreshold = 0.0f,threshold,from,to;
        float adjectiveSentenceRatio[] = new float[v.length];
        int matchCount = 0;
        float accuracy,maxAccuracy=0.0f,minAdjectiveCount=10000,maxAdjectiveCount=0,avgAdjectiveCount;
		boolean delimit=false;
		FileWriter data = new FileWriter("graphs/FirstSentence.txt");
		FileWriter plot = new FileWriter("graphs/FirstSentence.gnu");	
		FileWriter record=new FileWriter("graphs/FirstSentenceRec.txt",true);
        hashing h1 =  new hashing("positive.txt");
        hashing h2 =  new hashing("negative.txt");
        
		termHash factual = new termHash("input50/Factual.txt");
        termHash opinion = new termHash("input50/opinion.txt");

		/*Enumeration e = factual.ht.keys();
		for(;e.hasMoreElements();)
		{
			System.out.println(e.nextElement());
		}*/
        //iterating over every file in the directories
        for (i=0; i<v.length; i++)
        {
            //input.f_open(v[i]);
			adjectiveCount[i] = 0;
			if((new File(v[i]).getName()).compareTo("opinion.txt")==0 || (new File(v[i]).getName()).compareTo("Factual.txt")==0)
			{
				adjectiveCount[i]=-1;
			}
			else
			{
            //storing the contents of the file in a string to be used by the Monty Tagger
	    	
          
			adjectiveCount[i] = new FirstSentence().find(v[i],j);
			//System.out.println(adjectiveCount[i]);
            if (adjectiveCount[i]<minAdjectiveCount)
            {
                minAdjectiveCount = adjectiveCount[i];
            }
            if (adjectiveCount[i]>maxAdjectiveCount)
            {
                maxAdjectiveCount = adjectiveCount[i];
            }
           // System.out.println("\nFile :"+v[i]+" Adjective count = " + adjectiveCount[i]+" String:"+inputString);
		   }


        }//end for
		//System.out.println(minAdjectiveCount+"	"+maxAdjectiveCount);

        avgAdjectiveCount = (minAdjectiveCount + maxAdjectiveCount)/2;
        //System.out.println("Average adjective count: "+avgAdjectiveCount);
        from = 0;
        to = avgAdjectiveCount + 0.5f;
        for (threshold = from; threshold<= to; threshold+=0.1)
        {
        	matchCount=0;
            for (i=0; i<v.length; i++)
            {
                //if ((adjectiveCount[i] < threshold && v[i].matches("factual.*")) || (adjectiveCount[i] >= threshold && v[i].matches("opinionated.*")))
			//String path = v[i];
		   //String filename = new File(path).getName();
		   //System.out.println("File :"+ v[i] +"  got:"+filename);
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

		  //threshold=threshold/wordCount;

            //System.out.println("Threshold : "+threshold+"    Accuracy : "+accuracy);
            data.write(threshold+" "+accuracy+"\n");
        }
        //System.out.println("Final threshold : "+finalThreshold +"\n Max Accuracy : "+maxAccuracy);
	plot.write("set terminal jpeg\n");
	plot.write("set output \"graphs/FirstSentence.jpg\"\n");
	plot.write("set title \"First Sentence Based approach\"\n");
	plot.write("set xlabel \"Threshold\"\n");
	plot.write("set ylabel \"Accuracy\"\n");
	plot.write("plot  \'graphs/FirstSentence.txt\' using 1:2 with lines title \'First Sentence Based\' lw 3\n");

	record.write(v.length+" "+maxAccuracy+"\n");
	record.close();
	plot.close();
	data.close();
    }//end main
    
}//end class



