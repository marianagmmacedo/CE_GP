package br.poli.gp.util;

public class VectorHandler {

	public static double calculateVectorModule(double vector[]){
		
		double squaredSum = 0;
		
		for (int i = 0; i < vector.length; i++) {
			squaredSum += Math.pow(vector[i], 2.0);
		}
		return Math.sqrt(squaredSum);
	}
	
	public static double calculateMatrixModule(double matrix[][]){
		
		double sum = 0;
		
		for (int i = 0; i < matrix.length; i++) {
			sum += calculateVectorModule(matrix[i]);
		}
		return sum/matrix.length;
	}
}
