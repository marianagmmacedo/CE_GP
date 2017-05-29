package br.poli.fineTuning;

import java.io.IOException;
import java.util.ArrayList;

public class FishSchoolSearch {
	
	ArrayList<Fish> population;
	Fish bestFish;
	double stepIndividual;
	double stepVolitive;
	double weightSchool;
	double oldWeightSchool;
	String baseC;
	int simulacao;
	
	public FishSchoolSearch(String b) throws IOException{
		this.stepIndividual = Parameters.stepIndividual;
		this.stepVolitive = Parameters.stepVolitive;
		baseC = b;
		initializeParameters();
		this.bestFish = this.population.get(0);
		updateBestFish();
		
		
	}

	private void updateSteps() {
		this.stepIndividual -= Parameters.stepIndividual/Parameters.numberMaximumIteration;
		this.stepVolitive = 2*this.stepIndividual;
		//Parameters.stepIndividual * Math.pow(Math.E, (-iteracao/Parameters.numberMaximumIteration * 2.0));
		
	}

	private void updateBestFish() {
		for (int eachFish = 1; eachFish < Parameters.numberMaximumPopulation; eachFish++) {
			if(this.bestFish.getFitness() > this.population.get(eachFish).getFitness()){
				this.bestFish = this.population.get(eachFish);
			}
		}	
	}

	private void volitiveCollectiveMovement() throws IOException {
		updateWeightSchool();
		ArrayList<Double> barycenter = calculateBarycenter();
		for (int eachFish = 0; eachFish < Parameters.numberMaximumPopulation; eachFish++) {
			ArrayList<Double> newPosition = new ArrayList<Double>();
			 for (int d = 0; d < Parameters.numberMaximumDimension; d++) {
				 if(this.weightSchool > this.oldWeightSchool){
					 newPosition.add(this.population.get(eachFish).getPosition().get(d)
							 - ((this.population.get(eachFish).getPosition().get(d)-barycenter.get(d))
							 / Functions.euclideanDistance(this.population.get(eachFish).getPosition(),barycenter))
							 );
				 }else{
					 newPosition.add(this.population.get(eachFish).getPosition().get(d)
							 + ((this.population.get(eachFish).getPosition().get(d)-barycenter.get(d))
							 / Functions.euclideanDistance(this.population.get(eachFish).getPosition(),barycenter))
							 );
				 }
		    	if(newPosition.get(d) < Parameters.dimensionMin){
					newPosition.set(d, Parameters.dimensionMin);
		    	}else if(newPosition.get(d) > Parameters.dimensionMax){
		    		newPosition.set(d, Parameters.dimensionMax);
		    	}
			    	
			}
		 	double fit = Functions.calculateFitness(newPosition, baseC, simulacao, false, false);
			if(fit < this.population.get(eachFish).getFitness()){
				this.population.get(eachFish).setFitness(fit);
				this.population.get(eachFish).setPosition(newPosition);
			}
			
		}
		
	}

	private ArrayList<Double> calculateBarycenter() {
		ArrayList<Double> bary = new ArrayList<Double>();
		double den = 0.0;
		for (int d = 0; d < Parameters.numberMaximumDimension; d++) {
			bary.add(0.0);
		}
		for (int eachFish = 0; eachFish < Parameters.numberMaximumPopulation; eachFish++) {
			den += this.population.get(eachFish).getWeight();
			for (int d = 0; d < Parameters.numberMaximumDimension; d++) {
				bary.set(d, bary.get(d)+ this.population.get(eachFish).getPosition().get(d)*this.population.get(eachFish).getWeight());
			}
		}
		for (int d = 0; d < Parameters.numberMaximumDimension; d++) {
			bary.set(d, bary.get(d)/den);
		}
		return bary;
	}

	private void updateWeightSchool() {
		this.oldWeightSchool = this.weightSchool;
		this.weightSchool = 0.0;
		for (int eachFish = 0; eachFish < Parameters.numberMaximumPopulation; eachFish++) {
			this.weightSchool += this.population.get(eachFish).getWeight();
		}
	}

