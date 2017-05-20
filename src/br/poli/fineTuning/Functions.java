package br.poli.fineTuning;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import br.poli.gp.AlgoritmoGP;
import br.poli.gp.Common;
import br.poli.gp.EInicializacao;
import br.poli.gp.Parametros;
import br.poli.mlp.MultiLayerPerceptron;

public class Functions {
	
	public static Random random = new Random();
	public static int iteration = 0;
	
	public static double createRandomNumberInRange(double max, double min){
		return min + ( max - min) * random.nextDouble();
	}
	
	public static double euclideanDistance(ArrayList<Double> x, ArrayList<Double> y) {
		double sum = 0.0;
		for (int dim = 0; dim < Parameters.numberMaximumDimension; dim++) {
			sum += Math.pow(x.get(dim) - y.get(dim), 2);
		}
		return sum;
	}
	
	public static double calculateFitness(ArrayList<Double> position, String base, int simulation, boolean save) throws IOException {
		switch (Parameters.function) {
			case "sphere":
				return calculateSphere(position);
			case "rotated rastrigin":
				return calculateRotatedRastrigin(position);
			case "rosenbrock":
				return calculateRosenbrock(position);
			case "GP":
				return calculatePG(position, base);
			case "MLP":
				return calculateMLP(position, base, simulation, save);
			default:
				break;
		}
		
		return 0.0;
	}
	
	private static double calculateMLP(ArrayList<Double> position, String b, int sim, boolean save) throws IOException {
		//System.out.println(Parametros.Base);
		HashMap<Integer, Double> serieTemporal = Common.lerBase(b);
		int validarBase = (int) Math.ceil(serieTemporal.size()*Parametros.TAXA_VALIDACAO);
		MultiLayerPerceptron mlp = new MultiLayerPerceptron(b, validarBase, position, sim);
		mlp.forwardBackward();		
		return mlp.evaluate(b, save);
	}
	
	private static double calculatePG(ArrayList<Double> position, String b) throws IOException {
		//System.out.println(Parametros.Base);
		HashMap<Integer, Double> serieTemporal = Common.lerBase(b);
		double media = Common.CalcularMedia(serieTemporal);
		double desvio = Common.CalcularDesvioPadrao(serieTemporal);
		if (b.equals("lynx"))
			Common.NormalizarLN(serieTemporal);
		else
			Common.Normalizar2(serieTemporal);
		
		AlgoritmoGP gp = new AlgoritmoGP(EInicializacao.Completa, serieTemporal, Math.abs(position.get(0)),
				(int) Math.abs(Math.floor(position.get(1)*Parametros.NUMERO_TOTAL_FUNCAO)), 
				(int) Math.abs(position.get(2)*Parametros.TAMANHO_MAXIMO_PROFUNDIDADE_ARVORE), 
				(int) Math.abs(Math.floor(position.get(3)*Parametros.NUMERO_MAXIMO_POPULACAO)), 
				media, desvio, b);
				
		iteration++;
		return gp.runGP(iteration);
	}

	private static double calculateSphere(ArrayList<Double> x) {
		double sphere = 0.0;
		for (int d = 0; d < Parameters.numberMaximumDimension; d++) {
			sphere += Math.pow(x.get(d), 2);
		}
//		System.out.println(sphere);
		return sphere;
	}

	private static double calculateRotatedRastrigin(ArrayList<Double> x) {
		double rotatedRastrigin = 10*Parameters.numberMaximumDimension;
		for (int d = 0; d < Parameters.numberMaximumDimension; d++) {
			rotatedRastrigin += Math.pow(x.get(d), 2) - 10*Math.cos(2*Math.PI*x.get(d)) + 10;	  
		}
		return rotatedRastrigin;
	}

	private static double calculateRosenbrock(ArrayList<Double> x) {
		double rosenbrock = 0.0;
		for (int d = 0; d < Parameters.numberMaximumDimension-1; d++) {
//			rosenbrock += 100*Math.pow( (( x[d+1] - Math.pow(x[d], 2) ) ), 2) + Math.pow((x[d]-1),2);
			rosenbrock += (100*(Math.pow(Math.pow(x.get(d), 2) - x.get(d+1), 2)) + (Math.pow(x.get(d) - 1, 2)));
		}
		return rosenbrock;
	}
	
}

