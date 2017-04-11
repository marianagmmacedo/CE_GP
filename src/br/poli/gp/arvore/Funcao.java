package br.poli.gp.arvore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Funcao {

	public String valor;
	public int numeroMaximoTermo;
	public List<Funcao> nos;
	
	public static HashMap<String, Integer> index;

	public Funcao(String valor){
		this.nos = new ArrayList<Funcao>();
		this.valor = valor;
	}

	public abstract double calcularExpressao();
	
	public String toString(){
		String temp = valor;
		for(Funcao e : nos){
			temp += "\n[" + e.toString() + "]";
		}
		return temp;
	}
}