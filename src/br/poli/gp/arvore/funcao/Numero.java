package br.poli.gp.arvore.funcao;

import br.poli.gp.Common;
import br.poli.gp.arvore.Funcao;

public class Numero extends Funcao{
	
	public Numero() {
		super(Common.RANDOM.nextDouble()+"");
	}

	@Override
	public double calcularExpressao() {
		return Double.parseDouble(this.valor);
	}

	

}
