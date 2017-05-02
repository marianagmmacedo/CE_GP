package br.poli.gp.arvore.funcao;

import java.util.ArrayList;
import java.util.HashMap;

import br.poli.gp.Common;
import br.poli.gp.arvore.Funcao;

public class Numero extends Funcao{
	
	private static final long serialVersionUID = 2523627879534832795L;

	public double valorNumerico;
	
	public Numero() {
		valorNumerico = Common.RANDOM.nextDouble();
	}
	
	public Numero(double valorNumerico){
		this.valorNumerico = valorNumerico;
	}
	
	@Override
	public String toString(){
		return valorNumerico + "";
	}

	@Override
	public double calcularExpressao(HashMap<String, Double> hm) {
		return valorNumerico;
	}
	
	@Override
	public ArrayList<Double> atualizarConstantes(ArrayList<Double> constantes) {
		this.valorNumerico = constantes.get(0);
		constantes.remove(0);
		return constantes;
	}

	@Override
	public ArrayList<Double> getConstantes(ArrayList<Double> constantes) {
		constantes.add(valorNumerico);
		return constantes;
	}
	
	

}
