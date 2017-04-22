package br.poli.gp;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.importance.PerturbationFeatureImportanceCalc;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

import ChartDirector.ChartViewer;
import br.poli.gp.arvore.Arvore;
import br.poli.gp.arvore.Funcao;
import br.poli.gp.util.DemoModule;
import br.poli.gp.util.LineChart;

public class AlgoritmoGP {
	//jmathplot
	ArrayList<Individuo> populacao;
	Individuo melhorIndividuo;
	// OTIMIZAR MELHORINDIVIDUO c resilient backpropagation
	//     <X     , Y     >
	Map<Integer, Double> serieTemporal;


	public AlgoritmoGP(EInicializacao tipoInicializacao, HashMap<Integer, Double> serieTemporal) throws IOException{
		populacao = new ArrayList<Individuo>();
		this.serieTemporal = serieTemporal;
		for (int cadaIndividuo = 0; cadaIndividuo < Parametros.NUMERO_TOTAL_INDIVIDUO; cadaIndividuo++) {
			populacao.add(new Individuo(tipoInicializacao));
		}
	}

	public AlgoritmoGP(int profundidadeIndividuo, HashMap<Integer, Double> serieTemporal) throws IOException{
		populacao = new ArrayList<Individuo>();
		this.serieTemporal = serieTemporal;

		for (int cadaIndividuo = 0; cadaIndividuo < Parametros.NUMERO_TOTAL_INDIVIDUO; cadaIndividuo++) {
			populacao.add(new Individuo(profundidadeIndividuo));
		}
	}

	public void runGP() throws IOException {

		double[] getFit = new double[Parametros.NUMERO_TOTAL_ITERACAO];
		for (int iteracao = 0; iteracao < Parametros.NUMERO_TOTAL_ITERACAO; iteracao++) {
			reproduzir();
			calcularFitnessPopulacao();
			removerMenosAdaptados();
			
			Individuo melhorIndividuo = this.melhorIndividuo;
			atualizarMelhorIndividuo();
			
			//Caso o indivúduo tenha sido alterado
			if (melhorIndividuo != this.melhorIndividuo){
				otimizarMelhorIndividuo();				
			}
			getFit[iteracao] = melhorIndividuo.fitness;
			System.out.println(getFit[iteracao]);
		}
		showFitness(getFit);
	}

	private void removerMenosAdaptados() {
		populacao.sort((i1, i2) -> Double.compare(i1.fitness, i2.fitness));
		while (populacao.size() > Parametros.NUMERO_MAXIMO_POLPULACAO){
			populacao.subList((int)(populacao.size()/2), populacao.size()).clear();
		}
	}

	private void reproduzir() {
		// restrigir a arvore resposta
		int tamanhoPopulacao = populacao.size();
		for (int individuoIndex = 0; individuoIndex < tamanhoPopulacao; individuoIndex++) {
			Individuo i = populacao.get(individuoIndex);

			if(Common.RANDOM.nextDouble() > Parametros.TAXA_CRUZAMENTO_MUTACAO){
				gerarDescendentes(i, populacao.get(Common.RANDOM.nextInt(populacao.size())));
			}else{
				//Mutaï¿½ï¿½o sï¿½ ocorre se o individuo nï¿½o for o melhor, para evitar perda de informaï¿½ï¿½es
				if (i!=melhorIndividuo)
					mutarDescendente(i);
			}
		}		
	}

	private void mutarDescendente(Individuo individuo) {		
		if(individuo.noFuncao.size() != 0){
			individuo.fitnessJaCalculado = false;
			int noMutacaoFilho = Common.RANDOM.nextInt((individuo.noFuncao.size()));
			Funcao fFilho = individuo.noFuncao.get(noMutacaoFilho);
			fFilho.crossover((new Individuo(EInicializacao.Mutacao)).arvore.no);
		}
	}

