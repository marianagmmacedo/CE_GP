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
import java.util.HashMap;

import br.poli.gp.Common;

public class Arvore implements Serializable {

	private static final long serialVersionUID = 2L;
	
	public Funcao no;
	public int tamanhoAtualArvore;

	public String toString(){
		return ("Expressao: (" + no.toString() + ")");
	}

	public double calcularValor(HashMap<String, Double> hm) {
		return no.calcularExpressao(hm);
	}
	
	public void otimizarArvore(){
		no.otimizarFuncao(this, null, 0);
	}
	
	public static Funcao criarNovaExpressaoAleatoria(int profundidade, int profundidadeMaxima) {

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
		
		int numRand = Common.RANDOM.nextInt(Parametros.NUMERO_TOTAL_FUNCAO);
		
		switch (numRand) {
		case 0:
			expressao = new Soma();
			break;
		case 1:
			expressao = new Subtracao();
			break;
		case 2:
			expressao = new Seno();
			break;
		case 3:
			expressao = new Cosseno();
			break;
		case 4:
			expressao = new Multiplicacao();
			break;
		case 5:
			expressao = new Divisao();
			break;
		case 6:
			expressao = new RaizQuadrada();
			break;
		case 7:
			expressao = new Tangente();
			break;
		case 8:
			expressao = new Potencia();
			break;
		case 9:
			expressao = new Logaritmo();
			break;
		}
		
		if (expressao!=null){
			for(int i = 0; i < expressao.numeroMaximoTermo; i++){
				expressao.nos.add(Arvore.criarNovaExpressaoAleatoria(profundidade+1, profundidadeMaxima));
			}
		}
		
		return expressao;
	}
}
