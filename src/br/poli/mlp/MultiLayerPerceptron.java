package br.poli.mlp;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import br.poli.gp.Common;
import br.poli.gp.Parametros;
import br.poli.output.Output;



public class MultiLayerPerceptron {
	
	// Fazer com que a entrada seja escolhida aleatoriamente com isso (COMO) mapear as entradas
	
	// ok = inicializado
	// emite sinal recebe sinal
	static int numberHiddenLayers; // ok
	static int numberLayers; // ok
	static int numberInputNeurons; // ok
	static int[] numberNeurons; // ok
	static int numberOutputNeurons; // ok
	
	static ArrayList<Double[]> bias; // ok numero de camadas com numero de neuronios
	Double [][] delta; // ok numero de camadas com numero de neuronios
	Double [][] oldDelta; //ok numero de camadas com numero de neuronios
	
	static ArrayList<Double[][]> weight; // ok numero de camadas com numero de neuronio que emite para que recebe
	ArrayList<Double[][]> oldWeight; // ok numero de camadas com numero de neuronio que emite para que recebe

	
	Double [] netOutput; // ok numero da entrada com a net da resposta
	
	static Double [][] input; // tabela de entrada

	static Double [][] output; // tabela de saida (resposta - f(net))
	static Double [] errorOutput;
	static Double alfa;
	static Double beta;
	public Random random;
	int totalIteration;
	static ArrayList<Double[]> sensibility;
	static int tamanhoJanela;
	static double media;
	static double desvio;
	static int sizeValidacao;
	static int sim;
	// ok = construtor 
	public MultiLayerPerceptron(String nomeBASE, int validarSize
			, ArrayList<Double> inputFSS, int simulacao) throws IOException{
		sim = simulacao;
		sizeValidacao = validarSize;
		random = new Random();
		totalIteration = 200;
		tamanhoJanela = 2;
		//String csvFile1 = MOEADDRAparameters.pathDatabase;
		readData(nomeBASE);
//		System.out.println(getOutput()[0][0]);
		
		alfa = inputFSS.get(0)*0.00001; //(0.001)
		beta = inputFSS.get(1)*0.00001; //(0.002)
		numberInputNeurons = (int) Math.ceil(inputFSS.get(2)); //2
		numberOutputNeurons = (int) Math.ceil(inputFSS.get(3)); //1
		numberHiddenLayers = (int) Math.ceil(inputFSS.get(4)); //1
		oldWeight = new ArrayList<Double[][]>();
		weight  = new ArrayList<Double[][]>();
		sensibility = new ArrayList<Double[]>();
		netOutput = new Double[numberOutputNeurons];
		errorOutput = new Double[numberOutputNeurons];
		
		numberLayers = (numberHiddenLayers+2);
		numberNeurons = new int[numberLayers];
		numberNeurons[0] = numberInputNeurons;
		
		for (int i = 1; i < numberLayers-1; i++) {
			numberNeurons[i] = (int) (int) Math.ceil(inputFSS.get(5));//2
		}
		
		numberNeurons[numberLayers-1] = numberOutputNeurons;
		for (int i = 0; i < numberLayers-1; i++) {	
			int count = 0;
			Double [][] pesosInternos = new Double[numberNeurons[i]][numberNeurons[i+1]];
			Double [][] arrayZero = new Double[numberNeurons[i]][numberNeurons[i+1]];
			
			for (int j = 0; j < numberNeurons[i]; j++) {
				for (int k = 0; k < numberNeurons[i+1]; k++) {
					pesosInternos[j][k] = random.nextDouble();
					arrayZero[j][k] = 0.0;
					count++;
				}
			}
			oldWeight.add(arrayZero);
			weight.add(pesosInternos);
			sensibility.add(new Double[numberNeurons[i]]);
			
		}
		
		// inicializar bias, antigodelta, delta
		ArrayList<Double[]> biasX = new ArrayList<Double[]>();
		int count = 0;

		for (int i = 1; i < numberLayers; i++) {
			Double[] b = new Double[numberNeurons[i]];
			for (int j = 0; j < numberNeurons[i]; j++) {
				b[j] = random.nextDouble();
				count++;
				
			}
			biasX.add(b);
		}
		bias = (biasX);
		
		for (int i = 0; i < numberOutputNeurons; i++) {
			errorOutput[i] = 0.0;

		}
		
	}

