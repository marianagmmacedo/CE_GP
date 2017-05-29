package br.poli.fineTuning;

public class Parameters {
	
	// ALL
	public static int numberMaximumSimulation = 1;
	public static int numberMaximumIteration = 60;//60
	public static int numberMaximumPopulation = 30;
	public static int numberMaximumDimension = 4; //4,6
//	public static double dimensionMin = -100.0;
//	public static double dimensionMax = 100.0;
//	public static double dimensionMin = -5.12;
//	public static double dimensionMax = 5.12;
	//public static double dimensionMin = -30.0;
	//public static double dimensionMax = 30.0;
//	public static String function = "sphere";
//	public static String function = "rotated rastrigin";
	//public static String function = "rosenbrock";
//	public static double dimensionMin = 0.30;
//	public static double dimensionMax = 0.70;
//	public static double dimensionMin = 0.000001;
//	public static double dimensionMax = 1.0;
//	public static String function = "MLP";
	public static double dimensionMin = 0.30;
	public static double dimensionMax = 0.70;
	public static String function = "GP";
		
	// FSS
	public static double stepIndividual = 0.4;
	public static double weightInitial = 2.0;
	public static double maximumWeight = 10.0;
	public static double stepVolitive = 2*stepIndividual;
	public static boolean hasInput = false;
	
	// PSO
	//public static String topology = "global";
    public static String topology = "local";
//		public static String topology = "focal";
	public static int numberNeigbors = 2;
	public static double velocityMax = 5.0;
	public static double velocityMin = -5.0;
	public static double w = 0.8;
	public static double wInitial = 0.9;
	public static double wFinal = 0.4;
	public static boolean nonW = false;
	public static boolean decrementW = true;
	public static boolean clerc = false;
	public static double c1 = 2.05;
	public static double c2 = 2.05;
	//public static String function = "sphere";
//	public static String function = "rotated rastrigin";
	//public static String function = "rosenbrock";
	
	// FA
	public static double brightnessInitial = 0.4;
	public static double alpha = 1.0;
	public static double gamma = 1.0;
	
	// ABC
	public static double numberEmployedBees = 0.5;
	public static double numberOnlookers = 0.5;
	public static double numberScouts = 0.2;
}
