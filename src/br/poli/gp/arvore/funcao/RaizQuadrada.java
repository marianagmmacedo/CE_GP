package br.poli.gp.arvore.funcao;

import java.util.HashMap;

import br.poli.gp.arvore.Funcao;

public class RaizQuadrada extends Funcao {

	private static final long serialVersionUID = 3597167541665784043L;

	public RaizQuadrada() {
		super("Sqrt");
		this.numeroMaximoTermo = 1;
	}

	@Override
	public double calcularExpressao(HashMap<String, Double> hm) {
		return Math.sqrt(Math.abs(nos.get(0).calcularExpressao(hm)));
	}

	public String toString(){
		return "(" + valor+"[Abs[" + nos.get(0).toString() + "]" + "]" + ")";
	}


}
