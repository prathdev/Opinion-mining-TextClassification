package Basic;

import java.io.*;
import java.util.Hashtable;
import Basic.hashing.*;
import Basic.input.infile;

class infileThreshold extends infile
{


    infileThreshold(int size)
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



}

class outfile
{
    BufferedWriter fw;
    void f_open(String fname) throws IOException
    {
        fw = new BufferedWriter(new FileWriter(fname));
    }

    void writer(String s) throws IOException
    {
        fw.write(s);
    }

    void f_close() throws IOException
    {
        fw.close();
    }
}




class Threshold
{

    //take input from command line as a directory containing the reviews and articles

    public static void main(String v[]) throws IOException
    {
    	      	   
        infileThreshold input=new infileThreshold(v.length);
        //outfile output = new outfile();
        String s=new String();
        int i;
        int[] adjectiveCount=new int[v.length];
        float finalThreshold = 0.0f,threshold,from,to;
        float adjectiveSentenceRatio[] = new float[v.length];
        int matchCount = 0;
        float accuracy,maxAccuracy=0.0f,minAdjSentenceRatio=10000,maxAdjSentenceRatio=0,avgAdjSentenceRatio;
	     FileWriter data = new FileWriter("graphs/Threshold.txt");
        	FileWriter plot = new FileWriter("graphs/Threshold.gnu");
		FileWriter record=new FileWriter("graphs/ThresholdRec.txt",true);
        hashing h1 =  new hashing("positive.txt");
        hashing h2 =  new hashing("negative.txt");

	hashing factual = new hashing("input50/Factual.txt");
        hashing opinion = new hashing("input50/Opinion.txt");
        for (i=0; i<v.length; i++)
        {
            input.f_open(v[i]);
            //output.f_open("output.txt");
            //boolean stopWord;

            while (true)
            {
                //if the word read is in positive.txt or negative.txt icrement adjective count
                //threshold for each file is calc as no of adj/ no of sent ??
                //threshold to be set to different values by the program and prog to be run on the files
                s=input.fread(i);
                if (s==null) {

                    input.f_close();
                    break;
                    //System.exit(0);
                }
                if (s.length()!=0)
                {
                    if (h1.check(s)|| h2.check(s))
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
            //System.out.println("\nAdjective count = " + adjectiveCount[i] + "\tSentence Count = " + input.sentenceCount[i]);

            //System.out.println("File :"+v[i]+" Adjective sentence ratio " + adjectiveSentenceRatio[i]);



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
            accuracy = (float)matchCount/v.length;
            if (maxAccuracy < accuracy)
            {
                maxAccuracy = accuracy;
                finalThreshold = threshold;
            }
            //System.out.println("Threshold : "+threshold+"    Accuracy : "+accuracy);
            data.write(threshold+" "+accuracy+"\n");
        }
        //System.out.println("Final threshold : "+finalThreshold +"\n Max Accuracy : "+maxAccuracy);
      plot.write("set terminal jpeg\n");
	plot.write("set output \"graphs/Threshold.jpg\"\n");
	plot.write("set title \"Document based Voting approach\"\n");
	plot.write("set xlabel \"Threshold\"\n");
	plot.write("set ylabel \"Accuracy\"\n");
	plot.write("plot  \'graphs/Threshold.txt\' using 1:2 with lines title \'Document based Voting approach\' lw 3\n");
	record.write(v.length+" "+maxAccuracy+"\n");
	record.close();
	plot.close();
	data.close();

    }
}



