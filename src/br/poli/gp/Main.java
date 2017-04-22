package br.poli.gp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import br.poli.gp.arvore.Funcao;

public class Main {

	public static void main(String[] args) throws IOException {
		
		HashMap<Integer, Double> serieTemporal = Common.lerBase("sunspot");
		Common.Normalizar(serieTemporal);
		

		
		/*while(true){			
			Individuo i = new Individuo(6);
			System.out.println("PRE:" + i.toString());
			//new AlgoritmoGp
			//double d = i.calcularValor(null);
			//System.out.println("Solulï¿½ï¿½o: " + i.calcularValor(null));
			//i.otimizarArvore();
			//System.out.println("POS:" + i.toString());
			//if ((d+"").contains("NaN")) break;
		}*/
		
		// TRANSFORMACAO LINEAR ENTRE 0 E 1
		for (int simulacao = 0; simulacao < Parametros.NUMERO_TOTAL_SIMULACAO; simulacao++) {
			AlgoritmoGP gp = new AlgoritmoGP(EInicializacao.Completa, serieTemporal);
			gp.runGP();
//			System.out.println("GP:" + gp.toString());
//			System.out.println("GP:" + gp.melhorIndividuo.fitness);
//			System.out.println("GP:" + gp.populacao.get(0).toString());
//			System.out.println("GP:" + gp.populacao.get(0).fitness);
		}
	}
	
	
	//TODO
	/*Otimizações:

		Alternar valor de variávies para calcular fitness
		Substituir x/1 por x.
		Substituir Cos/Sin/Log/Sqrt de um número pelo resultado
		*/
}
