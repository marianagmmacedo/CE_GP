package br.poli.gp.arvore.funcao;

import java.util.ArrayList;
import java.util.List;

public class ClassesFuncoes {	
	
	//Singleton Esquisito
	private static List<List<String>> operadores;
	private static ClassesFuncoes instance = new ClassesFuncoes();
	
	private ClassesFuncoes(){
		operadores = new ArrayList<List<String>>();
		List<String> operadoresA = new ArrayList<String>();
		List<String> operadoresB = new ArrayList<String>();
		List<String> operadoresC = new ArrayList<String>();
		
		//OperadoresA { + , - }
		operadoresA.add("+");
		operadoresA.add("-");

		//OperadoresB { * , / }
		operadoresB.add("*");
		operadoresB.add("/");
		
		//OperadoresC { Sqrt , Pow }
		operadoresC.add("Sqrt");
		operadoresC.add("Pow");
		
		operadores.add(operadoresA);
		operadores.add(operadoresB);
		operadores.add(operadoresC);
	}
	
	public static List<String> getClasse(String operador){
		for(List<String> op : operadores){
			if (op.contains(operador)){
				return op;
			}
		}
		return null;
	}
}
