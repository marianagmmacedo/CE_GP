package br.poli.gp.arvore.funcao;

import java.util.HashMap;

import br.poli.gp.Parametros;
import br.poli.gp.arvore.Funcao;

public class Subtracao extends Funcao{

	private static final long serialVersionUID = -2578207134051816462L;

	public Subtracao() {
		super("-");
		this.numeroMaximoTermo = Parametros.TAMANHO_MAXIMO_LARGURA_ARVORE;
	}

	@Override
	public double calcularExpressao(HashMap<String, Double> hm) {
		double subtracao = 0.0;
		for (int numeroNos = 0; numeroNos < nos.size(); numeroNos++) {
			subtracao -= nos.get(numeroNos).calcularExpressao(hm);
		}
		return subtracao;
	}
	
	public String toString(){
		String s = "(";
		for(Funcao e : nos){
			s+= s.equals(")")?e.toString():"-" + e.toString();
		}
		return s + ")";
	}
}
