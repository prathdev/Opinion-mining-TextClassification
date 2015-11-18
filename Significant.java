package Basic;

import java.io.*;
import java.util.Hashtable;
import Basic.hashing.*;
import montytagger.JMontyTagger;
import java.util.*;

class infileSignificant
{
    FileReader f;
    int ch;
    boolean delimit;
    boolean sentenceEnd;
	int [] sentenceCount;
    //int sentenceCount[size];

    StringBuffer str=new StringBuffer();

    infileSignificant(int size)
    {
        sentenceCount=new int[size];
    }

    void f_open(String fname) throws IOException
    {
        f=new FileReader(fname);
    }

	

    boolean endOfSent(int ch)
    {
        if (ch == '.' || ch == '!' || ch == ';' || ch == '?')
		return true;
        else
            return false;
    }


    String fread(int i) throws IOException
    {
		sentenceEnd = false;
        str.delete(0,str.length());
        //if the character is a delimiter like . or ! and is not preceded by a delimiter, increment sentence count

        while (((ch=f.read())!=-1) && (!endOfSent(ch)) )
        {
            str.append((char)ch);
			sentenceEnd = true;
        }
        if (ch==-1) return null;
        
	//Will indicate the end of sentence has been encountered
        
        return str.toString().toLowerCase();
    }

    String getS()
    {
        return str.toString();
    }

    void f_close() throws IOException
    {
        f.close();
    }

    String getCh()
    {
        return Character.toString(' ');
    }
	
	boolean delimiter(char ch)
	{
		if((ch>='a' && ch<='z') || (ch>='A' && ch<='Z') || (ch>='0' && ch<='9'))
		{
			return false;
		}
		else
		{
			return true;
		}
	} 

	
	int getWordCount(String s)
	{
		
		int count = 0,i = 0;
		boolean prevWasDelimit = true;
		int stringLength = s.length();
		while(i < stringLength-1)
		{
			if(delimiter(s.charAt(i)))
			{
				if(prevWasDelimit == false)
				{
					count++;		
				}
				prevWasDelimit = true;
			}
			else
			{
				prevWasDelimit = false;
			}
			i++;

		}
		return (count+1);
      }

}


public class Significant
{
	String s=new String();
	int wordCount;
	String outputString = new String();
	 
	
	float ratio,count;
	int i;
	boolean breakWhile;
	String sentence;
	
	public float find(String filename,JMontyTagger j,String choice  ) throws IOException
	{
		hashing h1 =  new hashing("positive.txt");
		hashing h2 =  new hashing("negative.txt");
		count = 0;
		float adjectiveCount=0;
		infileSignificant input = new infileSignificant(1);
		input.f_open(filename);
		while(true)
		{
                //if the word read is in positive.txt or negative.txt icrement adjective count
                //threshold for each file is calc as no of adj/ no of sent ??
                //threshold to be set to different values by the program and prog to be run on the files
                s=input.fread(1);
			
			//wordCount = getWordCountS(s);
			//System.out.println("Count = "+wordCount);

				//fread returns a sentence
				if(s!=null)
				{
					wordCount = input.getWordCount(s);
					outputString = j.Tag(s);
					Scanner sc = new Scanner(outputString);
					//iterate over the tagged sentence word by word
					while(sc.hasNext())
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
					ratio = (float)count/(float)wordCount;
					if(adjectiveCount <= ratio)
					{
						adjectiveCount = ratio; //the count of adjectives in this sentence
						sentence = s;
						
					}
					
					//reset values for the next sentence
					count = 0;
					
				}
				else
				{
                    input.f_close();
					//breakWhile = true;
                    break;
                    //System.exit(0);
                }
        }     
            
		if(choice.compareTo("returnRatio") == 0)
		{
			return adjectiveCount;
		}
		else
		{
			return 0.0f;
		}
		
	}
}

