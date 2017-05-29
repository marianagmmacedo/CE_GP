package br.poli.fineTuning;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class PSO {
	
	private ArrayList<Particle> swarm;
	private Gbest gbest;
	private int focalParticle;
	Random random;
	public double wPSO;
	public double clerc;
	String dataset;
	
	public PSO(String b) throws IOException{
//		System.out.println("PSO");
		random = new Random();
		this.dataset = b;
		focalParticle = random.nextInt(Parameters.numberMaximumPopulation);
		this.swarm = new ArrayList<Particle>();
		for (int s = 0; s < Parameters.numberMaximumPopulation; s++) {
			this.swarm.add(new Particle(b));
		}		
		gbest = new Gbest(this.swarm.get(0).getPosition(), this.swarm.get(0).getFitness());
		if(Parameters.nonW){
			wPSO = Parameters.w;
			clerc = 1.0;
		}else if(Parameters.decrementW){
			wPSO = Parameters.wInitial;
			clerc = 1.0;
		}else if(Parameters.clerc){
			wPSO = 1.0;
			clerc = 0.73;
			
		}
	
		
		
	}
	

	private void updatePosition() throws IOException {
		for (int s = 0; s < Parameters.numberMaximumPopulation; s++) {
			ArrayList<Double> position = calculateNewPosition(this.swarm.get(s));		 
			this.swarm.get(s).setPosition(position);
			this.swarm.get(s).setFitness(Functions.calculateFitness(position, this.dataset, -1, false, false));
			
			if(this.swarm.get(s).getPbest().getBestFitness() > this.swarm.get(s).getFitness()){
				this.swarm.get(s).getPbest().setBestFitness(this.swarm.get(s).getFitness());
				this.swarm.get(s).getPbest().setBestPosition(this.swarm.get(s).getPosition());
			}
		}
	}

	private ArrayList<Double> calculateNewPosition(Particle bird) {
		ArrayList<Double> oldPosition = bird.getPosition();
		for (int d = 0; d < Parameters.numberMaximumDimension; d++) {
			oldPosition.add(bird.getPosition().get(d) + bird.getVelocity().get(d));
			if(oldPosition.get(d) > Parameters.dimensionMax){
				oldPosition.set(d, Parameters.dimensionMax);
			}else if(oldPosition.get(d) < Parameters.dimensionMin){
				oldPosition.set(d, Parameters.dimensionMin);
			}
		}
		return oldPosition;
	}


	private void updateVelocity() {		
		for (int s = 0; s < Parameters.numberMaximumPopulation; s++) {
			ArrayList<Double> velocity = calculateNewVelocity(s);		 
			this.swarm.get(s).setVelocity(velocity);
		}
	}

	private ArrayList<Double> calculateNewVelocity(int bird){
		ArrayList<Double> oldVelocity = this.swarm.get(bird).getVelocity();
		ArrayList<Double> gbest = calculateGbestPosition(bird);
		for (int d = 0; d < Parameters.numberMaximumDimension; d++) {
			oldVelocity.set(d, oldVelocity.get(d)*wPSO 
					+ Parameters.c1*random.nextDouble()*(this.swarm.get(bird).getPbest().getBestPosition().get(d)- this.swarm.get(bird).getPosition().get(d))
					+ Parameters.c2*random.nextDouble()*(gbest.get(d) - this.swarm.get(bird).getPosition().get(d))
					
					);
			oldVelocity.set(d, clerc*oldVelocity.get(d));
			if(oldVelocity.get(d) > Parameters.velocityMax){
				oldVelocity.set(d, Parameters.velocityMax);
			}else if(oldVelocity.get(d) < Parameters.velocityMin){
				oldVelocity.set(d, Parameters.velocityMin);
			}
		}
		return oldVelocity;
	}

	private ArrayList<Double> calculateGbestPosition(int iB) {
		switch (Parameters.topology) {
			case "global":
				return gbest.getBestPosition();
			case "local":
				return calculateLocalPosition(iB);
			case "focal":
				return calculateFocalPosition(iB);
			default:
				break;
		}
		return new ArrayList<Double>();
	}

	private ArrayList<Double> calculateLocalPosition(int birdI) {
		ArrayList<Particle> swarmS = new ArrayList<Particle>();
		ArrayList<Double> bestPos = new ArrayList<Double>();
		double bestFit = Double.MAX_VALUE; 
		for (int s = 0; s < Parameters.numberMaximumPopulation-1; s++) {
			if(s != birdI){
				swarmS.add(this.swarm.get(s));
			}
			
		}
		
		bestPos = this.swarm.get(birdI).getPbest().getBestPosition();
		bestFit = this.swarm.get(birdI).getPbest().getBestFitness();
		
		for (int n = 0; n < Parameters.numberNeigbors; n++) {
			int ind = n+1;
			if(ind == Parameters.numberNeigbors){
				ind = 0;
			}
//					getMinimumPosition(swarmS, this.swarm[birdI]);
			if (bestFit > swarmS.get(ind).getPbest().getBestFitness()){
				bestPos = swarmS.get(ind).getPbest().getBestPosition();
				bestFit = swarmS.get(ind).getPbest().getBestFitness();
			}
			
		}
		
		return bestPos;
	}
	
	private int getMinimumPosition(ArrayList<Particle> birds, Particle bI) {
		double min = Functions.euclideanDistance(bI.getPosition(), birds.get(0).getPbest().getBestPosition());
		int index = 0;
		for (int b = 1; b < birds.size(); b++) {
			if(min > Functions.euclideanDistance(bI.getPosition(), birds.get(b).getPbest().getBestPosition() )){
				min = Functions.euclideanDistance(bI.getPosition(), birds.get(b).getPbest().getBestPosition());
				index = b;
			}
		}
		return index;
	}


	


	private ArrayList<Double> calculateFocalPosition(int f) {
		ArrayList<Double> bestPos = new ArrayList<Double>();
		if(f == focalParticle){
			for (int s = 0; s < Parameters.numberMaximumPopulation; s++) {
				if(this.swarm.get(focalParticle).getPbest().getBestFitness() > this.swarm.get(s).getPbest().getBestFitness()){
					bestPos = this.swarm.get(s).getPbest().getBestPosition();
				}
			}
		}else{
			if(this.swarm.get(f).getPbest().getBestFitness() > this.swarm.get(focalParticle).getPbest().getBestFitness()){
				bestPos = this.swarm.get(focalParticle).getPbest().getBestPosition();
			}
		}
		return bestPos;
	}



	private void updateGbest() {
		
		for (int s = 0; s < Parameters.numberMaximumPopulation; s++) {
			if(gbest.getBestFitness() > this.swarm.get(s).getPbest().getBestFitness()){
				gbest.setBestFitness(this.swarm.get(s).getPbest().getBestFitness());
				gbest.setBestPosition(this.swarm.get(s).getPbest().getBestPosition());
			}
		}
			
	}
	
	private void updatePbest() {
		
		for (int s = 0; s < Parameters.numberMaximumPopulation; s++) {
			if(this.swarm.get(s).getPbest().getBestFitness() > this.swarm.get(s).getFitness()){
				this.swarm.get(s).getPbest().setBestFitness(this.swarm.get(s).getFitness());
				this.swarm.get(s).getPbest().setBestPosition(this.swarm.get(s).getPosition());
			}
		}
			
	}


	public void run(int simulation) throws IOException {
		for (int iteration = 0; iteration < Parameters.numberMaximumIteration; iteration++) {
			updateVelocity();
			updatePosition();
			updatePbest();
			updateGbest();
			if(Parameters.decrementW){
				wPSO -=  (Parameters.wInitial-Parameters.wFinal)/Parameters.numberMaximumIteration;
			}
			System.out.println(gbest.getBestFitness());
		}
	}
	
	
}
