package br.poli.gp;

import java.util.ArrayList;
import java.util.Random;

public class AlgoritmoGP {
	
	ArrayList<Individuo> populacao;
	
	public AlgoritmoGP(){
		populacao = new ArrayList<Individuo>();
					
		for (int cadaIndividuo = 0; cadaIndividuo < Parametros.NUMERO_TOTAL_INDIVIDUO; cadaIndividuo++) {
			populacao.add(new Individuo(10));
		}
	}

	private void operadorCruzamento() {
		for (int cadaIndividuo = 0; cadaIndividuo < Parametros.NUMERO_TOTAL_INDIVIDUO; cadaIndividuo++) {
			
			
		}
	}

	private double calculateFitness(Individuo individuo) {
		
		return 0;
	}

	private void operadorMutacao() {
		for (int cadaIndividuo = 0; cadaIndividuo < Parametros.NUMERO_TOTAL_INDIVIDUO; cadaIndividuo++) {
			
		}
		
	}
	
	public void calcularValor() {
		for (int cadaIndividuo = 0; cadaIndividuo < Parametros.NUMERO_TOTAL_INDIVIDUO; cadaIndividuo++) {
			System.out.println(populacao.get(cadaIndividuo).calcularValor());
		}
	}
	
	public String toString(){
		String s = "";
		for (Individuo i : populacao){
			s+=i.toString();
		}
		return s;
	}
}
