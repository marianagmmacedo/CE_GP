package br.poli.gp.arvore.funcao;

import java.util.ArrayList;
import java.util.HashMap;

import br.poli.gp.arvore.Funcao;

public class Potencia extends Funcao{

	private static final long serialVersionUID = -4273888352275119593L;

	public Potencia() {
		super("Power");
	}

	@Override
	public double calcularExpressao(HashMap<String, Double> hm) {
		return Math.pow(Math.abs(esquerda.calcularExpressao(hm)),direita.calcularExpressao(hm));
	}

	public String toString(){
		return "(" + valor+"[Abs[" + esquerda.toString() + "]," + direita.toString() + "]" + ")";
	}
	
	@Override
	public ArrayList<Double> atualizarConstantes(ArrayList<Double> constantes) {
		constantes = esquerda.atualizarConstantes(constantes);
		constantes = direita.atualizarConstantes(constantes);
		return constantes;
	}
	
	@Override
	public ArrayList<Double> getConstantes(ArrayList<Double> constantes) {
		constantes = esquerda.getConstantes(constantes);
		constantes = direita.getConstantes(constantes);
		return constantes;
	}
}
