package br.poli.gp.arvore.funcao;

import java.util.HashMap;

import br.poli.gp.arvore.Funcao;

public class Cosseno extends Funcao{

	private static final long serialVersionUID = -4681976996996751347L;

	public Cosseno() {
		super("Cos");
		apenasNoEsquerdo = true;
	}

	@Override
	public double calcularExpressao(HashMap<String, Double> hm) {		
		return Math.cos(esquerda.calcularExpressao(hm));
	}
	
	public String toString(){
		return "(" + valor+"[" + esquerda.toString() + "]" + ")";
	}

}
