package br.poli.gp.arvore.funcao;

import java.util.HashMap;

import br.poli.gp.arvore.Funcao;

public class Tangente extends Funcao{

	private static final long serialVersionUID = -3286648095169057790L;

	public Tangente() {
		super("Tan");
		apenasNoEsquerdo = true;
	}

	@Override
	public double calcularExpressao(HashMap<String, Double> hm) {		
		return Math.tan(esquerda.calcularExpressao(hm));
	}
	
	public String toString(){
		return "(" + valor+"[" + esquerda.toString() + "]" + ")";
	}

}
