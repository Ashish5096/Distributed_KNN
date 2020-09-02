import java.io.*;
import java.util.*;

public class Shared 
{
	private int PFlag,nodes;
	private int count, pcount;
	private HashMap<String, Integer> majority ;
	private String predict;
	
	Shared()
	{
		PFlag=0;
		majority= new HashMap<String, Integer>();
	}
	
	void reset()
	{
		count=0;
		pcount=0;
		PFlag=0;
		predict="";
		majority.clear();
	}
	
	void SetNofNodes(int no)
	{
		nodes=no;
	}
	
	void setPredict(String str)
	{
		predict=str;
		PFlag=1;
	}
	
	synchronized String getPredict()
	{
		pcount++;

		if(pcount==nodes)
			PFlag=0;
		
		return predict;
	}
	
	synchronized int getPFlag()
	{
		return PFlag;
	}
	
	synchronized void print( HashMap<String, Integer> voteRef)
	{
		String key;
		int value;
		
		for(Map.Entry m :voteRef.entrySet())
		{  
			key=(String)m.getKey();
			value=(int)m.getValue();

			if(majority.containsKey(key))
            {    
               majority.replace(key,majority.get(key)+value);
            }
			else
            {  
                majority.put(key, value);
            }    
		}
		
		count++;
	}
	
	int getCount()
	{
		return count;
	}
	
	String getResult()
	{
		String result="";
		int max=-1,value;
		
		for(Map.Entry entry :majority.entrySet())
		{ 
			value=(int)entry.getValue();
			if(value>max)
			{
				max=value;
				result=(String)entry.getKey();
			}
		}
		
		return result;
	}

}
