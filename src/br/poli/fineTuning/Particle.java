package br.poli.fineTuning;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Particle {
	
	private ArrayList<Double> position;
	private ArrayList<Double> velocity;
	private double fitness;
	private Pbest pbest;
	
	public Particle(String base) throws IOException{
//		System.out.println("Particle");
		position = new ArrayList<Double>();
		velocity = new ArrayList<Double>();
		for (int d = 0; d < Parameters.numberMaximumDimension; d++) {
			position.add(Functions.createRandomNumberInRange(Parameters.dimensionMax,Parameters.dimensionMin));
			velocity.add(Functions.createRandomNumberInRange(Parameters.velocityMax,Parameters.velocityMin));
		}
		fitness = Functions.calculateFitness(position, base, -1, false, false);
		setPbest(new Pbest(position, fitness));
	}
	
	
	
	public ArrayList<Double> getPosition() {
		return position;
	}

	public void setPosition(ArrayList<Double> position) {
		this.position = position;
	}

	public ArrayList<Double> getVelocity() {
		return velocity;
	}

	public void setVelocity(ArrayList<Double> velocity) {
		this.velocity = velocity;
	}



	public double getFitness() {
		return fitness;
	}



	public void setFitness(double fitness) {
		this.fitness = fitness;
	}



	public Pbest getPbest() {
		return pbest;
	}



	public void setPbest(Pbest pbest) {
		this.pbest = pbest;
	}


	
	
}
