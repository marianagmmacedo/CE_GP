package br.poli.gp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

public class Common {
	public static final Random RANDOM = new Random();
	

		public static Object DeepCopy(Object original)
	    {
	        ObjectOutputStream oos = null;
	        ObjectInputStream ois = null;

	        Object clone = null;
	        
	        try
	        {
	            // deep copy
	            ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
	            oos = new ObjectOutputStream(bos); 
	            oos.writeObject(original);   
	            oos.flush();               
	            ByteArrayInputStream bin = 
				        new ByteArrayInputStream(bos.toByteArray()); 
	            ois = new ObjectInputStream(bin);                  
	            
	            // retorna novo objeto
	            clone = ois.readObject();
	        }
	        catch(Exception e)
	        {
	            System.out.println("Exception in main = " +  e);
	        }
	        finally
	        {        
	        	try{
	            oos.close();
	            ois.close();
	        	} catch(Exception e) {}
	        }
	        
	        return clone;
	    }
		
	
}
