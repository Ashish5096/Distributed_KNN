import java.net.*;
import java.util.*;
import java.io.*;


public class KNN_Server
{
	private ArrayList<String> Data;
	ServerSocket connSocket;
	Socket s;
    String delimeter;
	int nodes;
	int port;
	int neighbours;
    private KNN_ClientHandler list[];
    private Shared share;
    
	
	KNN_Server(int port,int nodes,int neighbours)
	{
		connSocket=null;
		s=null;
		delimeter=",";
		share=new Shared();
		this.port=port;
		this.nodes=nodes;
		this.neighbours=neighbours;
	}
	
	public void setDelimeter(String delimeter)
	{
		this.delimeter=delimeter;
	}
	
	public void readFile(String filename,int header)
	{
		String line="";
		Data=new ArrayList<String>();
		try
		{
			FileReader f1=new FileReader(filename);
			BufferedReader br=new BufferedReader(f1);
			
			while((line=br.readLine())!=null)
			{
				Data.add(line);
			}
			
			if(header==1)
	            Data.remove(0);

            randomShuffle();
		}
		catch(IOException e)
		{
			System.out.println("File Read error ");
		}
	}

    public void randomShuffle()
    {
        int len=Data.size();
        
        Random rgen = new Random();
        for (int i=0; i<len; i++)
        {
		    int pos = rgen.nextInt(len);
		    String temp = Data.get(i);
		    Data.set(i,Data.get(pos));
            Data.set(pos,temp);
		}
    }	
	public void createSocket()
	{
		try
		{
			connSocket=new ServerSocket(port);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void start_connection()
	{	
		int len=Data.size();
        int limit=(len/nodes)+1;
		int x=0,i=0,j=0;
		int k=(neighbours/nodes)+1;
		
		createSocket();
        list =new KNN_ClientHandler [nodes];
        share.SetNofNodes(nodes);

        for(x=0;x<nodes;x++)
		{
			try
			{
				    j=i+limit;
				    if(j>=len)
				    {
				    	j= len;
				    }
                    
				    System.out.println("Now Ready to accept a connection "+ (x+1) );
				    s=connSocket.accept();
				    list[x]= new KNN_ClientHandler(s,Data.subList(i,j),j-i,delimeter,k,share);
				    list[x].start();
				    System.out.println("Connection Accepted");
				    i=j;
            }
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

    }

    public String classify(String str)throws Exception
    { 
       String result;
       
       share.reset();
       
       share.setPredict(str);
       
       if(str.equals("EXIT"))
       {
    	   result="EXIT";
           connSocket.close();
    	   return result;
       }
       
       while(share.getCount()!=nodes)
       {
            System.out.print("");
       }
       
       result=share.getResult();

       return result;	
	}
	
	
	public static void main(String args[])throws Exception
	{
		
		String str,result,filename= "iris.csv";
        
        int port=6669 ,n_clients=2 ,n_neighbours=10;   /*  MODIFY HERE    */

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));	
        

        //KNN_sever(port,client nodes, no of nearest neighbours)
		KNN_Server ks=new KNN_Server(port,n_clients,n_neighbours);
		
		ks.setDelimeter(","); 
		
		//readFile(filePath,header) set header=1 if header present in dataset else 0
		ks.readFile(filename,1);	
       
        ks.start_connection();

        while(true)
        {
            System.out.println("Enter the test case, Enter 'EXIT' to end ");
            str=br.readLine();
            
            result=ks.classify(str);
            if(result.equals("EXIT"))
            {
            	System.out.println("Exiting ...");
                break;
            }
            
            System.out.println(str+" belongs to class "+result);
           
        }	
	}

}
