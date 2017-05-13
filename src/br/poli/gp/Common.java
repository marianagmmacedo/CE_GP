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
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

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
		
		/*
		 * Divide todos pelo maior valor. (não é uma boa forma para normalizar)
		 */
		public static void Normalizar1(HashMap<Integer, Double> serieTemporal){
			Double maximoValor = serieTemporal.values().stream().max((x,y) -> Double.compare(x, y)).get();
			serieTemporal.entrySet().forEach(x -> x.setValue(x.getValue()/maximoValor));
		}
		
		/*
		 * Mï¿½todo de normalizaï¿½ï¿½o do professor de Mariana
		 * (Dado - media)/(desvio padrao)
		 */
		public static void Normalizar2(HashMap<Integer, Double> serieTemporal){
			Double media = CalcularMedia(serieTemporal);
			Double desvioPadrao = CalcularDesvioPadrao(serieTemporal);
			serieTemporal.entrySet().forEach(x -> x.setValue((x.getValue() - media)/desvioPadrao));
//			for (Map.Entry<Integer, Double> set : serieTemporal.entrySet()){
//				System.out.println(set.getKey());
//				System.out.println(set.getValue());
//			}
		}
		
		public static void NormalizarLN(HashMap<Integer, Double> serieTemporal){
			serieTemporal.entrySet().forEach(x -> x.setValue( (Math.log(x.getValue()) )) );
//			for (Map.Entry<Integer, Double> set : serieTemporal.entrySet()){
//				System.out.println(set.getKey());
//				System.out.println(set.getValue());
//			}
			//return r;
		}
		
		public static double CalcularDesvioPadrao(double[] respostas){
			DescriptiveStatistics estatistica = new DescriptiveStatistics();
			
			for (Double valor : respostas){
				estatistica.addValue(valor);
			}
			
			return estatistica.getStandardDeviation();
		}
		
		public static double CalcularDesvioPadrao(HashMap<Integer, Double> serieTemporal){
			DescriptiveStatistics estatistica = new DescriptiveStatistics();
			
			for (Map.Entry<Integer, Double> set : serieTemporal.entrySet()){
				estatistica.addValue(set.getValue());
			}
			
			return estatistica.getStandardDeviation();
		}

		public static double CalcularMedia(double[] respostas){
			DescriptiveStatistics estatistica = new DescriptiveStatistics();
			
			for (Double valor : respostas) {
				estatistica.addValue(valor);
			}
			
			return estatistica.getMean();
		}
		public static double CalcularMedia(HashMap<Integer, Double> serieTemporal){
			DescriptiveStatistics estatistica = new DescriptiveStatistics();
			
			for (Map.Entry<Integer, Double> set : serieTemporal.entrySet()){
				estatistica.addValue(set.getValue());
			}
			
			return estatistica.getMean();
		}
		
	
}
