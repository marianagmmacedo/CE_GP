package br.poli.gp.arvore.funcao;

import java.util.HashMap;

import br.poli.gp.arvore.Funcao;

public class Seno extends Funcao{

	private static final long serialVersionUID = -4963733915003354366L;

	public Seno() {
		super("Sin");
		apenasNoEsquerdo = true;
	}

	@Override
	public double calcularExpressao(HashMap<String, Double> hm) {		
		return Math.sin(esquerda.calcularExpressao(hm));
	}
	
	public String toString(){
		return "(" + valor+"[" + esquerda.toString() + "]" + ")";
	}

}
