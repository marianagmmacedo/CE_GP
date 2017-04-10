package GP;

import java.util.Random;

public class Individuo {

	Arvore arvore;
	private double fitness;
	static Random random;

	public Individuo(){
		arvore = new Arvore();
		random = new Random();
		IndividuoMetadeProfundidade();
	}

	public void IndividuoAleatorio(){
		IndividuoParametrizado(random.nextInt(Parametros.tamanhoMaximoArvoreProfundidade));
	}

	public void IndividuoMetadeProfundidade(){
		IndividuoParametrizado(Parametros.tamanhoMaximoArvoreProfundidade/2);
	}

	public void IndividuoCompleto(){
		IndividuoParametrizado(Parametros.tamanhoMaximoArvoreProfundidade);
	}

	public void IndividuoParametrizado(int profundidadeMaxima){
		arvore.setNos(Expressao.criarNovaExpressao(1, profundidadeMaxima));
	}

	public double getFitness() {
		return fitness;
	}


	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public String toString(){
		return arvore.toString();
	}

	public double calcularValor() {
		return arvore.calcularValor();
	}
}
