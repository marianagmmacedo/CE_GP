package br.poli.fineTuning;

import java.io.IOException;
import java.util.ArrayList;

public class FireflyAlgorithm {
	
	ArrayList<Firefly> population;
	Firefly bestFirefly;
	String dataset;
	
	public FireflyAlgorithm(String base) throws IOException{
			dataset = base;
			initializePopulation();
			this.bestFirefly = this.population.get(0);
			updateBest();
			
			
		
	}
	

	private void updateBest() {
		for (int eachFirefly = 0; eachFirefly < Parameters.numberMaximumPopulation; eachFirefly++) {
			if(this.bestFirefly.fitness > this.population.get(eachFirefly).fitness){
				this.bestFirefly = this.population.get(eachFirefly);
			}
		}
	}

	private double distanceEuclidean(int j, int i) {
		double rji = 0.0;
		for (int dimension = 0; dimension < Parameters.numberMaximumDimension; dimension++) {
			rji += (Math.pow(this.population.get(j).position.get(dimension)-this.population.get(i).position.get(dimension),2));
		}
		return rji;
	}

	private void initializePopulation() throws IOException {
		this.population = new ArrayList<Firefly>();
		for (int eachFirefly = 0; eachFirefly < Parameters.numberMaximumPopulation; eachFirefly++) {
			this.population.add(new Firefly(dataset));
		}
		
	}
	
	public double run(int simulation) throws IOException{
		for (int iteration = 0; iteration < Parameters.numberMaximumIteration; iteration++) {
			for (int i = 0; i < Parameters.numberMaximumPopulation; i++) {
				for (int j = 0; j < Parameters.numberMaximumPopulation; j++) {
					if(this.population.get(j).fitness < this.population.get(i).fitness){
						ArrayList<Double> newPosition = new ArrayList<Double>();
						double rij = distanceEuclidean(j,i);
						this.population.get(i).brightness *= Math.pow(Math.E, (-1*Parameters.gamma*Math.pow(rij, 2)));
					    for (int d = 0; d < Parameters.numberMaximumDimension; d++) {
					    	newPosition.add(this.population.get(i).position.get(d) +
					    			(this.population.get(i).brightness*(this.population.get(i).position.get(d)-this.population.get(i).position.get(d)))+
					    			Parameters.alpha*(Math.random()-0.5));
					    	if(newPosition.get(d) < Parameters.dimensionMin){
								newPosition.set(d, Parameters.dimensionMin);
					    	}else if(newPosition.get(d) > Parameters.dimensionMax){
					    		newPosition.set(d, Parameters.dimensionMax);
					    	}
					    	
						}
					    
					    double fit = Functions.calculateFitness(newPosition, dataset,-1, false, false);
						if(fit < this.population.get(i).fitness){
							this.population.get(i).fitness = fit;
							this.population.get(i).position = newPosition;
						}
								
					}else{
						ArrayList<Double> newPosition = new ArrayList<Double>();
						for (int d = 0; d < Parameters.numberMaximumDimension; d++) {
							newPosition.add(d, this.population.get(i).position.get(d)*Math.random());
							if(newPosition.get(d) < Parameters.dimensionMin){
								newPosition.set(d, Parameters.dimensionMin);
					    	}else if(newPosition.get(d) > Parameters.dimensionMax){
					    		newPosition.set(d, Parameters.dimensionMax);
					    	}
						}
						
						double fit = Functions.calculateFitness(newPosition, this.dataset,-1, false, false);
						if(fit < this.population.get(i).fitness){
							this.population.get(i).fitness = fit;
							this.population.get(i).position = newPosition;
						}
					}
					
				}
			}
			updateBest();
			System.out.println(this.bestFirefly.fitness);
		}
		return (this.bestFirefly.fitness);
	}
}