	private void gerarDescendentes(Individuo pai, Individuo mae){
		Individuo filho = (Individuo) Common.DeepCopy(pai);
		Individuo filha = (Individuo) Common.DeepCopy(mae);

		if(filho.noFuncao.size() != 0 && filha.noFuncao.size() != 0){
			int noCrossOverFilho = Common.RANDOM.nextInt(filho.noFuncao.size());
			int noCrossOverFilha = Common.RANDOM.nextInt(filha.noFuncao.size());

			Funcao fFilho = filho.noFuncao.get(noCrossOverFilho);
			Funcao fFilha = filha.noFuncao.get(noCrossOverFilha);

			fFilho.crossover(fFilha);

			filho.otimizarArvore();
			filha.otimizarArvore();

			populacao.add(filho);
			populacao.add(filha);
		}
	}

	private void calcularFitnessIndividuo(HashMap<String, Double> hm, Individuo i) {

		double fitness = 0.0;

		for(int walk = 0; walk < (serieTemporal.size()-Parametros.NUMERO_TOTAL_VARIAVEL); walk++){
			int aux = 0;
			for (Double numeroJanela = 0.0; numeroJanela < Parametros.NUMERO_TOTAL_VARIAVEL; numeroJanela++) {
				hm.replace("X"+numeroJanela.intValue(), serieTemporal.get(walk+aux));
				aux++;
			}
			fitness += Math.pow(serieTemporal.get(walk+Parametros.NUMERO_TOTAL_VARIAVEL) - i.calcularValor(hm), 2);
		}

		i.fitness = fitness/serieTemporal.size();
		i.fitnessJaCalculado = true;
	}

	private void calcularFitnessPopulacao() {
		HashMap<String, Double> hm = new HashMap<String, Double>();
		
		for (int numeroJanela = 0; numeroJanela < Parametros.NUMERO_TOTAL_VARIAVEL; numeroJanela++) {
			hm.put("X"+numeroJanela, 0d);
		}
		
		for(Individuo i : populacao){
			if (!i.fitnessJaCalculado){
				calcularFitnessIndividuo(hm, i);
			}
		}
	}

	public void atualizarMelhorIndividuo() {
		//Collections.sort(populacao, (Individuo x, Individuo y) -> Double.compare(x.fitness, y.fitness));
		if(Parametros.TIPO_DE_OTIMIZACAO == "MINIMIZACAO"){
			melhorIndividuo = populacao.stream().min((x, y) -> Double.compare(x.fitness, y.fitness)).get();
		}else{
			melhorIndividuo = populacao.stream().max((x, y) -> Double.compare(x.fitness, y.fitness)).get();
		}		
	}
	
	public double[][] prepareXOR_INPUT(Individuo i){
		
		List<List<Double>> xorList = new ArrayList<List<Double>>();
		HashMap<String, Double> hm = new HashMap<String, Double>();
		
		for (int numeroJanela = 0; numeroJanela < Parametros.NUMERO_TOTAL_VARIAVEL; numeroJanela++) {
			hm.put("X"+numeroJanela, 0d);
		}
		
		for(int walk = 0; walk < (serieTemporal.size()-Parametros.NUMERO_TOTAL_VARIAVEL); walk++){
			int aux = 0;
			for (int numeroJanela = 0; numeroJanela < Parametros.NUMERO_TOTAL_VARIAVEL; numeroJanela++) {
				hm.replace("X"+numeroJanela, serieTemporal.get(walk+aux));
				aux++;
			}
			xorList.add(i.parseToDoubleList(hm));
		}
		
		double[][] XOR_INPUT = new double[xorList.size()][xorList.get(0).size()]; 
		for (int indexX = 0; indexX < xorList.size(); indexX++)
		{
			for (int indexY = 0; indexY < xorList.get(indexX).size(); indexY++)
				XOR_INPUT[indexX][indexY] = xorList.get(indexX).get(indexY);
		}
				
		return XOR_INPUT;
	}
	
