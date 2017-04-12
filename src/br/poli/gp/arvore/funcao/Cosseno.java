package br.poli.gp.arvore.funcao;

import java.util.HashMap;

import br.poli.gp.arvore.Funcao;

public class Cosseno extends Funcao{

	private static final long serialVersionUID = -4681976996996751347L;

	public Cosseno() {
		super("Cos");
		this.numeroMaximoTermo = 1;
	}

	@Override
	public double calcularExpressao(HashMap<String, Double> hm) {		
		return Math.cos(nos.get(0).calcularExpressao(hm));
	}
	
	public String toString(){
		return "(" + valor+"[" + nos.get(0).toString() + "]" + ")";
	}

}
