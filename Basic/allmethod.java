package Basic;

import java.io.*;
import java.util.*;
import Basic.hashing.termHash;
import Basic.hashing.hashing;
import montytagger.JMontyTagger;
import Basic.Significant;
import Basic.FirstSentence;
import Basic.lastSentence;


class allmethod
{
static float threshold=0.0f,accuracy=0.0f;
public static void main(String[] v) throws IOException
{


allmethod obj=new allmethod();
int i=0;
float[] adjectiveCount=new float[v.length];
float finalThreshold = 0.0f,from=0.0f,to=0.0f;
float adjectiveSentenceRatio[] = new float[v.length];
int matchCount = 0;
float maxAccuracy=0.0f,minAdjectiveCount=10000.0f,maxAdjectiveCount=0.0f,avgAdjectiveCount=0.0f;
JMontyTagger j = new JMontyTagger();

/*FirstSentenceV o1=new FirstSentenceV();
lastSentenceV o2=new lastSentenceV();
SignificantV o3=new SignificantV();*/
FirstSentence o4=new FirstSentence();
lastSentence o5=new lastSentence();
Significant o6=new Significant();

FileWriter data=new FileWriter("graphs/allmethod.txt");


maxAccuracy=0.0f;
minAdjectiveCount=10000;
maxAdjectiveCount=0;
finalThreshold=0.0f;

System.out.println("\n\n-------------------------------------------------------------------------------");
System.out.println("\nTagged approach");
System.out.println("-------------------------------------------------------------------------------\n");

for(i=0;i<v.length;i++)
{
adjectiveCount[i] = 0;
			if((new File(v[i]).getName()).compareTo("opinion.txt")==0 || (new File(v[i]).getName()).compareTo("Factual.txt")==0)
			{
				adjectiveCount[i]=-1;
			}
			else
			{
adjectiveCount[i]=0;
adjectiveCount[i]+=o4.find(v[i],j);
//System.out.println(adjectiveCount[i]);
adjectiveCount[i]+=o5.find(v[i],j);
//System.out.println(adjectiveCount[i]);

adjectiveCount[i]+=o6.find(v[i],j,"returnRatio");
//System.out.println(adjectiveCount[i]);

if (adjectiveCount[i]<minAdjectiveCount)
            {
                minAdjectiveCount = adjectiveCount[i];
            }
            if (adjectiveCount[i]>maxAdjectiveCount)
            {
                maxAdjectiveCount = adjectiveCount[i];
            }
            //System.out.println("\nFile :"+v[i]+" Adjective count = " + adjectiveCount[i]); //+" String:"+inputString);


//System.out.println(minAdjectiveCount + maxAdjectiveCount);
}
}
obj.findAccuracy(v,adjectiveCount,minAdjectiveCount,maxAdjectiveCount,avgAdjectiveCount,maxAccuracy,finalThreshold,from,to,data);

//data.write(obj.threshold+" "+obj.accuracy+"\n");

data.close();
}

public void findAccuracy(String[] v,float[] adjectiveCount,float minAdjectiveCount,float maxAdjectiveCount,float avgAdjectiveCount,float maxAccuracy,float finalThreshold,float from,float to,FileWriter data) throws IOException
{
		allmethod o=new allmethod();
		//FileWriter data=new FileWriter("graphs/allmethod.txt");
		FileWriter plot=new FileWriter("graphs/allmethod.gnu");
		FileWriter all=new FileWriter("graphs/allmethodRec.txt",true);
		int matchCount = 0,i=0;
		//System.out.println(minAdjectiveCount + maxAdjectiveCount);
		avgAdjectiveCount = (minAdjectiveCount + maxAdjectiveCount)/2;
        //System.out.println("Average adjective count: "+avgAdjectiveCount);
        from = 0.0f;
        to = avgAdjectiveCount + 0.5f;
		String inputDirectory = new File(v[0]).getParent();
		
		termHash opinion = new termHash("input50/opinion.txt");
		termHash factual = new termHash("input50/factual.txt");
        for (o.threshold = from; o.threshold<= to; o.threshold+=0.1)
        {
        	matchCount=0;
            for (i=0; i<v.length; i++)
            {
                if ((adjectiveCount[i] < threshold && factual.check(new File(v[i]).getName())) || (adjectiveCount[i] >= o.threshold && opinion.check(new File(v[i]).getName())))
                    matchCount++;
            }
           if(v[0].indexOf("input50")>=0)
            o.accuracy = (float)matchCount/(v.length-2);
			else
			o.accuracy = (float)matchCount/(v.length);
            if (maxAccuracy <= o.accuracy)
            {
			if(maxAccuracy == o.accuracy)
			{
				float diff1 = avgAdjectiveCount - o.threshold;
				float diff2 = avgAdjectiveCount - finalThreshold;
				if(diff1 < 0.0) { diff1 = -diff1;}
				if(diff2 < 0.0) { diff2 = -diff2;}
				if(diff1<diff2)
				{
					finalThreshold = o.threshold;
					maxAccuracy = o.accuracy;
				}
			} 
			else
			{
                		maxAccuracy = o.accuracy;
                		finalThreshold = o.threshold;
			}
		}
            //System.out.println("Threshold : "+o.threshold+"    Accuracy : "+o.accuracy);
data.write(o.threshold+" "+o.accuracy+"\n");
        }
        //System.out.println("Final threshold : "+finalThreshold +"\n Max Accuracy : "+maxAccuracy);
		all.write(v.length + " " + maxAccuracy + "\n"); 
		all.close();

plot.write("set terminal jpeg\n");
plot.write("set output \"graphs/allmethod.jpg\"\n");
plot.write("set title \"All tagged methods\"\n");
plot.write("set xlabel  \"threshold\"\n");
plot.write("set ylabel  \"accuracy\"\n");

plot.write("plot \'graphs/allmethod.txt\' using 1:2 with lines title \'All tagged methods\' lw 3\n");

plot.close();
//data.close();

}
}


