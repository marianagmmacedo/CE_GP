package br.poli.gp.arvore;

import br.poli.gp.Parametros;
import br.poli.gp.arvore.funcao.Cosseno;
import br.poli.gp.arvore.funcao.Divisao;
import br.poli.gp.arvore.funcao.Logaritmo;
import br.poli.gp.arvore.funcao.Multiplicacao;
import br.poli.gp.arvore.funcao.Numero;
import br.poli.gp.arvore.funcao.Potencia;
import br.poli.gp.arvore.funcao.RaizQuadrada;
import br.poli.gp.arvore.funcao.Seno;
import br.poli.gp.arvore.funcao.Soma;
import br.poli.gp.arvore.funcao.Subtracao;
import br.poli.gp.arvore.funcao.Tangente;
import br.poli.gp.arvore.funcao.Variavel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import br.poli.gp.Common;

public class Arvore implements Serializable {

	private static final long serialVersionUID = 2L;
	
	public Funcao no;
	public int tamanhoAtualArvore;

	public String toString(){
		return ("Expressao: (" + no.toString() + ")");
	}
	
	public ArrayList<Double> getConstantes(ArrayList<Double> constantes){
//		System.out.println("Arvore");
		return no.getConstantes(constantes);
	}
	
	public ArrayList<Double> atualizarConstantes(ArrayList<Double> constantes){
		return no.atualizarConstantes(constantes);
	}

	public double calcularValor(HashMap<String, Double> hm) {
		return no.calcularExpressao(hm);
	}
	
	public void otimizarArvore(List<Funcao> noFuncao){
		noFuncao.clear();
		no = no.otimizarFuncao(null);
		no.corrigirNos(null, noFuncao, this);
	}
	
	public static Funcao criarNovaExpressaoAleatoria(int profundidade, int profundidadeMaxima, Arvore arvore, int NUMERO_TOTAL_FUNCAO) {

		//fluxo caso atingida a profundidade maxima. Devem-se criar folhas
		if (profundidade == profundidadeMaxima){
			if (Common.RANDOM.nextDouble() > Parametros.CHANCE_CRIACAO_VARIAVEL){
				return new Numero();
			} else {
				return new Variavel("X" + Common.RANDOM.nextInt(Parametros.NUMERO_TOTAL_VARIAVEL));
			}
		}
		
		//Fluxo caso seja n� de fun��o.
		Funcao expressao = null;
//		System.out.println(Parametros.NUMERO_TOTAL_FUNCAO);
		int numRand = Common.RANDOM.nextInt(NUMERO_TOTAL_FUNCAO);
		
		switch (numRand) {
			case 0:
				expressao = new Soma();
				break;
			case 1:
				expressao = new Cosseno();
				break;
			case 2:
				expressao = new Seno();
				break;
			case 3:
				expressao = new Multiplicacao();
				break;
			case 4:
				expressao = new Divisao();
				break;
			case 5:
				expressao = new Subtracao();
				break;
			case 6:
				expressao = new Potencia();
				break;
			case 7:
				expressao = new RaizQuadrada();
				break;
			case 8:
				expressao = new Tangente();
				break;
			case 9:
				expressao = new Logaritmo();
				break;
		}
		
		expressao.arvore = arvore;
		
		if (expressao != null){
			expressao.esquerda = Arvore.criarNovaExpressaoAleatoria(profundidade+1, profundidadeMaxima, arvore, NUMERO_TOTAL_FUNCAO);
			if (!expressao.apenasNoEsquerdo)
				expressao.direita = Arvore.criarNovaExpressaoAleatoria(profundidade+1, profundidadeMaxima, arvore, NUMERO_TOTAL_FUNCAO);
		}
		
		return expressao;
	}
	
	public List<Double> parseToDoubleList(HashMap<String, Double> variableValues) {
		List<Double> ld = new ArrayList<Double>();
		no.parseToDoubleList(variableValues, ld);
		return ld;
	}

	
}
