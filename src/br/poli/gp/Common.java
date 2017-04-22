package br.poli.gp;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

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
		
		public static HashMap<Integer, Double> lerBase(String nomeBase){
			
			LinkedHashMap<Integer, Double> base = new LinkedHashMap<Integer, Double>();
			
			try{
				File directory = new File("./");
				String macMari = "/src/Bases/";
//				File directory = new File(".\\");
//				String macCarlos = "\\src\\Bases\\";
				BufferedReader in = new BufferedReader(new FileReader(directory.getAbsolutePath() + macMari + nomeBase + ".txt"));
				String line;
				
				Integer i = 0;
				
				while((line = in.readLine())!= null){
					base.put(i++, Double.parseDouble(line));
				}
				
				in.close();
				
			} catch (Exception e){
				System.out.println(e);
			}			
			return base;
		}
		
		//Substituir por nome bonito
		public static void Normalizar(HashMap<Integer, Double> serieTemporal){
			Double maximoValor = serieTemporal.values().stream().max((x,y) -> Double.compare(x, y)).get();
			serieTemporal.entrySet().forEach(x -> x.setValue(x.getValue()/maximoValor));
		}
		
		public static void Ordenar(HashMap<Double, Double> serieTemporal){
			
		}
		
	
}
