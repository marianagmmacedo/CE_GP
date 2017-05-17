package br.poli.output;

import java.util.ArrayList;
import java.util.List;

public class Output {

	public static List<Output> outputList = new ArrayList<Output>();
	public String nomeBase;
	//{0} Media / Desvio
	//{1} Funcao
	//{2} Erros
	
	public Output(String base){
		nomeBase = base;
		texto[1] = texto[0] = texto[2] = "";
	}
	
	public String[] texto = new String[3];
	
	public String toString(){
		String s = "";
		
		s += texto[0] + "\n\n\n";
		s +="Funcoes: \n\n" + texto[1];
		s += "Erros: \n\n"+ texto[2];
		s += "\n\n";
		return s;
	}
	
	public static Output getOutputByList(String base){
		for(Output o : outputList){
			if (o.nomeBase.equals(base)){
				return o;
			}
		}
		return null;
	}
}