	private void instintiveCollectiveMovement() {
		ArrayList<Double> m = new ArrayList<Double>();
		double den = 0.0;
		for (int d = 0; d < Parameters.numberMaximumDimension; d++) {
			m.add(0.0);
		}
		for (int eachFish = 0; eachFish < Parameters.numberMaximumPopulation; eachFish++) {
			if(this.population.get(eachFish).isGotBetter()){
				den += this.population.get(eachFish).getDeltaFitness();
				for (int d = 0; d < Parameters.numberMaximumDimension; d++) {
					m.set(d, m.get(d)+ this.population.get(eachFish).getDeltaPosition().get(d)*this.population.get(eachFish).getDeltaFitness());
				}
			}
		}
		for (int d = 0; d < Parameters.numberMaximumDimension; d++) {
			m.set(d, m.get(d)/den);
		}
		for (int eachFish = 0; eachFish < Parameters.numberMaximumPopulation; eachFish++) {
			ArrayList<Double> newPosition = new ArrayList<Double>();
			 for (int d = 0; d < Parameters.numberMaximumDimension; d++) {
			    	newPosition.add(this.population.get(eachFish).getPosition().get(d)+m.get(d));
			    	if(newPosition.get(d) < Parameters.dimensionMin){
						newPosition.set(d, Parameters.dimensionMin);
			    	}else if(newPosition.get(d) > Parameters.dimensionMax){
			    		newPosition.set(d, Parameters.dimensionMax);
			    	}
			    	
			}
		}
	}

	private void feeding() {
		double maximumDeltaFitness = calculateMaxDeltaFitness();
		for (int eachFish = 0; eachFish < Parameters.numberMaximumPopulation; eachFish++) {
			if(this.population.get(eachFish).isGotBetter()){
				this.population.get(eachFish).setWeight( this.population.get(eachFish).getWeight() 
						+ (this.population.get(eachFish).getDeltaFitness()/maximumDeltaFitness));
//				if(this.population.get(eachFish).getWeight() > Parameters.maximumWeight){
//					normalizeWeight();
//				}
			}
		}
		
	}

	private double calculateMaxDeltaFitness() {
		double maximum = this.population.get(0).getDeltaFitness();
		for (int eachFish = 1; eachFish < Parameters.numberMaximumPopulation; eachFish++) {
			if(maximum < this.population.get(eachFish).getDeltaFitness()){
				maximum = this.population.get(eachFish).getDeltaFitness();
			}
		}
		return maximum;
	}

	private void individualMovement() throws IOException {
		for (int eachFish = 0; eachFish < Parameters.numberMaximumPopulation; eachFish++) {
			this.population.get(eachFish).setGotBetter(false);
			ArrayList<Double> newPosition = new ArrayList<Double>();
			 for (int d = 0; d < Parameters.numberMaximumDimension; d++) {
			    	newPosition.add(this.population.get(eachFish).getPosition().get(d)
			    			+ this.stepIndividual * Functions.createRandomNumberInRange(1.0, -1.0));
			    	if(newPosition.get(d) < Parameters.dimensionMin){
						newPosition.set(d, Parameters.dimensionMin);
			    	}else if(newPosition.get(d) > Parameters.dimensionMax){
			    		newPosition.set(d, Parameters.dimensionMax);
			    	}
			    	
			}
		 	double fit = Functions.calculateFitness(newPosition, baseC, simulacao, false, false);
			if(fit < this.population.get(eachFish).getFitness()){
				this.population.get(eachFish).setDeltaFitness(this.population.get(eachFish).getFitness()-fit);
				this.population.get(eachFish).setFitness(fit);
				ArrayList<Double> deltaPosition = new ArrayList<Double>();
				for (int d = 0; d < Parameters.numberMaximumDimension; d++) {
					deltaPosition.add(this.population.get(eachFish).getPosition().get(d) - newPosition.get(d));
				}
				this.population.get(eachFish).setDeltaPosition(deltaPosition);
				this.population.get(eachFish).setPosition(newPosition);
				this.population.get(eachFish).setGotBetter(true);
			}
		}
	}

	private void initializeParameters() throws IOException {
		this.population = new ArrayList<Fish>();
		this.weightSchool = 0.0;
		for (int eachFish = 0; eachFish < Parameters.numberMaximumPopulation; eachFish++) {
			this.population.add(new Fish(baseC));
			this.weightSchool += this.population.get(eachFish).getWeight();
		}
		this.oldWeightSchool = this.weightSchool;
		
	}

	public double run(int i) throws IOException {
		simulacao = i;
		for (int iteration = 0; iteration < Parameters.numberMaximumIteration; iteration++) {
			individualMovement();
			feeding();
			instintiveCollectiveMovement();
			volitiveCollectiveMovement();
			updateSteps();
			updateBestFish();
		}
		
		double fit = Functions.calculateFitness(this.bestFish.getPosition(), baseC, simulacao, true, true);
		System.out.println(fit);
		return fit;
	}
}
