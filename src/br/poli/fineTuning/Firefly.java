package br.poli.fineTuning;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Firefly {

	ArrayList<Double> position;
	double brightness;
	double fitness;
	static Random random;
	
	
	public Firefly(String b) throws IOException{
		fitness = Double.MAX_VALUE;
		random = new Random();
		this.position = new ArrayList<Double>();
		this.brightness = Parameters.brightnessInitial;
		for (int dimensions = 0; dimensions < Parameters.numberMaximumDimension; dimensions++) {
			this.position.add(Functions.createRandomNumberInRange(Parameters.dimensionMax,Parameters.dimensionMin));
		}
		this.fitness = Functions.calculateFitness(this.position, b, -1, false, false);
				
	}
	

}
