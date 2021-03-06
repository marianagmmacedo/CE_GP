package br.poli.gp.arvore.funcao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.poli.gp.arvore.Funcao;

public class Divisao extends Funcao {

	private static final long serialVersionUID = -5771054330938595952L;

	public Divisao() {
		super("/");
	}

	@Override
	public double calcularExpressao(HashMap<String, Double> hm) {
		double dir = direita.calcularExpressao(hm);
		if (dir == 0) return 1;
		return esquerda.calcularExpressao(hm) / dir;
	}

	public String toString(){
		return "(" + esquerda.toString() + "/" + direita.toString() + ")";
	}

	@Override
	public ArrayList<Double> getConstantes(ArrayList<Double> constantes) {
		constantes = esquerda.getConstantes(constantes);
		constantes = direita.getConstantes(constantes);
		return constantes;
	}
	
	@Override
	public ArrayList<Double> atualizarConstantes(ArrayList<Double> constantes) {
		constantes = esquerda.atualizarConstantes(constantes);
		constantes = direita.atualizarConstantes(constantes);
		return constantes;
	}	
}
