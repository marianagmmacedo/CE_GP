package br.poli.fineTuning;

import java.io.IOException;
import java.util.ArrayList;

public class Bee {

	private ArrayList<Double> position;
	private double probability;
	private double fitness;
	
	public Bee(String b) throws IOException{
		this.position = new ArrayList<Double>();
		for (int dimensions = 0; dimensions < Parameters.numberMaximumDimension; dimensions++) {
			this.position.add(Functions.createRandomNumberInRange(Parameters.dimensionMax,Parameters.dimensionMin));
		}
		this.setFitness(Functions.calculateFitness(this.position, b, -1, false, false));
	}
	public ArrayList<Double> getPosition() {
		return position;
	}

	public void setPosition(ArrayList<Double> position) {
		this.position = position;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	public double getProbability() {
		return probability;
	}
	public void setProbability(double probability) {
		this.probability = probability;
	}
}
