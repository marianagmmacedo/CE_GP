package br.poli.gp.arvore.funcao;

import java.util.ArrayList;
import java.util.HashMap;

import br.poli.gp.arvore.Funcao;

public class Multiplicacao extends Funcao {

	private static final long serialVersionUID = -6612692810735686630L;

	public Multiplicacao() {
		super("*");
	}

	@Override
	public double calcularExpressao(HashMap<String, Double> hm) {
		return esquerda.calcularExpressao(hm) * direita.calcularExpressao(hm);
	}

	public String toString(){
		return "(" + esquerda.toString() + "*" + direita.toString() + ")";
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
