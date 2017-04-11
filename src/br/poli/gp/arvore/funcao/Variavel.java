package br.poli.gp.arvore.funcao;

import br.poli.gp.arvore.Funcao;

public class Variavel extends Funcao{

	public Variavel(String valor) {
		super(valor);
	}

	@Override
	public double calcularExpressao() {
		return Funcao.index.get(this.valor);
	}

}
