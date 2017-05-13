package br.poli.gp;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class ThreadMain extends Thread {

	int startingIndex;
	int endingIndex;

	public ThreadMain(int startingIndex, int endingIndex){
		this.startingIndex = startingIndex; 
		this.endingIndex = endingIndex;
	}

	public void run() {
		System.out.println("Thread Running!");
		System.out.println("StartingIndex: "+ startingIndex + "EndingIndex: " + endingIndex);
		try{			
			ArrayList<String> stringList = new ArrayList<String>();

			for (int i = startingIndex; i < endingIndex; i++){
				stringList.add(Parametros.Bases[i]);	
			}

			for (String base : stringList){
				HashMap<Integer, Double> serieTemporal = Common.lerBase(base);
				if (base.equals("lynx"))
					Common.NormalizarLN(serieTemporal);
				else
					Common.Normalizar2(serieTemporal);
				
				double[] mediaDesvio = {Common.CalcularMedia(serieTemporal), Common.CalcularDesvioPadrao(serieTemporal)};

				double[] respostas = new double[30];

				//System.out.println("Base: " + base);

				for (int i = 0; i < 30;){

					System.out.println(base + ", Sim: " + (i + 1));
					
					AlgoritmoGP gp = new AlgoritmoGP(EInicializacao.Completa, serieTemporal, Parametros.TAXA_CRUZAMENTO_MUTACAO
							, Parametros.NUMERO_TOTAL_FUNCAO, Parametros.TAMANHO_MAXIMO_PROFUNDIDADE_ARVORE
							, Parametros.NUMERO_MAXIMO_POPULACAO, mediaDesvio[0], mediaDesvio[1]);

					double d = gp.runGP(i);

					if (!(Double.isInfinite(d) || Double.isNaN(d))){
						respostas[i]=d;
						i++;
					}
				}

				double media = Common.CalcularMedia(respostas);
				double desvio = Common.CalcularDesvioPadrao(respostas);

//				System.out.println("base: " + base + "/med: " + media + "/desvio: " + desvio);
				
				File f = new File("");
				File file = new File(f.getAbsolutePath() + "\\CRU" + base + ".txt");
			    PrintWriter gravarArq = new PrintWriter(file, "UTF-8");
			    
			    gravarArq.write("med: " + media + "/desvio: " + desvio);
			    
	//		    System.out.println("asdf");

			    gravarArq.close();
			}
		} catch (Exception e) {
			System.out.println("Deu Caca");
		}		
	}
}
