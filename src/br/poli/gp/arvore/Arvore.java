package br.poli.gp.arvore;

import br.poli.gp.Parametros;
import br.poli.gp.arvore.funcao.Numero;
import br.poli.gp.arvore.funcao.Seno;
import br.poli.gp.arvore.funcao.Soma;
import br.poli.gp.arvore.funcao.Subtracao;
import br.poli.gp.arvore.funcao.Variavel;

import java.io.Serializable;

import br.poli.gp.Common;

public class Arvore implements Serializable {

	private static final long serialVersionUID = 2L;
	
	public Funcao no;
	public int tamanhoAtualArvore;

	public String toString(){
		return ("Expressao: (" + no.toString() + ")");
	}

	public double calcularValor() {
		return no.calcularExpressao();
	}
	
	public static Funcao criarNovaExpressaoAleatoria(int profundidade, int profundidadeMaxima) {

		//fluxo caso atingida a profundidade maxima. Devem-se criar folhas
		if (profundidade == profundidadeMaxima){
			int folhaRand = Common.RANDOM.nextInt(Parametros.NUMERO_TOTAL_VARIAVEL+1);
			switch(folhaRand){
				case 0:
					return new Numero();
				case 1:
					return new Variavel("x");
				default:
					return new Variavel("y");
			}
		}
		
		//Fluxo caso seja nó de função.
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
			//			case 4:
			//				return "/";
			//			case 5:
			//				return "seno";
			//			case 6:
			//				return "cosseno";
			//			case 7:
			//				return "tangente";
			//			case 8:
			//				return "integral";
			//			case 9:
			//				return "derivada";
		}
		
		if (expressao!=null){
			for(int i = 0; i < expressao.numeroMaximoTermo; i++){
				expressao.nos.add(Arvore.criarNovaExpressaoAleatoria(profundidade+1, profundidadeMaxima));
			}
		}
		
		return expressao;
	}
}
