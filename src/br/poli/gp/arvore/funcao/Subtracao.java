package br.poli.gp.arvore.funcao;

import java.util.HashMap;

import br.poli.gp.arvore.Funcao;

public class Subtracao extends Funcao{

	private static final long serialVersionUID = -2578207134051816462L;

	public Subtracao() {
		super("-");
	}

	@Override
	public double calcularExpressao(HashMap<String, Double> hm) {
		return esquerda.calcularExpressao(hm) - direita.calcularExpressao(hm);
	}
	
	public String toString(){
		return "(" + esquerda.toString() + "-" + direita.toString() + ")";
	}
}
