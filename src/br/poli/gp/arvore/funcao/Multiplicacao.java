package br.poli.gp.arvore.funcao;

import java.util.HashMap;

import br.poli.gp.arvore.Funcao;

public class Multiplicacao extends Funcao {

	private static final long serialVersionUID = -6612692810735686630L;

	public Multiplicacao() {
		super("*");
	}

	@Override
	public double calcularExpressao(HashMap<String, Double> hm) {
		return esquerda.calcularExpressao(hm) * direita.calcularExpressao(hm);
	}

	public String toString(){
		return "(" + esquerda.toString() + "*" + direita.toString() + ")";
	}
	
}
