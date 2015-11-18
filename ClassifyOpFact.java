package Basic;

import java.io.*;
import java.util.*;
import Basic.hashing.termHash;
import montytagger.JMontyTagger;
import java.lang.*;
import Basic.FirstSentence;
import Basic.lastSentence;
import Basic.Significant;
import Basic.input.infile;

class infileClassify extends infile
{
   
	boolean sentenceEnd;
	int sentCount;

    infileClassify(int size)
    {
        sentenceCount=new int[size];
    }
	
	infileClassify()
	{
		sentCount=0;
	}

	String getSentence(int i) throws IOException
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

	
    public String fread(int i) throws IOException
    {
		sentenceEnd=false;
        str.delete(0,str.length());
        //if the character is a delimiter like . or ! and is not preceded by a delimiter, increment sentence count

        while (((ch=f.read())!=-1) && (!delimiter(ch)) )
        {
            str.append((char)ch);
            delimit = false;
        }
        if (ch==-1) return null;
        if (delimit == false && (ch == '.' || ch == '!' || ch == ';' || ch == '?'))
		{
         
			sentenceEnd=true;
			sentCount++;
		}

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

	String f_getFirstSentence(String filename) throws IOException
	{
	str.delete(0,str.length());
	
	  while (((ch=f.read())!=-1) && !(ch == '.' || ch == '!' || ch == ';' || ch == '?')) 
        {
            str.append((char)ch);
        }
        return str.toString();
	
	}
	

	boolean endOfSent(int ch)
    {
        if (ch == '.' || ch == '!' || ch == ';' || ch == '?')
		return true;
        else
            return false;
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





} //infile end






class ClassifyOpFact
{

public static void main(String[] v) throws IOException
{


ClassifyOpFact obj=new ClassifyOpFact();
int i=0;
float adjectiveCount=0.0f;

//float maxAccuracy=0.0f,minAdjectiveCount=10000.0f,maxAdjectiveCount=0.0f,avgAdjectiveCount=0.0f;
JMontyTagger j = new JMontyTagger();
FileWriter result=new FileWriter("ClassifyOpFactResult.txt");
infileClassify input=new infileClassify();
String cue=null;

input.f_open(v[0]);
String inputString = input.f_storeToString(v[0]);
if(inputString.length()==0)
{
System.out.println("No text in file, Input string");
System.exit(0);
input.f_close();
}
inputString=inputString.toLowerCase();

      	  FileReader r = new FileReader("data.txt");
        BufferedReader reader = new BufferedReader(r);
		
            while (true)	
            {
          
             //checking if every one of the cues are substrings of the input string
             //incrementing the count for every substring match
             //ignoring multiple occurences of the same cue.
             if((cue = reader.readLine())!= null)
             {
            cue=cue.toLowerCase();
             if(inputString.contains(cue))
             {
        	 adjectiveCount=0.3f;
             }
             }
             else
             break;
             
            }
			input.f_close();
            reader.close();
			if(adjectiveCount > 0.2)
			{
			result.write("Opinionated");
			result.close();
			System.exit(0);
			}
			


/*FirstSentenceV o1=new FirstSentenceV();
lastSentenceV o2=new lastSentenceV();
SignificantV o3=new SignificantV();*/
FirstSentence o4=new FirstSentence();
lastSentence o5=new lastSentence();
Significant o6=new Significant();


adjectiveCount = 0;
			
adjectiveCount+=o4.find(v[0],j);

//System.out.println(adjectiveCount);
adjectiveCount+=o5.find(v[0],j);
//System.out.println(adjectiveCount);
adjectiveCount+=o6.find(v[0],j,"returnRatio");
//System.out.println(adjectiveCount);

            //System.out.println("\nFile :"+v[i]+" Adjective count = " + adjectiveCount[i]); //+" String:"+inputString);


//System.out.println(minAdjectiveCount + maxAdjectiveCount);


if(adjectiveCount >= 0.2)
result.write("Opinionated");

else
result.write("Factual");

result.close();


//data.write(obj.threshold+" "+obj.accuracy+"\n");


}

}


