
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class KNN_client 
{
	public static void main(String args[])throws IOException
	{
		
		String domainName="localhost";      // SET THE IP ADDRESS AT WHICH THE SERVER IS RUNNING
        int receiverPort=6669   ;            // SET THE PORT NO AT WHICH SERVER IS LISTENING

		String delimeter,predict [], tokens[],features [];
		int len,k;
		ArrayList<String []> Data=new ArrayList<String []>();
		ArrayList<String> group=new ArrayList<String>();
		ArrayList<Double> euclidean_distance=new ArrayList<Double>();
		
		
		try
		{
			    InetAddress ip = InetAddress.getByName(domainName); 
			    Socket s = new Socket(ip,receiverPort);
			
			    DataInputStream dis = new DataInputStream(s.getInputStream()); 
			    DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			
                System.out.println("Connection Established ");

			    delimeter=dis.readUTF();
            
			    len=dis.readInt();

			    k=dis.readInt();
                
			
			    for(int i=0;i<len;i++)
			    {
				    tokens=dis.readUTF().split(delimeter);
				    Data.add(tokens);
			    }


                while(true)
                { 
                	group.clear();
                	euclidean_distance.clear();
                	
                    String str1=dis.readUTF();
                    System.out.println("Test Set received:-  "+str1);
                    
                    if(str1.equals("EXIT"))
                         break;
                    
                    predict=str1.split(delimeter);                   

			        for (String i[]:Data)
			        {

				        features=Arrays.copyOf(i,i.length-1);
				        group.add(i[i.length-1]);
                        double sum=0;
				        for(int j=0;j<features.length;j++)
				        {
				    	    sum=sum+Math.pow((Double.parseDouble(features[j])-Double.parseDouble(predict[j])),2);
				        }			
				        euclidean_distance.add(Math.sqrt(sum));	
			        }
  
            
            
                    for(int  i=0;i<len-1;i++)
                    {
                        for(int j=0;j<len-i-1;j++)
                        {
                            if( euclidean_distance.get(j) >  euclidean_distance.get(j+1)  )
                            {
                                double temp=euclidean_distance.get(j);
                                euclidean_distance.set(j,euclidean_distance.get(j+1));
                                euclidean_distance.set(j+1,temp);

                                String str=group.get(j);
                                group.set(j,group.get(j+1));
                                group.set(j+1,str);
                            }
                        }
                    }
           
			
			        for(int j=0;j<k;j++)
			        {
				        dos.writeUTF(group.get(j));
			        }
			    }
                System.out.println("Closing the connection");
                s.close();
                	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
}
