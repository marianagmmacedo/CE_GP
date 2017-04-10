package GP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public abstract class Expressao {

	public String valor;
	public int numeroMaximoTermo;
	public List<Expressao> nos;
	public static Random random  = new Random();
	public static HashMap<String, Integer> index;

	public Expressao(String valor){
		this.nos = new ArrayList<Expressao>();
		this.valor = valor;
	}

	public abstract double calcularExpressao();

	public static Expressao criarNovaExpressao(int profundidade, int profundidadeMaxima) {
		
		Expressao expressao = null;
		
		//fluxo caso atingida a profundidade máxima
		if (profundidade == profundidadeMaxima){
			int folhaRand = random.nextInt(Parametros.numeroTotalVariavel+1);
			switch(folhaRand){
				case 0:
					expressao = new Numero();
					break;
				case 1:
					expressao = new Variavel("x");
					break;
				default:
					expressao = new Variavel("y");
					break;
			}
			return expressao;
			
		}
		
		int numRand = random.nextInt(Parametros.numeroTotalFuncoes);
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
				expressao.nos.add(Expressao.criarNovaExpressao(profundidade+1, profundidadeMaxima));
			}
		}
		
		return expressao;



	}
	
	public String toString(){
		String temp = valor;
		for(Expressao e : nos){
			temp += "\n[" + e.toString() + "]";
		}
		return temp;
	}
}