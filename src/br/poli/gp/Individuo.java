package br.poli.gp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
		fitness = Parametros.TIPO_DE_OTIMIZACAO == "MINIMIZACAO"? Double.MAX_VALUE: Double.MIN_VALUE;
		arvore.no = Arvore.criarNovaExpressaoAleatoria(1, tamanhoProfundidade, arvore);
		noFuncao = new ArrayList<Funcao>();
		
		otimizarArvore();
	}
	
	public Individuo(EInicializacao tipoInicializacao){
		arvore = new Arvore();
		noFuncao = new ArrayList<Funcao>();
		
		if (tipoInicializacao == EInicializacao.Aleatoria){
			arvore.no = Arvore.criarNovaExpressaoAleatoria(1, 1 + Common.RANDOM.nextInt(Parametros.TAMANHO_MAXIMO_PROFUNDIDADE_ARVORE-1), arvore);
		}else if (tipoInicializacao == EInicializacao.Completa)
			arvore.no = Arvore.criarNovaExpressaoAleatoria(1, Parametros.TAMANHO_MAXIMO_PROFUNDIDADE_ARVORE, arvore);
		else if (tipoInicializacao == EInicializacao.MetadeProfundidade)
			arvore.no = Arvore.criarNovaExpressaoAleatoria(1, Parametros.TAMANHO_MAXIMO_PROFUNDIDADE_ARVORE/2, arvore);
		else if (tipoInicializacao == EInicializacao.Mutacao)
			arvore.no = Arvore.criarNovaExpressaoAleatoria(1, Parametros.TAMANHO_MAXIMO_MUTACAO_PROFUNDIDADE, arvore);
		
		otimizarArvore();
	}
	
	public void otimizarArvore(){
		arvore.otimizarArvore(noFuncao);
	}
		
	public String toString(){
		return arvore.toString();
	}

	public double calcularValor(HashMap<String, Double> hm) {
		return arvore.calcularValor(hm);
	}
}
