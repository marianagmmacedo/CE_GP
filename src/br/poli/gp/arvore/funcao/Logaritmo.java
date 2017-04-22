package br.poli.gp.arvore.funcao;

import java.util.HashMap;

import br.poli.gp.arvore.Funcao;

public class Logaritmo extends Funcao {

	private static final long serialVersionUID = -8287920052257751187L;

	public Logaritmo() {
		super("Log");
		apenasNoEsquerdo = true;
	}

	@Override
	public double calcularExpressao(HashMap<String, Double> hm) {
		return Math.log(Math.abs(esquerda.calcularExpressao(hm)));
	}

	public String toString(){
		return "(" + valor+"[Abs[" + esquerda.toString() + "]" + "]" + ")";
	}


}
