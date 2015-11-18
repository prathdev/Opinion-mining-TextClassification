package Basic;

import java.io.*;
import java.util.*;
import Basic.hashing.hashing;
import montytagger.JMontyTagger;
import Basic.input.infile;

class infileOtherPOSLast extends infile
{


    infileOtherPOSLast(int size)
    {
        sentenceCount=new int[size];
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





class OtherPOSLast
{

    //take input from command line as a directory containing the reviews and articles

    public static void main(String v[]) throws IOException
    {
	
		//using the monty tagger for POS tagging   	  
		JMontyTagger j = new JMontyTagger();
    	  	     	   FileWriter data = new FileWriter("graphs/OtherPOSLast.txt");
	   FileWriter plot = new FileWriter("graphs/OtherPOSLast.gnu"); 
	 FileWriter record=new FileWriter("graphs/OtherPOSLastRec.txt",true);

        infileOtherPOSLast input=new infileOtherPOSLast(v.length);
  	  boolean firstTrial = true;
        String s=new String();
        int i;
        float[] patternCount=new float[v.length];
        float finalThreshold = 0.0f,threshold,from,to;
        float patternSentenceRatio[] = new float[v.length];
        int matchCount = 0;
        float accuracy,maxAccuracy=0.0f,minpatternCount=10000,maxpatternCount=0,avgpatternCount,minPatSentenceRatio=10000,maxPatSentenceRatio=0,avgPatSentenceRatio;
	  boolean delimit=false;
	  String prev = null,current = null,next = null;	
        hashing h1 =  new hashing("positive.txt");
        hashing h2 =  new hashing("negative.txt");

		hashing factual = new hashing("input50/Factual.txt");
        hashing opinion = new hashing("input50/Opinion.txt");

        
        //iterating over every file in the directories
        for (i=0; i<v.length; i++)
        {
            input.f_open(v[i]);
			
								patternCount[i] = 0;
		if((new File(v[i]).getName()).compareTo("opinion.txt")==0 || (new File(v[i]).getName()).compareTo("Factual.txt")==0)
		{
			patternCount[i]=-1;
		}
		
		else
		{
            //storing the contents of the file in a string to be used by the Monty Tagger
	    	String inputString = input.getLastSentence(i);
	   
			String outputString = j.Tag(inputString);
			int wordCount=input.getWordCount(inputString)+1;
			Scanner sc = new Scanner(outputString);
			
			if(sc.hasNext())
			prev = sc.next();
			if(sc.hasNext())
			current = sc.next();
            while (sc.hasNext())
            {	
                
               
                if(firstTrial)
                {
                firstTrial = false;
                }
                else
                {
                 prev = current;
                 current = next;
                }
                 next = sc.next();
                
                 
                
                //check for the different cases NEED TO VERIFY IF WE NEED THE .* AT THE END OF THE REG EX ALSO
                if(prev.matches(".*/JJ")) 
                {
                if(current.matches(".*/NN")||current.matches(".*/NNS"))             
                {
                patternCount[i]++;
                }
                else if(current.matches(".*/JJ.*") && !(current.matches(".*/NN.*")||current.matches(".*/NNS.*")))
                		patternCount[i]++;
                }
                else if(prev.matches(".*/RB.*") || prev.matches(".*/RBR.*") || prev.matches(".*/RBS.*"))
                {
                if(current.matches(".*/JJ.*") && !(current.matches(".*/NN.*")||current.matches(".*/NNS.*")))
                	patternCount[i]++;
                else if(current.matches(".*/VB.*") || current.matches(".*/VBD.*") || current.matches(".*/VBG.*") ||current.matches(".*/VBN.*"))
                	patternCount[i]++;
                }
                if(prev.matches(".*/NN.*")||prev.matches(".*/NNS.*"))
                {
                if(current.matches(".*/JJ.*") && !(current.matches(".*/NN.*")||current.matches(".*/NNS.*")))
                patternCount[i]++;
                }
               
             
               
            }
patternCount[i]/=wordCount;
          
            if (patternCount[i]<minpatternCount)
            {
                minpatternCount = patternCount[i];
            }
            if (patternCount[i]>maxpatternCount)
            {
                maxpatternCount = patternCount[i];
            }
           // System.out.println("\nFile :"+v[i]+" pattern count = " + patternCount[i]+" String:"+inputString);


		}
        }//end for


        avgpatternCount = (minpatternCount + maxpatternCount)/2;
       // System.out.println("Average pattern count: "+avgpatternCount);
        //from = avgPatSentenceRatio - 0.5f;
        from = 0;
        to = avgpatternCount + 0.5f;
        for (threshold = from; threshold<= to; threshold+=0.1)
        {
        	matchCount=0;
            for (i=0; i<v.length; i++)
            {
                /*if ((patternCount[i] < threshold && v[i].matches("factual.*")) || (patternCount[i] >= threshold && v[i].matches("opinionated.*")))*/

String path = v[i];
		   String filename = new File(path).getName();
		  
               if ((patternCount[i] < threshold && factual.check(filename)) || (patternCount[i] >= threshold && opinion.check(filename)))

                    matchCount++;
            }
			if(v[0].indexOf("input50")>=0)
            accuracy = (float)matchCount/(v.length-2);
			else
			accuracy = (float)matchCount/(v.length);
            if (maxAccuracy < accuracy)
                {
				if(maxAccuracy==accuracy)
				{
				float diff1=avgpatternCount-threshold;
				float diff2=avgpatternCount-finalThreshold;
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

plot.write("set terminal jpeg\n");
	plot.write("set output \"graphs/OtherPOSLast.jpg\"\n");
	plot.write("set title \"Other POS last sentence approach\"\n");
	plot.write("set xlabel \"Threshold\"\n");
	plot.write("set ylabel \"Accuracy\"\n");
	plot.write("plot  \'graphs/OtherPOSLast.txt\' using 1:2 with lines title \'Other POS Last sentence\' lw 3\n");


record.write(v.length+" "+maxAccuracy+"\n");
record.close();

plot.close();
data.close();
        //System.out.println("Final threshold : "+finalThreshold +"\n Max Accuracy : "+maxAccuracy);

    }//end main
    
}//end class

