package br.poli.fineTuning;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import br.poli.fineTuning.*;

public class ABC {

	ArrayList<Bee> employedBees;
	int sizeEmployedBees;
	ArrayList<Bee> onLookers;
	int sizeOnlookers;
	ArrayList<Bee> scouts;
	int sizeScouts;
	Bee bestBee;
	Random random;
	ArrayList<Integer> foodSource;
	String dataset;
	
	public ABC(String base) throws IOException{
		this.dataset = base;
		random = new Random();
		this.sizeEmployedBees = (int) Math.floor(Parameters.numberMaximumPopulation*Parameters.numberEmployedBees);
		this.sizeOnlookers =(int) Math.floor(Parameters.numberMaximumPopulation*Parameters.numberOnlookers);
		this.sizeScouts = Parameters.numberMaximumPopulation-(this.sizeEmployedBees+this.sizeOnlookers);
		initializeParameters();
		this.bestBee = this.employedBees.get(0);
		
	}

	private void updateBestSolution() {
		for (int eachOnLookers = 0; eachOnLookers < this.sizeOnlookers; eachOnLookers++) {
			if(bestBee.getFitness() > this.onLookers.get(eachOnLookers).getFitness()){
				bestBee = this.onLookers.get(eachOnLookers);
			}
		}
		for (int eachScout = 0; eachScout < this.sizeScouts; eachScout++) {
			if(bestBee.getFitness() > this.scouts.get(eachScout).getFitness()){
				bestBee = this.scouts.get(eachScout);
			}
		}
		for (int eachEmployed = 0; eachEmployed < this.sizeEmployedBees; eachEmployed++) {
			if(bestBee.getFitness() > this.employedBees.get(eachEmployed).getFitness()){
				bestBee = this.employedBees.get(eachEmployed);
			}
		}
		
	}

	private void scouts() throws IOException {
		
		for (int eachRemain = 0; eachRemain < this.sizeOnlookers; eachRemain++) {
			if(!foodSource.contains(eachRemain)){
				ArrayList<Double> newPosition = new ArrayList<Double>();
				for (int eachDimension = 0; eachDimension < Parameters.numberMaximumDimension; eachDimension++) {
					newPosition.add(Functions.createRandomNumberInRange(Parameters.dimensionMax,Parameters.dimensionMin)
							+(random.nextDouble()* ( maximumFoodSource(eachDimension) - minimumFoodSource(eachDimension) ))
							);
					if(newPosition.get(eachDimension) < Parameters.dimensionMin){
						newPosition.set(eachDimension, Parameters.dimensionMin);
			    	}else if(newPosition.get(eachDimension) > Parameters.dimensionMax){
			    		newPosition.set(eachDimension, Parameters.dimensionMax);
			    	}
				}
				double fit = Functions.calculateFitness(newPosition, dataset, -1, false, false);
				if(fit < this.onLookers.get(eachRemain).getFitness()){
					this.onLookers.get(eachRemain).setFitness(fit);
					this.onLookers.get(eachRemain).setPosition(newPosition);
					foodSource.add(eachRemain);
				}
			}
			
		}
			
		
		
	}

	private double minimumFoodSource(int eachDimension) {
		double min = Double.MAX_VALUE;
		for (int eachOnLookers = 0; eachOnLookers < this.sizeOnlookers; eachOnLookers++) {
			if(min > this.onLookers.get(eachOnLookers).getPosition().get(eachDimension)){
				min = this.onLookers.get(eachOnLookers).getPosition().get(eachDimension);
			}
		}
		return min;
	}
	
	private double maximumFoodSource(int eachDimension) {
		double max = Double.MAX_VALUE;
		for (int eachOnLookers = 0; eachOnLookers < this.sizeOnlookers; eachOnLookers++) {
			if(max < this.onLookers.get(eachOnLookers).getPosition().get(eachDimension)){
				max = this.onLookers.get(eachOnLookers).getPosition().get(eachDimension);
			}
		}
		return max;
	}

