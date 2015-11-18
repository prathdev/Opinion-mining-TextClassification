package Basic;

import java.io.*;
import java.util.*;
import Basic.hashing.hashing;
import montytagger.JMontyTagger;
import Basic.input.infile;

class infileOtherPOSAll extends infile
{

    boolean sentenceEnd;
    int sentCount;

    //int sentenceCount[size];

    

    infileOtherPOSAll(int size)
    {
        sentenceCount=new int[size];
    }

   
 boolean endOfSent(int ch)
    {
        if (ch == '.' || ch == '!' || ch == ';' || ch == '?')
		return true;
        else
            return false;
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
		sentenceEnd = false;
        str.delete(0,str.length());
        //if the character is a delimiter like . or ! and is not preceded by a delimiter, increment sentence count

        while (((ch=f.read())!=-1) && (!endOfSent(ch)) )
        {
            str.append((char)ch);
			sentenceEnd = true;
        }
        str.append((char)ch);
        if (ch==-1) return null;
        
	//Will indicate the end of sentence has been encountered
        
        return str.toString().toLowerCase();
    }



    String f_storeToString(String filename)	throws IOException
    {
        BufferedReader reader = new BufferedReader( new FileReader (filename));
        String line  = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        while ( ( line = reader.readLine() ) != null )
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
    


    String getSigSentence(String v, hashing h1, hashing h2,JMontyTagger j) throws IOException
    {
        int count = 0, adjectiveCount = 0;
        String s, outputString;
        String sentence = null;
        f_open(v);
        //output.f_open("output.txt");
        //boolean stopWord;

	
        while (true)
        {
            //if the word read is in positive.txt or negative.txt icrement adjective count
            //threshold for each file is calc as no of adj/ no of sent ??
            //threshold to be set to different values by the program and prog to be run on the files
            s=fread(1);
            //fread returns a sentence
            if (s!=null)
            {
            	
                outputString = j.Tag(s);
                Scanner sc = new Scanner(outputString);
                //iterate over the tagged sentence word by word
                while (sc.hasNext())
                {
                    String word = new String(sc.next());
                    if (word.matches(".*/JJ.*"))
                    {
                        //removing the tag part to compare with the positive and negative lists
                        String candidateAdj = word.substring(0,word.indexOf('/'));
                        if (h1.check(candidateAdj)|| h2.check(candidateAdj))
                        {
                            count++;

                        }
                    }
                }
                if (adjectiveCount <= count)
                {
                    adjectiveCount = count; //the count of adjectives in this sentence
                    sentence = s;
			
                }
                //reset values for the next sentence

                count =0;
            }
            else
            {
                f_close();
                
                return sentence;
                //break;
                //System.exit(0);
            }
        }

    }



}





class OtherPOSAll
{

    //take input from command line as a directory containing the reviews and articles

