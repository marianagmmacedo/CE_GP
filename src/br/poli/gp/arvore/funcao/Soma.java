package br.poli.gp.arvore.funcao;

import br.poli.gp.Parametros;
import br.poli.gp.arvore.Funcao;

public class Soma extends Funcao {

	private static final long serialVersionUID = -5494585914574328953L;

	public Soma() {
		super("+");
		this.numeroMaximoTermo = Parametros.TAMANHO_MAXIMO_LARGURA_ARVORE;
	}

	@Override
	public double calcularExpressao() {
		double soma = 0.0;
		for (Funcao no : this.nos) {
			soma += no.calcularExpressao();
		}
		return soma;
	}

	public String toString(){
		String s = "(";
		for(Funcao e : this.nos){
			s+= s.equals(")")?e.toString():"+" + e.toString();
		}
		return s + ")";
	}
	
}