	public Double[] forwardBackward(){

		ArrayList<Double[]> newNet = new ArrayList<Double[]>();
		ArrayList<Double[]> newFnet = new ArrayList<Double[]>();
		
		for (int j = 0; j < numberLayers; j++) {
			Double[] x = new Double [numberNeurons[j]];
			for (int k = 0; k < numberNeurons[j]; k++) {
					x[k] = 0.0;
			}
			newNet.add(x);
			newFnet.add(x);
			
		}
		int error = 0;
		// Para cada iteracao
		for (int iteration = 1; iteration <= totalIteration; iteration++) {
			int eachLayer = 0;
			// Para cada exemplo
			Double[]errorOutputTEMP = new Double[numberOutputNeurons];
			for (int i = 0; i < numberOutputNeurons; i++) {
				errorOutputTEMP[i] = 0.0;
			}
			for (int eachInput = 0; eachInput < (input.length-sizeValidacao); eachInput++) {
				
				// Calcular o sinal da camada entrada para a primeira camada escondida
				Double []nets = new Double[numberNeurons[1]];
				Double []fnets = new Double[numberNeurons[1]];
				
				for (int eachHiddenNeuron = 0; eachHiddenNeuron < numberNeurons[1]; eachHiddenNeuron++) {
					nets[eachHiddenNeuron] = 0.0;
					nets[eachHiddenNeuron] = bias.get(0)[eachHiddenNeuron];
					Double x = 0.0;
					for (int eachInputRow = 0; eachInputRow < numberNeurons[0]; eachInputRow++) {
						//System.out.println("eachInputRow--------"+eachInputRow+"------"+input[eachInput][eachInputRow]);
						x = (weight.get(0)[eachInputRow][eachHiddenNeuron]*input[eachInput][eachInputRow]);
						nets[eachHiddenNeuron] += x;			
					}
					fnets[eachHiddenNeuron] = (1.0 / (1 + Math.exp(-nets[eachHiddenNeuron])));
				}
				
				// Salvar net e fnet da camada de entrada para camada escondida
				newNet.set(0, nets);
				newFnet.set(0, fnets);
				
				// Calcular o sinal da primeira camada escondida até a saida
				for (eachLayer =1; eachLayer < numberLayers-1; eachLayer++) {
					
					nets = new Double[numberNeurons[eachLayer+1]];
					fnets = new Double[numberNeurons[eachLayer+1]];
			
					for (int eachHiddenNeuronNext = 0; eachHiddenNeuronNext < numberNeurons[eachLayer+1]; eachHiddenNeuronNext++) {
						nets[eachHiddenNeuronNext] = 0.0;
						nets[eachHiddenNeuronNext] = bias.get(eachLayer)[eachHiddenNeuronNext];
		
						for (int eachHiddenNeuronJ = 0; eachHiddenNeuronJ < numberNeurons[eachLayer]; eachHiddenNeuronJ++) {
							nets[eachHiddenNeuronNext] += ((weight.get(eachLayer)[eachHiddenNeuronJ][eachHiddenNeuronNext])*newFnet.get(eachLayer-1)[eachHiddenNeuronJ]);
						
						}
						

						fnets[eachHiddenNeuronNext] = (1.0 / (1 + Math.exp(-nets[eachHiddenNeuronNext])));
					}
					
					// Salvar cada net e fnet 
					newNet.set(eachLayer, nets);
					newFnet.set(eachLayer, fnets);
					
					
				}
				// Como todo sinal chegou a saida, calcularemos o erro 
				for (int i = 0; i < numberOutputNeurons; i++) {
					Double x = (newFnet.get(numberLayers-2)[i]);
					if(x.isNaN()){
						error++;
//						System.out.println("x.isNaN()");
						errorOutputTEMP[i] = 1.0;
					}else{
//						errorOutputTEMP[i] = (output[eachInput][i] - (newFnet.get(numberLayers-2)[i]));			
						errorOutputTEMP[i] = Math.pow(output[eachInput][i] - (newFnet.get(numberLayers-2)[i]),2)/2;	
					}
//					System.out.println(errorOutputTEMP[i]);
					

				}

//				System.out.println();
				// ------------------------
				// Começa o backpropagation

				//Calcular a sensibilidade
				sensibility.set(numberLayers-2, errorOutputTEMP);
				for (eachLayer = numberLayers-3; eachLayer >= 0; eachLayer--) {
					Double [] tempSensibility = new Double[numberNeurons[eachLayer+1]];
					
					for (int numberNeuronsLayer = numberNeurons[eachLayer+1]-1; numberNeuronsLayer >= 0; numberNeuronsLayer--) {
						double ePowNet = Math.exp(-newNet.get(eachLayer)[numberNeuronsLayer]);
						double derivate = (ePowNet) / (Math.pow( (ePowNet+1) , 2));
//						double derivate = newFnet.get(eachLayer)[numberNeuronsLayer] *(1-newFnet.get(eachLayer)[numberNeuronsLayer]);
						double sumWeightSensibility = 0.0;
						
						for (int i = 0; i < numberNeurons[eachLayer+2]; i++) {
							for (int j = 0; j < numberNeurons[eachLayer+1]; j++) {

								sumWeightSensibility += weight.get(eachLayer+1)[j][i]*sensibility.get(eachLayer+1)[i];
								
							}
						}
						
						tempSensibility[numberNeuronsLayer] =  (derivate*sumWeightSensibility);
						
					}
					sensibility.set(eachLayer, tempSensibility);
					
				}
				
				
				for (eachLayer = 0; eachLayer < numberLayers-1; eachLayer++) {
					
					Double [][] pesosInternos = new Double[numberNeurons[eachLayer]][numberNeurons[eachLayer+1]];
					Double [] biasX = new Double[numberNeurons[eachLayer+1]];
					for (int i = 0; i < numberNeurons[eachLayer+1]; i++) {
						
						for (int j = 0; j < numberNeurons[eachLayer]; j++) {
							if(eachLayer==0){

								pesosInternos[j][i] = (weight.get(eachLayer)[j][i]+
										(alfa*sensibility.get(eachLayer)[i]*input[eachInput][j]));
							}else{
								
								pesosInternos[j][i] = (weight.get(eachLayer)[j][i]+
										(alfa*sensibility.get(eachLayer)[i]*newFnet.get(eachLayer)[i])+
										beta*oldWeight.get(eachLayer)[j][i]);
							}
							
						}
						biasX[i] = bias.get(eachLayer)[i] + alfa*sensibility.get(eachLayer)[i];
						
					}
					bias.set(eachLayer, biasX);
					oldWeight.set(eachLayer, weight.get(eachLayer));
					weight.set(eachLayer, pesosInternos);
					
				}
			
			}
			for (int i = 0; i < numberOutputNeurons; i++) {
//				if(i==0){
//					errorOutputTEMP[i] /= 160.0;
//				}else if(i==1){
//					errorOutputTEMP[i] /= 137.0;
//				}else if(i==2 || i==3){
//					errorOutputTEMP[i] /= 36.0;
//				}else if(i==4){
//					errorOutputTEMP[i] /= 14.0;
//				}
//				
				errorOutput[i] = errorOutputTEMP[i];
//
			}
//			System.out.println("errorOutputTEMP");
//			System.out.println(errorOutputTEMP[0]);
//			System.out.println(errorOutputTEMP[1]);
//			System.out.println(errorOutputTEMP[2]);
//			System.out.println(errorOutputTEMP[3]);
//			System.out.println(errorOutputTEMP[4]);
//			System.out.println();
		}
//		System.out.println();
//		for (int i = 0; i < numberOutputNeurons; i++) {
//			errorOutput[i] = Math.abs(errorOutput[i]);
////			System.out.println(errorOutput[i]);
//
//		}
//		System.out.println("nan    "+error);
//		System.out.println("acabou treinamento");
		return errorOutput;
		
	}




	
	public static void readData(String nomeBase) throws IOException{
		HashMap<Integer, Double> serieTemporal = Common.lerBase(nomeBase);
		media = Common.CalcularMedia(serieTemporal);
		desvio = Common.CalcularDesvioPadrao(serieTemporal);
		File directory = new File("./");
		String macMari = "/src/Bases/";
        BufferedReader dataBR1 = new BufferedReader(new FileReader(new File(directory.getAbsolutePath() + macMari + nomeBase + ".txt")));
        String line = "";

        ArrayList<String[]> dataArr = new ArrayList<String[]>(); 

        while ((line = dataBR1.readLine()) != null) { 
        	String[] value = line.split(";");
        	dataArr.add(value); 
            
        }
        Double [][] saidasIniciais = new Double[dataArr.size()-2][1];
        Double [][] entradasIniciais = new Double[dataArr.size()-2][tamanhoJanela];
        output = new Double[dataArr.size()][1];
        input = new Double[dataArr.size()][1];
        for (int i = 0; i < dataArr.size()-2; i++) {
        	for (int j = 0; j < tamanhoJanela; j++) {
        		if (nomeBase.equals("lynx")){
        			entradasIniciais[i][j] = Math.log(Double.parseDouble(dataArr.get(i+j)[0].replace(',','.'))) ;
        		}else{
        			entradasIniciais[i][j] = ((Double.parseDouble(dataArr.get(i+j)[0].replace(',','.')) - media ) /desvio ) ;
//        			System.out.println(entradasIniciais[i][j]);
        		}
        		//System.out.println(entradasIniciais[i][j]);
			}
        	if (nomeBase.equals("lynx")){
        		saidasIniciais[i][0] = Math.log(Double.parseDouble(dataArr.get(i+tamanhoJanela)[0].replace(',','.'))) ;
    		}else{
    			saidasIniciais[i][0] = ((Double.parseDouble(dataArr.get(i+tamanhoJanela)[0].replace(',','.'))- media ) /desvio ) ;
//    			System.out.println(entradasIniciais[i][j]);
    		}
        	
        }
        
        
        dataBR1.close();
        output = (saidasIniciais);
        input = entradasIniciais;
//        System.out.println(input[0][0]);
//        System.out.println(entradasIniciais[0][0]);
//        System.out.println(entradasIniciais.length);
//        System.out.println(entradasIniciais[0].length);
//        System.out.println();
    }



