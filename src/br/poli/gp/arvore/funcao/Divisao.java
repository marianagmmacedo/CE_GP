package br.poli.gp.arvore.funcao;

import java.util.HashMap;

import br.poli.gp.Parametros;
import br.poli.gp.arvore.Funcao;

public class Divisao extends Funcao {

	private static final long serialVersionUID = -5771054330938595952L;

	public Divisao() {
		super("/");
		this.numeroMaximoTermo = Parametros.TAMANHO_MAXIMO_LARGURA_ARVORE;
	}

	@Override
	public double calcularExpressao(HashMap<String, Double> hm) {
		double divisao = this.nos.get(0).calcularExpressao(hm);
		for (int numeroNos = 1; numeroNos < nos.size(); numeroNos++) {
			divisao /= nos.get(numeroNos).calcularExpressao(hm);
		}
		return divisao;
	}

	public String toString(){
		String s = "(";
		for(Funcao e : this.nos){
			s+= s.equals("(")?e.toString():"/" + e.toString();
		}
		return s + ")";
	}
	
}