	public double[][] prepareXOR_IDEAL(){
		
		double[][] XOR_IDEAL = new double[serieTemporal.size()-Parametros.NUMERO_TOTAL_VARIAVEL][Parametros.NUMERO_TOTAL_VARIAVEL]; 
		
		for(int walk = 0; walk < (serieTemporal.size()-Parametros.NUMERO_TOTAL_VARIAVEL); walk++){
			int aux = 0;
			
			for (int numeroJanela = 0; numeroJanela < Parametros.NUMERO_TOTAL_VARIAVEL; numeroJanela++) {
				XOR_IDEAL[walk][numeroJanela] = serieTemporal.get(numeroJanela + aux);
				aux++;
			}
		}
		
		return XOR_IDEAL;
	}

	private void otimizarMelhorIndividuo() {
		// TODO Auto-generated method stub
		// EACH EPOCH iS ONE MORE INDIVIDUAL
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null,true, 2));
		network.addLayer(new BasicLayer(new ActivationSigmoid(),true, 3));
		network.addLayer(new BasicLayer(new ActivationSigmoid(),false, 1));
		network.getStructure().finalizeStructure();
		network.reset();

		// cada constante 
		// melhorIndividuo.noFuncao.
		//
		//           +
		//         /   \
		//        *     3
		//      /   \
		//     11   20
		// melhor individuo: fitness 
		//  xor_input = { { 11, 20, 3 } }
		// XORIDEAL Ã© a resposta final que eu quero chegar da serietemporal, notepad
		// { {const1, const2, const3, const4 ...} }

		// 1, 2, 3, 4, 5, 6, 7, 8, 9 Som alô
		//Alô, som som som, alô som ;-(

		double XOR_INPUT[][] = prepareXOR_INPUT(melhorIndividuo); //= Arvore.getConstantes(); criar um vetor c  numeros
		double XOR_IDEAL[][] = prepareXOR_IDEAL(); //  serie temporal

		MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);

		final ResilientPropagation train = new ResilientPropagation(network, trainingSet);

		int epoch = 1;

		do {
			train.iteration();
			System.out.println("Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		} while(train.getError() > 0.01);
		train.finishTraining();

		// test the neural network
		System.out.println("Neural Network Results:");
		for(MLDataPair pair: trainingSet ) {
			final MLData output = network.compute(pair.getInput());
			System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1)
					+ ", actual=" + output.getData(0) + ",ideal=" + pair.getIdeal().getData(0));
		}

		PerturbationFeatureImportanceCalc d;

		Encog.getInstance().shutdown();
	}

	public String toString(){
		return melhorIndividuo.toString();
	}

	private void showFitness(double[] fit) throws IOException {

		// The labels for the chart
		String[] labels = new String[fit.length];
		double[] data0 = new double[fit.length];

		for (int i = 0; i < fit.length; i++) {
			labels[i] = Integer.toString(i+1);
			//        	System.out.println(labels[i]);
		}

		//Instantiate an instance of this demo module
		DemoModule demo = new LineChart(labels, fit, "Iteracao", "Fitness", "Fitness para iteracoes");

		//Create and set up the main window
		JFrame frame = new JFrame(demo.toString());
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {System.exit(0);} });
		frame.getContentPane().setBackground(Color.white);

		// Create the chart and put them in the content pane
		ChartViewer viewer = new ChartViewer();
		demo.createChart(viewer, 0);
		frame.getContentPane().add(viewer);

		// Display the window
		frame.pack();
		frame.setVisible(true);

		// save image
		Container content = frame.getContentPane();
		BufferedImage awtImage = new BufferedImage(frame.getWidth(),frame.getHeight(),BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = awtImage.createGraphics();

		content.printAll(g2d);

		g2d.dispose();

		//		File directory = new File(".\\");
		File directory = new File("./");
		ImageIO.write(awtImage, "png", new File(directory.getAbsolutePath() + Parametros.NOME_CAMINHO_SALVAR_FITNESS));
	}
}
