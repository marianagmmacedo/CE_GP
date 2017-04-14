package br.poli.gp.arvore.funcao;

import java.util.HashMap;

import br.poli.gp.arvore.Funcao;

public class Divisao extends Funcao {

	private static final long serialVersionUID = -5771054330938595952L;

	public Divisao() {
		super("/");
	}

	@Override
	public double calcularExpressao(HashMap<String, Double> hm) {
		return esquerda.calcularExpressao(hm) / direita.calcularExpressao(hm);
	}

	public String toString(){
		return "(" + esquerda.toString() + "/" + direita.toString() + ")";
	}
	
}
