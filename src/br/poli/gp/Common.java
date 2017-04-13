package br.poli.gp;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.util.HashMap;
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
		
		public static HashMap<Double, Double> lerBase(String nomeBase){
			
			HashMap<Double, Double> base = new HashMap<Double, Double>();
			
			try{
				File directory = new File(".\\");
				BufferedReader in = new BufferedReader(new FileReader(directory.getAbsolutePath() + "\\src\\Bases\\" + nomeBase + ".txt"));
				String line;
				
				double i = 0;
				
				while((line = in.readLine())!= null){
					base.put(i++, Double.parseDouble(line));
				}
				
				in.close();
				
			} catch (Exception e){
				System.out.println(e);
			}
			
			return base;
		}
		
	
}
