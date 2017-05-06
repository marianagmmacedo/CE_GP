package br.poli.fineTuning;

import java.util.ArrayList;
import br.poli.fineTuning.*;

public class Fish {
	
	private ArrayList<Double> position;
	private ArrayList<Double> deltaPosition;
	private double weight;
	private double fitness;
	private double deltaFitness;
	private boolean gotBetter;
	
	public Fish(){
		this.position = new ArrayList<Double>();
		this.setWeight(Parameters.weightInitial);
		for (int dimensions = 0; dimensions < Parameters.numberMaximumDimension; dimensions++) {
			this.position.add(Functions.createRandomNumberInRange(Parameters.dimensionMax,Parameters.dimensionMin));
		}
		this.setFitness(Functions.calculateFitness(this.position));
		this.setGotBetter(false);
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

	public ArrayList<Double> getDeltaPosition() {
		return deltaPosition;
	}

	public void setDeltaPosition(ArrayList<Double> deltaPosition) {
		this.deltaPosition = deltaPosition;
	}

	public double getDeltaFitness() {
		return deltaFitness;
	}

	public void setDeltaFitness(double deltaFitness) {
		this.deltaFitness = deltaFitness;
	}

	public boolean isGotBetter() {
		return gotBetter;
	}

	public void setGotBetter(boolean gotBetter) {
		this.gotBetter = gotBetter;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	
	
}