	public double evaluate(String nomeBase) {
		Output o = Output.getOutputByList(nomeBase);
		Output.getOutputByList(nomeBase).texto[1] = "";
		Output.getOutputByList(nomeBase).texto[2] = "";
		ArrayList<Double[]> newNet = new ArrayList<Double[]>();
		ArrayList<Double[]> newFnet = new ArrayList<Double[]>();
		
		for (int j = 0; j < numberLayers; j++) {
			Double[] x = new Double [numberNeurons[j]];
			for (int k = 0; k < numberNeurons[j]; k++) {
					x[k] = 0.0;
			}
			newNet.add(x);
			newFnet.add(x);
			
		}
		int error = 0;
		
		Output.getOutputByList(nomeBase).texto[2] += (sim+1) + " : " + "\n\n" + "x <- c(";
		// Para cada iteracao
		for (int iteration = 1; iteration <= totalIteration; iteration++) {
			int eachLayer = 0;
			// Para cada exemplo
			Double[]errorOutputTEMP = new Double[numberOutputNeurons];
			for (int i = 0; i < numberOutputNeurons; i++) {
				errorOutputTEMP[i] = 0.0;
			}
			for (int eachInput = (input.length-sizeValidacao); eachInput < (input.length); eachInput++) {
				
				// Calcular o sinal da camada entrada para a primeira camada escondida
				Double []nets = new Double[numberNeurons[1]];
				Double []fnets = new Double[numberNeurons[1]];
				
				for (int eachHiddenNeuron = 0; eachHiddenNeuron < numberNeurons[1]; eachHiddenNeuron++) {
					nets[eachHiddenNeuron] = 0.0;
					nets[eachHiddenNeuron] = bias.get(0)[eachHiddenNeuron];
					Double x = 0.0;
					for (int eachInputRow = 0; eachInputRow < numberNeurons[0]; eachInputRow++) {
						//System.out.println("eachInputRow--------"+eachInputRow+"------"+input[eachInput][eachInputRow]);
						x = (weight.get(0)[eachInputRow][eachHiddenNeuron]*input[eachInput][eachInputRow]);
						nets[eachHiddenNeuron] += x;			
					}
					fnets[eachHiddenNeuron] = (1.0 / (1 + Math.exp(-nets[eachHiddenNeuron])));
				}
				
				// Salvar net e fnet da camada de entrada para camada escondida
				newNet.set(0, nets);
				newFnet.set(0, fnets);
				
				// Calcular o sinal da primeira camada escondida até a saida
				for (eachLayer =1; eachLayer < numberLayers-1; eachLayer++) {
					
					nets = new Double[numberNeurons[eachLayer+1]];
					fnets = new Double[numberNeurons[eachLayer+1]];
			
					for (int eachHiddenNeuronNext = 0; eachHiddenNeuronNext < numberNeurons[eachLayer+1]; eachHiddenNeuronNext++) {
						nets[eachHiddenNeuronNext] = 0.0;
						nets[eachHiddenNeuronNext] = bias.get(eachLayer)[eachHiddenNeuronNext];
		
						for (int eachHiddenNeuronJ = 0; eachHiddenNeuronJ < numberNeurons[eachLayer]; eachHiddenNeuronJ++) {
							nets[eachHiddenNeuronNext] += ((weight.get(eachLayer)[eachHiddenNeuronJ][eachHiddenNeuronNext])*newFnet.get(eachLayer-1)[eachHiddenNeuronJ]);
						
						}
						

						fnets[eachHiddenNeuronNext] = (1.0 / (1 + Math.exp(-nets[eachHiddenNeuronNext])));
					}
					
					// Salvar cada net e fnet 
					newNet.set(eachLayer, nets);
					newFnet.set(eachLayer, fnets);
					
					
				}
				// Como todo sinal chegou a saida, calcularemos o erro 
				for (int i = 0; i < numberOutputNeurons; i++) {
					Double x = (newFnet.get(numberLayers-2)[i]);
					if(x.isNaN()){
						error++;
//						System.out.println("x.isNaN()");
						errorOutputTEMP[i] = 1.0;
					}else{
//						errorOutputTEMP[i] = (output[eachInput][i] - (newFnet.get(numberLayers-2)[i]));			
						errorOutputTEMP[i] = Math.pow(output[eachInput][i] - (newFnet.get(numberLayers-2)[i]),2)/2;	
					}
//					System.out.println(errorOutputTEMP[i]);
					

				}
			}
//			}
			for (int i = 0; i < numberOutputNeurons; i++) {
//				if(i==0){
//					errorOutputTEMP[i] /= 160.0;
//				}else if(i==1){
//					errorOutputTEMP[i] /= 137.0;
//				}else if(i==2 || i==3){
//					errorOutputTEMP[i] /= 36.0;
//				}else if(i==4){
//					errorOutputTEMP[i] /= 14.0;
//				}
//				
				errorOutput[i] = errorOutputTEMP[i];
//
			}
			
			if(nomeBase.equals("lynx")){
				Output.getOutputByList(nomeBase).texto[2] +=  (Math.pow(Math.E, (double) errorOutputTEMP[0]))  +  ", ";
			}else{
				Output.getOutputByList(nomeBase).texto[2] +=  (((double) errorOutputTEMP[0]*desvio) + media)  +  ", ";
			}
			
//			System.out.println("errorOutputTEMP");
//			System.out.println(errorOutputTEMP[0]);
//			System.out.println(errorOutputTEMP[1]);
//			System.out.println(errorOutputTEMP[2]);
//			System.out.println(errorOutputTEMP[3]);
//			System.out.println(errorOutputTEMP[4]);
//			System.out.println();
		}
//		System.out.println();
//		for (int i = 0; i < numberOutputNeurons; i++) {
//			errorOutput[i] = Math.abs(errorOutput[i]);
////			System.out.println(errorOutput[i]);
//
//		}
//		System.out.println("nan    "+error);
//		System.out.println("acabou treinamento");

		
		double []answer = new double[numberOutputNeurons];
		for (int i = 0; i < numberOutputNeurons; i++) {
//			System.out.println(errorOutput[i]);
			if(nomeBase.equals("lynx")){
				answer[i] = Math.pow(Math.E, (double) errorOutput[i]);
    			//System.out.println("------answer---E---"+ answer[i]);
				
			}else{
				answer[i] =  ((double) errorOutput[i]*desvio) + media;
				//System.out.println("------answer---mD---"+ answer[i]);
			}
			
			
		}
		int indiceUltimaVirgula = Output.getOutputByList(nomeBase).texto[2].lastIndexOf(',');
		Output.getOutputByList(nomeBase).texto[2] = Output.getOutputByList(nomeBase).texto[2].substring(0, indiceUltimaVirgula) + ") \n\n";
		
//		System.out.println("------evaluate2");
		return answer[0];
	}
	
	
}
