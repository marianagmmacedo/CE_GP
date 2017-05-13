package br.poli.fineTuning;

public class Parameters {
	
	// ALL
	public static int numberMaximumSimulation = 1;
	public static int numberMaximumIteration = 100;
	public static int numberMaximumPopulation = 30;
	public static int numberMaximumDimension = 4;
//	public static double dimensionMin = -100.0;
//	public static double dimensionMax = 100.0;
//	public static double dimensionMin = -5.12;
//	public static double dimensionMax = 5.12;
	//public static double dimensionMin = -30.0;
	//public static double dimensionMax = 30.0;
//	public static String function = "sphere";
//	public static String function = "rotated rastrigin";
	//public static String function = "rosenbrock";
	public static double dimensionMin = 0.30;
	public static double dimensionMax = 0.70;
	public static String function = "AG";
		
	// FSS
	public static double stepIndividual = 0.1;
	public static double weightInitial = 2.0;
	public static double maximumWeight = 10.0;
	public static double stepVolitive = 2*stepIndividual;
	
}