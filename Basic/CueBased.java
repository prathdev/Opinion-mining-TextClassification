package Basic;

import java.io.*;
import java.util.*;
import Basic.hashing.hashing;
import montytagger.JMontyTagger;
import Basic.input.infile;

class infileCueBased extends infile
{


    infileCueBased(int size)
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



class CueBased
{

    //take input from command line as a directory containing the reviews and articles

    public static void main(String v[]) throws IOException
    {
    	   JMontyTagger j = new JMontyTagger();
    	   
        infileCueBased input=new infileCueBased(v.length);

	//infile input=new infile(v.length);
  	   FileWriter data = new FileWriter("graphs/CueBased.txt");
	   FileWriter plot = new FileWriter("graphs/CueBased.gnu");
	   FileWriter record=new FileWriter("graphs/CueBasedRec.txt",true);

 
        String s=new String();
        String cue;
        Boolean delimit = false, opinion = false;
        int i;
        float[] cueCount=new float[v.length];
        float finalThreshold = 0.0f,threshold=0,from,to;
        float cueSentenceRatio[] = new float[v.length];
        int matchCount = 0;
        float accuracy=0.0f,maxAccuracy=0.0f,minCueSentenceRatio=10000,maxCueSentenceRatio=0,avgCueSentenceRatio=0.0f;
      ;
        //BufferedReader reader = new BufferedReader(new InputStreamReader("data.txt"));

		hashing factual = new hashing("input50/Factual.txt");
        hashing opinion1 = new hashing("input50/Opinion.txt");

      
        for (i=0; i<v.length; i++)
        {
            input.f_open(v[i]);
			
			cueCount[i] = 0;
			if((new File(v[i]).getName()).compareTo("opinion.txt")==0 || (new File(v[i]).getName()).compareTo("Factual.txt")==0)
			{
				cueCount[i]=-1;
			}
			
			else
			{
            //storing the input text as is into a string
            String inputString = input.f_storeToString(v[i]);
			inputString = inputString.toLowerCase();
      	  FileReader r = new FileReader("data.txt");
        BufferedReader reader = new BufferedReader(r);
		
            while (true)	
            {
          
             //checking if every one of the cues are substrings of the input string
             //incrementing the count for every substring match
             //ignoring multiple occurences of the same cue.
             if((cue = reader.readLine())!= null)
             {
            cue = cue.toLowerCase();
             if(inputString.contains(cue))
             {
        	 opinion = true;
             cueCount[i]++;
             }
             }
             else
             break;
             
            }
            reader.close();
		 //System.out.println("File :"+v[i]+" cueCount " + cueCount[i]);

           

		}

        }//end for

            for (i=0; i<v.length; i++)
            {
               /* if ((!(opinion) && v[i].matches("factual.*")) || (opinion && v[i].matches("opinionated.*")))*/

			String path = v[i];
		   String filename = new File(path).getName();
		  
               if ((!(opinion) && factual.check(filename)) || (opinion && opinion1.check(filename)))


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
				float diff1=avgCueSentenceRatio-threshold;
				float diff2=avgCueSentenceRatio-finalThreshold;
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

            //System.out.println(" Accuracy : "+accuracy);
			data.write(threshold+" "+accuracy+"\n");
        
        //System.out.println("Final threshold : "+finalThreshold +"\n Max Accuracy : "+maxAccuracy);

plot.write("set terminal jpeg\n");
	plot.write("set output \"graphs/CueBased.jpg\"\n");
	plot.write("set title \"Cue Based approach\"\n");
	plot.write("set xlabel \"Threshold\"\n");
	plot.write("set ylabel \"Accuracy\"\n");
	plot.write("plot  \'graphs/CueBased.txt\' using 1:2 with lines title \'Cue Based approach/\' lw 3\n");


record.write(v.length+" "+maxAccuracy+"\n");
record.close();
plot.close();
data.close();    
     
    }
}



