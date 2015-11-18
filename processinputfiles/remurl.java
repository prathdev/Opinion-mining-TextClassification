import java.io.*;
import java.util.*;

class remurl
{
public static void main(String v[]) throws IOException
{
String redirect = "> ";
int i;
for(i=0;i<v.length;i++)
{
String command = new String();
command = "sed /http/d ";
command = command + v[i]+" > "+v[i]+".tmp";
System.out.println(command);
Process p=Runtime.getRuntime().exec(command); 

}
}
}
