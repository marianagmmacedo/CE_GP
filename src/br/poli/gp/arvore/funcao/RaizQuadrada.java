package br.poli.gp.arvore.funcao;

import java.util.HashMap;

import br.poli.gp.arvore.Funcao;

public class RaizQuadrada extends Funcao {

	private static final long serialVersionUID = 3597167541665784043L;

	public RaizQuadrada() {
		super("Sqrt");
	}

	@Override
	public double calcularExpressao(HashMap<String, Double> hm) {
		return Math.sqrt(Math.abs(esquerda.calcularExpressao(hm)));
	}

	public String toString(){
		return "(" + valor+"[Abs[" + esquerda.toString() + "]" + "]" + ")";
	}


}
