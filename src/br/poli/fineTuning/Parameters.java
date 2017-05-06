package br.poli.fineTuning;

public class Parameters {
	
	// ALL
	public static int numberMaximumSimulation = 1;
	public static int numberMaximumIteration = 10000;
	public static int numberMaximumPopulation = 30;
	public static int numberMaximumDimension = 30;
//	public static double dimensionMin = -100.0;
//	public static double dimensionMax = 100.0;
//	public static double dimensionMin = -5.12;
//	public static double dimensionMax = 5.12;
	public static double dimensionMin = -30.0;
	public static double dimensionMax = 30.0;
//	public static String function = "sphere";
//	public static String function = "rotated rastrigin";
	public static String function = "rosenbrock";
		
	// FSS
	public static double stepIndividual = 0.4;
	public static double weightInitial = 2.0;
	public static double maximumWeight = 10.0;
	public static double stepVolitive = 2*stepIndividual;
	
}
