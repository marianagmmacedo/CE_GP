package br.poli.gp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.poli.gp.arvore.Arvore;
import br.poli.gp.arvore.Funcao;

public class Individuo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	Arvore arvore;
	double fitness;
	List<Funcao> noFuncao;

	public Individuo(int tamanhoProfundidade){
		arvore = new Arvore();
		arvore.no = Arvore.criarNovaExpressaoAleatoria(1, tamanhoProfundidade);
		noFuncao = new ArrayList<Funcao>();
		atualizarReferenciaNosFuncao(noFuncao);
	}
	
	public Individuo(EInicializacao tipoInicializacao){
		arvore = new Arvore();
		noFuncao = new ArrayList<Funcao>();
		
		if (tipoInicializacao == EInicializacao.Aleatoria)
			arvore.no = Arvore.criarNovaExpressaoAleatoria(1, Common.RANDOM.nextInt(Parametros.TAMANHO_MAXIMO_PROFUNDIDADE_ARVORE));
		else if (tipoInicializacao == EInicializacao.Completa)
			arvore.no = Arvore.criarNovaExpressaoAleatoria(1, Parametros.TAMANHO_MAXIMO_PROFUNDIDADE_ARVORE);
		else if (tipoInicializacao == EInicializacao.MetadeProfundidade)
			arvore.no = Arvore.criarNovaExpressaoAleatoria(1, Parametros.TAMANHO_MAXIMO_PROFUNDIDADE_ARVORE/2);
		
		atualizarReferenciaNosFuncao(noFuncao);
	}
	
	public void atualizarReferenciaNosFuncao(List<Funcao> noFuncao){
		arvore.no.atualizarReferenciaNosFuncao(null, noFuncao);
	}
	
	public String toString(){
		return arvore.toString();
	}

	public double calcularValor() {
		return arvore.calcularValor();
	}
}
