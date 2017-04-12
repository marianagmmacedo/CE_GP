package br.poli.gp.arvore.funcao;

import br.poli.gp.arvore.Funcao;

public class Variavel extends Funcao{

	private static final long serialVersionUID = -7339598167466991079L;

	public Variavel(String valor) {
		super(valor);
	}

	@Override
	public double calcularExpressao() {
		return Funcao.index.get(this.valor);
	}

}
