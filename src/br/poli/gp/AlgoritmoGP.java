package br.poli.gp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.poli.gp.arvore.Funcao;

public class AlgoritmoGP {
	
	ArrayList<Individuo> populacao;
	
	//     <X     , Y     >
	HashMap<Double, Double> serieTemporal;
	
	
	public AlgoritmoGP(EInicializacao tipoInicializacao, HashMap<Double, Double> serieTemporal){
		populacao = new ArrayList<Individuo>();
		this.serieTemporal = serieTemporal;
		for (int cadaIndividuo = 0; cadaIndividuo < Parametros.NUMERO_TOTAL_INDIVIDUO; cadaIndividuo++) {
			populacao.add(new Individuo(tipoInicializacao));
		}
	}
	
	public AlgoritmoGP(int profundidadeIndividuo, HashMap<Double, Double> serieTemporal){
		populacao = new ArrayList<Individuo>();
		this.serieTemporal = serieTemporal;
		
		for (int cadaIndividuo = 0; cadaIndividuo < Parametros.NUMERO_TOTAL_INDIVIDUO; cadaIndividuo++) {
			populacao.add(new Individuo(profundidadeIndividuo));
		}
	}
	
	private void gerarDescendentes(Individuo pai, Individuo mae){
		Individuo filho = (Individuo) Common.DeepCopy(pai);
		Individuo filha = (Individuo) Common.DeepCopy(mae);

		int noCrossOverFilho = Common.RANDOM.nextInt(filho.noFuncao.size());
		int noCrossOverFilha = Common.RANDOM.nextInt(filha.noFuncao.size());
		
		Funcao fFilho = filho.noFuncao.get(noCrossOverFilho);
		Funcao fFilha = filho.noFuncao.get(noCrossOverFilha);
		
		fFilho.crossover(fFilha);
		
		populacao.add(filho);
		populacao.add(filha);
	}

	private void calcularFitnessPopulacao() {
		HashMap<String, Double> hm = new HashMap<String, Double>();
		hm.put("x", 0d);
		
		for(Individuo i : populacao){
			double fitness = 0;
			for(Map.Entry<Double, Double> entry : serieTemporal.entrySet()){
				hm.replace("x", entry.getKey());
				//fitness += i.calcularValor(); REGRA PARA CALCULAR O FITNESS DEVE SER DISCUTIDA
			}
			
			i.fitness = fitness;
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
