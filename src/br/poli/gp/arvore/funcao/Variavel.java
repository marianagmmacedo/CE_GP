package br.poli.gp.arvore.funcao;

import java.util.ArrayList;
import java.util.HashMap;

import br.poli.gp.arvore.Funcao;

public class Variavel extends Funcao{

	private static final long serialVersionUID = -7339598167466991079L;

	public Variavel(String valor) {
		super(valor);
	}

	@Override
	public double calcularExpressao(HashMap<String, Double> hm) {
		return hm.get(this.valor);
	}

	@Override
	public String toString() {
		return valor;
	}
	
	@Override
	public ArrayList<Double> atualizarConstantes(ArrayList<Double> constantes) {
		return constantes;
	}

	@Override
	public ArrayList<Double> getConstantes(ArrayList<Double> constantes) {
		return constantes;
	}
}
