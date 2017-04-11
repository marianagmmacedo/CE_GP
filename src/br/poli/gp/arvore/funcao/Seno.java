package br.poli.gp.arvore.funcao;

import br.poli.gp.arvore.Funcao;

public class Seno extends Funcao{

	public Seno() {
		super("Sin");
		this.numeroMaximoTermo = 1;
	}

	@Override
	public double calcularExpressao() {		
		return Math.sin(nos.get(0).calcularExpressao());
	}
	
	public String toString(){
		return "(" + valor+"[" + nos.get(0).toString() + "]" + ")";
	}

}