class SignificantMain
{
	public static void main(String v[]) throws IOException
	{
			   
        //infile input=new infile(v.length);
	    String[] sentence = new String[v.length];
        int i;
        float[] adjectiveCount=new float[v.length];
        float finalThreshold = 0.0f,threshold,from,to,ratio;
        float adjectiveSentenceRatio[] = new float[v.length];
        int matchCount = 0, count =0;
        float accuracy,maxAccuracy=0.0f,minAdjectiveCount=10000,maxAdjectiveCount=0,avgAdjectiveCount;
		FileWriter data = new FileWriter("graphs/Significant.txt");
	    FileWriter plot = new FileWriter("graphs/Significant.gnu");
		FileWriter all = new FileWriter("graphs/SignificantRec.txt",true);
       JMontyTagger j = new JMontyTagger();
		
		String inputDirectory = new File(v[0]).getParent();
		
		termHash opinion = new termHash("input50/opinion.txt");
		termHash factual = new termHash("input50/Factual.txt");
		
		
        for (i=0; i<v.length; i++)
        {
            //input.f_open(v[i]);
            //output.f_open("output.txt");
            //boolean stopWord;
			String filename= new File(v[i]).getName() ;
			//System.out.println(filename);
		adjectiveCount[i] = 0;
		//CHANGE
		if((new File(v[i]).getName()).compareTo("opinion.txt")==0 || (new File(v[i]).getName()).compareTo("Factual.txt")==0)
		{
			adjectiveCount[i]=-1;
		}
		//CHANGE
		else
		{
			Significant obj = new Significant();
			//obj.breakWhile = false;
			
				adjectiveCount[i] = obj.find(v[i],j,"returnRatio");
				
				//if(obj.breakWhile == true)
					//break;
			
			//obj.breakWhile = false;
		}
		if (adjectiveCount[i]<minAdjectiveCount)
            {
                minAdjectiveCount = adjectiveCount[i];
            }
            if (adjectiveCount[i]>maxAdjectiveCount)
            {
                maxAdjectiveCount = adjectiveCount[i];
            }
            //System.out.println("\nFile :"+v[i]+" Adjective count = " + adjectiveCount[i]+" String:"+sentence[i]);
		//END ELSE	
        }//end for
	

         avgAdjectiveCount = (minAdjectiveCount + maxAdjectiveCount)/2;
        //System.out.println("Average threshold: "+avgAdjectiveCount);
        from = 0.0f;
        to = avgAdjectiveCount + 0.5f;
        for (threshold = from; threshold<= to; threshold+=0.1)
        {
        	matchCount=0;
            for (i=0; i<v.length; i++)
            {
				if(adjectiveCount[i]!=-1)
				{
                if ((adjectiveCount[i] < threshold && factual.check(new File(v[i]).getName())) || (adjectiveCount[i] >= threshold && opinion.check(new File(v[i]).getName())))
				//if ((adjectiveCount[i] < threshold && v[i].matches("factual.*")) || (adjectiveCount[i] >= threshold && v[i].matches("opinionated.*")))
				  matchCount++;
				}
            }
            //CHANGED
            if(v[0].indexOf("input50")>=0)
            accuracy = (float)matchCount/(v.length-2);
			else
			accuracy = (float)matchCount/(v.length);

            if (maxAccuracy <= accuracy)
            {
			if(maxAccuracy == accuracy)
			{
				float diff1 = avgAdjectiveCount - threshold;
				float diff2 = avgAdjectiveCount - finalThreshold;
				if(diff1 < 0.0) { diff1 = -diff1;}
				if(diff2 < 0.0) { diff2 = -diff2;}
				if(diff1<diff2)
				{
					finalThreshold = threshold;
					maxAccuracy = accuracy;
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

	all.write(v.length+" "+maxAccuracy+"\n");
	all.close();
	plot.write("set terminal jpeg\n");
	plot.write("set output \"graphs/Significant.jpg\"\n");
	plot.write("set title \"Significant Tagged Method\"\n");
	plot.write("set xlabel  \"threshold\"\n");
	plot.write("set ylabel  \"accuracy\"\n");
	
	plot.write("plot \'graphs/Significant.txt\' using 1:2 with lines title \'Significant Tagged Method\' lw 3\n");

	plot.close();
	data.close();

	
    }//end main
    
}//end class




			