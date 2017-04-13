package br.poli.gp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import br.poli.gp.arvore.Funcao;

public class Main {

	public static void main(String[] args) throws IOException {
		
		HashMap<Double, Double> hm = new HashMap<Double, Double>();
		for (Double x = 1.0; x < 100.0; x++){
			hm.put(x, Common.RANDOM.nextDouble());
		}
		// TRANSFORMACAO LINEAR ENTRE 0 E 1
		for (int simulacao = 0; simulacao < Parametros.NUMERO_TOTAL_SIMULACAO; simulacao++) {
			AlgoritmoGP gp = new AlgoritmoGP(EInicializacao.MetadeProfundidade, hm);
//			System.out.println("GP:" + gp.toString());
//			System.out.println("GP:" + gp.melhorIndividuo.fitness);
//			System.out.println("GP:" + gp.populacao.get(0).toString());
//			System.out.println("GP:" + gp.populacao.get(0).fitness);
		}
		
		

	}
}
