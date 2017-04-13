package br.poli.gp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import br.poli.gp.arvore.Funcao;

public class Main {

	public static void main(String[] args) throws IOException {
		
		HashMap<Double, Double> serieTemporal = Common.lerBase("sunspot");
		
		while(true){
		Individuo i = new Individuo(10);
		System.out.println(i.toString());
		double d = i.calcularValor(null);
		System.out.println("Solulção: " + i.calcularValor(null));
		System.out.println(i.toString());
		System.out.println(d == i.calcularValor(null));
		if (d != i.calcularValor(null)) break;
		}
		
		// TRANSFORMACAO LINEAR ENTRE 0 E 1
		/*for (int simulacao = 0; simulacao < Parametros.NUMERO_TOTAL_SIMULACAO; simulacao++) {
			AlgoritmoGP gp = new AlgoritmoGP(EInicializacao.MetadeProfundidade, serieTemporal);
			gp.runGP();
//			System.out.println("GP:" + gp.toString());
//			System.out.println("GP:" + gp.melhorIndividuo.fitness);
//			System.out.println("GP:" + gp.populacao.get(0).toString());
//			System.out.println("GP:" + gp.populacao.get(0).fitness);
		}*/
		
		

	}
}
