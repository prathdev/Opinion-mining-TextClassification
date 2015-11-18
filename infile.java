package Basic.input;

import java.io.*;
import java.util.*;

abstract public class infile
{
	public FileReader f;
    public int ch;
    public boolean delimit;
    public int sentenceCount[];
	public StringBuffer str=new StringBuffer();
	
	
	
	public void f_open(String fname) throws IOException
    {
        f=new FileReader(fname);
    }
	public boolean delimiter(int ch)
    {
        if ((ch>=65 && ch<=90) || (ch>=97 && ch<=122) || (ch>='0' && ch<='9') || ch==7)
            return false;
        else
            return true;
    }

	public abstract String fread(int i) throws IOException;
	
	
	
	
	public void f_close() throws IOException
    {
        f.close();
    }
	
	public String getCh()
    {
        return Character.toString(' ');
    }
	 public String getS()
    {
        return str.toString();
    }
}