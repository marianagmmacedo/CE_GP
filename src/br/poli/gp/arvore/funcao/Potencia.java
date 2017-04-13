package br.poli.gp.arvore.funcao;

import java.util.HashMap;

import br.poli.gp.arvore.Funcao;

public class Potencia extends Funcao{

	private static final long serialVersionUID = -4273888352275119593L;

	public Potencia() {
		super("Power");
		this.numeroMaximoTermo = 2;
	}

	@Override
	public double calcularExpressao(HashMap<String, Double> hm) {
		return Math.pow(Math.abs(nos.get(0).calcularExpressao(hm)),nos.get(1).calcularExpressao(hm));
	}

	public String toString(){
		return "(" + valor+"[Abs[" + nos.get(0).toString() + "]," + nos.get(1).toString() + "]" + ")";
	}

}
