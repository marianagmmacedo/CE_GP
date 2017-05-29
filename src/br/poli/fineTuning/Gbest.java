package br.poli.fineTuning;

import java.util.ArrayList;

public class Gbest {
	
	private ArrayList<Double> bestPosition;
	private double bestFitness;
	
	public Gbest(ArrayList<Double> position, double calculateFitness) {
		this.bestFitness = calculateFitness;
		this.bestPosition = position;
	}
	
	public ArrayList<Double> getBestPosition() {
		return bestPosition;
	}
	public void setBestPosition(ArrayList<Double> bestPosition) {
		this.bestPosition = bestPosition;
	}
	public double getBestFitness() {
		return bestFitness;
	}
	public void setBestFitness(double bestFitness) {
		this.bestFitness = bestFitness;
	}
	
}