	private void onlookersToFoodSource() throws IOException {
		foodSource = new ArrayList<Integer>();
		for (int eachOnLookers = 0; eachOnLookers < this.sizeOnlookers; eachOnLookers++) {
			int rand = random.nextInt(this.sizeEmployedBees);
			if(random.nextDouble() < this.employedBees.get(rand).getProbability() && !foodSource.contains(eachOnLookers)){
				ArrayList<Double> newPosition = new ArrayList<Double>();
				for (int d = 0; d < Parameters.numberMaximumDimension; d++) {
					newPosition.add(d, this.onLookers.get(eachOnLookers).getPosition().get(d) + 
							(Functions.createRandomNumberInRange(1.0,-1.0)*
									(this.onLookers.get(eachOnLookers).getPosition().get(d)-this.employedBees.get(rand).getPosition().get(d)
											)));
					if(newPosition.get(d) < Parameters.dimensionMin){
						newPosition.set(d, Parameters.dimensionMin);
			    	}else if(newPosition.get(d) > Parameters.dimensionMax){
			    		newPosition.set(d, Parameters.dimensionMax);
			    	}
				}
				double fit = Functions.calculateFitness(newPosition, this.dataset, -1, false, false);
				if(fit < this.onLookers.get(eachOnLookers).getFitness()){
					this.onLookers.get(eachOnLookers).setFitness(fit);
					this.onLookers.get(eachOnLookers).setPosition(newPosition);
					foodSource.add(eachOnLookers);
				}
				
			}
			
		}
		
		
	}

	private void probability() {
		double probability = 0.0;
		for (int eachEmployed = 0; eachEmployed < this.sizeEmployedBees; eachEmployed++) {
			probability += this.employedBees.get(eachEmployed).getFitness();
		}
		for (int eachEmployed = 0; eachEmployed < this.sizeEmployedBees; eachEmployed++) {
			this.employedBees.get(eachEmployed).setProbability( this.employedBees.get(eachEmployed).getFitness()/probability );
		}
	}

	private void employedBeesNewSolution() throws IOException {
		
		for (int eachEmployed = 0; eachEmployed < this.sizeEmployedBees; eachEmployed++) {
			for (int eachEmployed2 = 0; eachEmployed2 < this.sizeEmployedBees; eachEmployed2++) {
				ArrayList<Double> newPosition = new ArrayList<Double>();
				for (int eachDimension = 0; eachDimension < Parameters.numberMaximumDimension; eachDimension++) {
					newPosition.add(this.employedBees.get(eachEmployed).getPosition().get(eachDimension) 
							+ (Functions.createRandomNumberInRange(1.0,-1.0)*
									(this.employedBees.get(eachEmployed).getPosition().get(eachDimension)-this.employedBees.get(eachEmployed2).getPosition().get(eachDimension)
											)));
					if(newPosition.get(eachDimension) < Parameters.dimensionMin){
						newPosition.set(eachDimension, Parameters.dimensionMin);
			    	}else if(newPosition.get(eachDimension) > Parameters.dimensionMax){
			    		newPosition.set(eachDimension, Parameters.dimensionMax);
			    	}
				}
				double fit = Functions.calculateFitness(newPosition, this.dataset, -1, false, false);
				if(fit < this.employedBees.get(eachEmployed).getFitness()){
					this.employedBees.get(eachEmployed).setFitness(fit);
					this.employedBees.get(eachEmployed).setPosition(newPosition);
				}
			}
		}
	}

	private void initializeParameters() throws IOException {
		this.employedBees = new ArrayList<Bee>();
		this.onLookers = new ArrayList<Bee>();
		this.scouts = new ArrayList<Bee>();
		for (int eachEmployed = 0; eachEmployed < this.sizeEmployedBees; eachEmployed++) {
			this.employedBees.add(new Bee(this.dataset));
		}
		for (int eachOnlookers = 0; eachOnlookers < this.sizeOnlookers; eachOnlookers++) {
			this.onLookers.add(new Bee(this.dataset));
		}
		for (int eachScouts = 0; eachScouts < this.sizeScouts; eachScouts++) {
			this.scouts.add(new Bee(this.dataset));
		}
		
		
	}

	public double run(int i) throws IOException {
		for (int iteration = 0; iteration < Parameters.numberMaximumIteration; iteration++) {
//			System.out.println("INICIO");
			employedBeesNewSolution();
//			System.out.println("employedBeesNewSolution();");
			probability();
//			System.out.println("probability();");
			onlookersToFoodSource();
//			System.out.println("onlookersToFoodSource();");
			scouts();
//			System.out.println("scouts();");
			updateBestSolution();
//			System.out.println("updateBestSolution();");
			System.out.println(this.bestBee.getFitness());
		}
		return this.bestBee.getFitness();
	}
	
	
}
