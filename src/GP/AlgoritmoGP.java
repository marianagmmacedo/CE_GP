package GP;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlgoritmoGP {
	
	Random random;
	ArrayList<Individuo> populacao;
	
	public AlgoritmoGP(){
		random = new Random();
		populacao = new ArrayList<Individuo>();
		initializarPopulacao();
		
//		for (int cadaSimulacao = 0; cadaSimulacao < Parametros.numeroTotalSimulacao; cadaSimulacao++) {
//			for (int cadaIteracao = 0; cadaIteracao < Parametros.numeroTotalIteracao; cadaIteracao++) {
//				
//				
//				
//			}
//		}
	}

	private void operadorCruzamento() {
		for (int cadaIndividuo = 0; cadaIndividuo < Parametros.numeroTotalIndividuo; cadaIndividuo++) {
			
			
		}
		
	}

	private double calculateFitness(Individuo individuo) {
		
		return 0;
	}

	private void operadorMutacao() {
		for (int cadaIndividuo = 0; cadaIndividuo < Parametros.numeroTotalIndividuo; cadaIndividuo++) {
			
		}
		
	}

	private void initializarPopulacao() {
		for (int cadaIndividuo = 0; cadaIndividuo < Parametros.numeroTotalIndividuo; cadaIndividuo++) {
			populacao.add(new Individuo());
		}
	}
	public String toString(){
		String s = "";
		for (Individuo i : populacao){
			s+=i.toString();
		}
		return s;
	}

	public void calcularValor() {
		for (int cadaIndividuo = 0; cadaIndividuo < Parametros.numeroTotalIndividuo; cadaIndividuo++) {
			System.out.println(populacao.get(cadaIndividuo).calcularValor());
		}
	}
}
