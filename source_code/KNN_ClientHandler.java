import java.net.*;
import java.io.*;
import java.util.*;

public class KNN_ClientHandler extends Thread 
{
	private Socket s;
	private List<String> Data;
	private String predict;
	private String delimeter;
	private int len,k;
	private HashMap<String, Integer> votes = new HashMap<String, Integer>();
    Shared SRef;
	
	KNN_ClientHandler(Socket s2,List<String> Data,int len,String delimeter,int k,Shared shr)
	{
		s=s2;
		this.Data=Data;
		this.delimeter=delimeter;
		this.len=len;
        this.k=k;
        SRef=shr;
	}
	
	@Override
	public void run()
	{
		try
		{
			DataInputStream dis = new DataInputStream(s.getInputStream()); 
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			
			dos.writeUTF(delimeter);
			dos.flush();
			
			dos.writeInt(len);
			dos.flush();
			
			dos.writeInt(k);
			dos.flush();
			
			for (String i:Data)
			{
				dos.writeUTF(i);
				dos.flush();
			}

            
            while(true)
            {	
            	while(SRef.getPFlag()==0)
                {
                    int noperation=0;
                }
            	
            	predict=SRef.getPredict();

                dos.writeUTF(predict);
			    dos.flush();

                if(predict.equals("EXIT"))
                {
                    break;
			    }
                
                votes.clear();

			    for(int j=0;j<k;j++)
			    {
				    String temp=dis.readUTF();
				    if(votes.containsKey(temp))
	                {    
	                    votes.replace(temp, votes.get(temp)+1);
	                }
				    else
	                {    
	                    votes.put(temp, 1);
	                }
			    }
                
			    SRef.print(votes);               
            }

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

    
}
