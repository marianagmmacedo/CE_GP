package br.poli.gp.arvore.funcao;

import java.util.HashMap;

import br.poli.gp.arvore.Funcao;

public class Logaritmo extends Funcao {

	private static final long serialVersionUID = -8287920052257751187L;

	public Logaritmo() {
		super("Log");
		this.numeroMaximoTermo = 1;
	}

	@Override
	public double calcularExpressao(HashMap<String, Double> hm) {
		return Math.log(Math.abs(nos.get(0).calcularExpressao(hm)));
	}

	public String toString(){
		return "(" + valor+"[Abs[" + nos.get(0).toString() + "]" + "]" + ")";
	}


}
