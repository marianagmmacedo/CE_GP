package br.poli.fineTuning;

import java.util.ArrayList;
import java.util.Random;

public class Functions {
	
	public static Random random = new Random();
		
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
	
	public static double calculateFitness(ArrayList<Double> position) {
		switch (Parameters.function) {
			case "sphere":
				return calculateSphere(position);
			case "rotated rastrigin":
				return calculateRotatedRastrigin(position);
			case "rosenbrock":
				return calculateRosenbrock(position);
			default:
				break;
		}
		
		return 0.0;
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

