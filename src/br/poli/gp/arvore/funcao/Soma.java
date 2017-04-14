package br.poli.gp.arvore.funcao;

import java.util.HashMap;

import br.poli.gp.arvore.Funcao;

public class Soma extends Funcao {

	private static final long serialVersionUID = -5494585914574328953L;

	public Soma() {
		super("+");
	}

	@Override
	public double calcularExpressao(HashMap<String, Double> hm) {
		return esquerda.calcularExpressao(hm) + direita.calcularExpressao(hm);
	}

	public String toString(){
		return "(" + esquerda.toString() + "+" + direita.toString() + ")";
	}
	
}
