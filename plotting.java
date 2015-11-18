import java.io.*;
import java.util.*;


class plotting
{
public static void main(String v[]) throws IOException
{
int i=0;
String[] files={"AdjectiveCnt","allmethod","CueBased","Threshold","FirstSentence","FirstSentenceV","lastSentence","lastSentenceV","OtherPOS","OtherPOSAll","OtherPOSFirst","OtherPOSLast","OtherPOSSig","Significant"};

for(i=0;i<14;i++)
{
String fname="graphs\\"+files[i]+"Rec.gnu";
String f=files[i]+"Rec.txt";
FileWriter plot=new FileWriter(fname);
String opfile=files[i]+"Rec.jpg";
plot.write("set terminal jpeg\n");
	plot.write("set output "+"\""+opfile+"\""+"\n");
	plot.write("set title " +"\""+files[i]+" Method for all input"+"\""+"\n");
	plot.write("set xlabel \"No. of Input Files\"\n");
	plot.write("set ylabel \"Maximum Accuracy\"\n");
	plot.write("plot \'"+f+"\' using 1:2 with lines title \'"+ files[i]+ " Method for all input/\' lw 3\n");
plot.close();

}
}
}
