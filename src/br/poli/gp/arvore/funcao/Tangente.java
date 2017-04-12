package br.poli.gp.arvore.funcao;

import java.util.HashMap;

import br.poli.gp.arvore.Funcao;

public class Tangente extends Funcao{

	private static final long serialVersionUID = -3286648095169057790L;

	public Tangente() {
		super("Tan");
		this.numeroMaximoTermo = 1;
	}

	@Override
	public double calcularExpressao(HashMap<String, Double> hm) {		
		return Math.tan(nos.get(0).calcularExpressao(hm));
	}
	
	public String toString(){
		return "(" + valor+"[" + nos.get(0).toString() + "]" + ")";
	}

}