    public static void main(String v[]) throws IOException
    {

        //using the monty tagger for POS tagging
        JMontyTagger j = new JMontyTagger();
        
 FileWriter data = new FileWriter("graphs/OtherPOSAll.txt");
	   FileWriter plot = new FileWriter("graphs/OtherPOSAll.gnu");
	   FileWriter record=new FileWriter("graphs/OtherPOSAllRec.txt",true);
        infileOtherPOSAll input=new infileOtherPOSAll(v.length);

        String SigSentence=new String();
        int i,firstSentCount = 0, lastSentCount = 0;
        float[] patternCount=new float[v.length];
        float finalThreshold = 0.0f,threshold,from,to;
        float patternSentenceRatio[] = new float[v.length];
        int matchCount = 0;
        float accuracy,maxAccuracy=0.0f,minpatternCount=10000,maxpatternCount=0,avgpatternCount,minPatSentenceRatio=10000,maxPatSentenceRatio=0,avgPatSentenceRatio;
        boolean delimit=false;

        hashing h1 =  new hashing("positive.txt");
        hashing h2 =  new hashing("negative.txt");

		hashing factual = new hashing("input50/Factual.txt");
        hashing opinion = new hashing("input50/Opinion.txt");


        //iterating over every file in the directories
        for (i=0; i<v.length; i++)
        {
            //input.f_open(v[i]);
            //storing the contents of the file in a string to be used by the Monty Tagger
		input.f_open(v[i]);
		
					patternCount[i] = 0;
		if((new File(v[i]).getName()).compareTo("opinion.txt")==0 || (new File(v[i]).getName()).compareTo("Factual.txt")==0)
		{
			patternCount[i]=-1;
		}
		else
		{
		String lastSentence = input.getLastSentence(i);
		 int wordCount=input.getWordCount(lastSentence)+1;
		 input.f_close();
		input.f_open(v[i]);
		String firstSentence = input.f_getFirstSentence(v[i]);
		wordCount+=input.getWordCount(firstSentence)+1;
		input.f_close();
            //Find significant sentence
            SigSentence = input.getSigSentence(v[i],h1,h2,j);
           wordCount+=input.getWordCount(SigSentence)+1;

           
            //Call the POS function with the sig sentence. This returns the pattern count.
		
            patternCount[i] = POS(SigSentence,j);
           
            firstSentCount = POS(firstSentence,j);
            
		lastSentCount = POS(lastSentence,j);
		
		patternCount[i] = patternCount[i] + firstSentCount + lastSentCount;
		patternCount[i]/=wordCount;
            if (patternCount[i]<minpatternCount)
            {
                minpatternCount = patternCount[i];
            }
            if (patternCount[i]>maxpatternCount)
            {
                maxpatternCount = patternCount[i];
            }
          //  System.out.println("\nFile :"+v[i]+" pattern count = " + patternCount[i]+" String:"+SigSentence);

		}
        }



       


        avgpatternCount = (minpatternCount + maxpatternCount)/2;
        //System.out.println("Average pattern count: "+avgpatternCount);
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

           // System.out.println("Threshold : "+threshold+"    Accuracy : "+accuracy);

data.write(threshold+" "+accuracy+"\n");

        }
plot.write("set terminal jpeg\n");
	plot.write("set output \"graphs/OtherPOSAll.jpg\"\n");
	plot.write("set title \"Other POS First,Last and Significant approach\"\n");
	plot.write("set xlabel \"Threshold\"\n");
	plot.write("set ylabel \"Accuracy\"\n");
	plot.write("plot  \'graphs/OtherPOSAll.txt\' using 1:2 with lines title \'Other POS Patterns\' lw 3\n");


record.write(v.length+" "+maxAccuracy+"\n");
record.close();
plot.close();
data.close();


    }//end main

    static int POS(String inputString, JMontyTagger j)
    {
        String prev = null,current = null,next = null;
        int patternCount = 0;
        boolean firstTrial = true;
        String outputString = j.Tag(inputString);
       
        Scanner sc = new Scanner(outputString);

        if (sc.hasNext())
            prev = sc.next();
        if (sc.hasNext())
            current = sc.next();
        while (sc.hasNext())
        {


            if (firstTrial)
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
            if (prev.matches(".*/JJ"))
            {
                if (current.matches(".*/NN")||current.matches(".*/NNS"))
                {
                    patternCount++;
                }
                else if (current.matches(".*/JJ") && !(current.matches(".*/NN")||current.matches(".*/NNS")))
                    patternCount++;
            }
            else if (prev.matches(".*/RB") || prev.matches(".*/RBR") || prev.matches(".*/RBS"))
            {
                if (current.matches(".*/JJ") && !(current.matches(".*/NN")||current.matches(".*/NNS")))
                    patternCount++;
                else if (current.matches(".*/VB") || current.matches(".*/VBD") || current.matches(".*/VBG") ||current.matches(".*/VBN"))
                    patternCount++;
            }
            if (prev.matches(".*/NN")||prev.matches(".*/NNS"))
            {
                if (current.matches(".*/JJ") && !(current.matches(".*/NN")||current.matches(".*/NNS")))
                    patternCount++;
            }



        }
        
        return patternCount;
    }

}//end class

