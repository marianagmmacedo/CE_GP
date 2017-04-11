package br.poli.gp;

import br.poli.gp.arvore.Arvore;

public class Individuo {

	Arvore arvore;
	double fitness;

	public Individuo(int tamanhoProfundidade){
		arvore = new Arvore();
		arvore.nos = Arvore.criarNovaExpressaoAleatoria(1, tamanhoProfundidade);
	}
	
	public Individuo(EInicializacao tipoInicializacao){
		arvore = new Arvore();
		
		if (tipoInicializacao == EInicializacao.Aleatoria)
			arvore.nos = Arvore.criarNovaExpressaoAleatoria(1, Common.RANDOM.nextInt(Parametros.TAMANHO_MAXIMO_PROFUNDIDADE_ARVORE));
		else if (tipoInicializacao == EInicializacao.Completa)
			arvore.nos = Arvore.criarNovaExpressaoAleatoria(1, Parametros.TAMANHO_MAXIMO_PROFUNDIDADE_ARVORE);
		else if (tipoInicializacao == EInicializacao.MetadeProfundidade)
			arvore.nos = Arvore.criarNovaExpressaoAleatoria(1, Parametros.TAMANHO_MAXIMO_PROFUNDIDADE_ARVORE/2);
	}
	
	public String toString(){
		return arvore.toString();
	}

	public double calcularValor() {
		return arvore.calcularValor();
	}
}
