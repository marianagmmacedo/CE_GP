package br.poli.gp;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import ChartDirector.ChartViewer;
import br.poli.gp.arvore.Funcao;
import br.poli.gp.util.DemoModule;
import br.poli.gp.util.LineChart;

public class AlgoritmoGP {
	//jmathplot
	ArrayList<Individuo> populacao;
	Individuo melhorIndividuo;
	// OTIMIZAR MELHORINDIVIDUO c resilient backpropagation
	//     <X     , Y     >
	HashMap<Double, Double> serieTemporal;
	
	
	public AlgoritmoGP(EInicializacao tipoInicializacao, HashMap<Double, Double> serieTemporal) throws IOException{
		populacao = new ArrayList<Individuo>();
		this.serieTemporal = serieTemporal;
		for (int cadaIndividuo = 0; cadaIndividuo < Parametros.NUMERO_TOTAL_INDIVIDUO; cadaIndividuo++) {
			populacao.add(new Individuo(tipoInicializacao));
		}
		calcularFitnessPopulacao();
		melhorIndividuo = (Individuo) Common.DeepCopy(populacao.get(0));
		runGP();
	}

	public AlgoritmoGP(int profundidadeIndividuo, HashMap<Double, Double> serieTemporal) throws IOException{
		populacao = new ArrayList<Individuo>();
		this.serieTemporal = serieTemporal;
		
		for (int cadaIndividuo = 0; cadaIndividuo < Parametros.NUMERO_TOTAL_INDIVIDUO; cadaIndividuo++) {
			populacao.add(new Individuo(profundidadeIndividuo));
		}
		calcularFitnessPopulacao();
		melhorIndividuo = (Individuo) Common.DeepCopy(populacao.get(0));
		runGP();
	}
	
	private void runGP() throws IOException {
		double[] getFit = new double[Parametros.NUMERO_TOTAL_ITERACAO];
		for (int iteracao = 0; iteracao < Parametros.NUMERO_TOTAL_ITERACAO; iteracao++) {
			cruzamentoMutacao();
			atualizarMelhorIndividuo();
			getFit[iteracao] = melhorIndividuo.fitness;
			System.out.println(getFit[iteracao]);
		}
		showFitness(getFit);
	}
	
	private void cruzamentoMutacao() {
		// restrigir a arvore resposta
		for (int cadaIndividuo = 0; cadaIndividuo < Parametros.NUMERO_TOTAL_INDIVIDUO; cadaIndividuo++) {
			if(Common.RANDOM.nextDouble() > Parametros.TAXA_CRUZAMENTO_MUTACAO){
				gerarDescendentes(populacao.get(cadaIndividuo), populacao.get(Common.RANDOM.nextInt(Parametros.NUMERO_TOTAL_INDIVIDUO)));
			}else{
				mutarDescendente(cadaIndividuo);
			}
		}
		calcularFitnessPopulacao();
		
	}

	private void mutarDescendente(int indexI) {
		// COMO VAI TRATAR A MUTACAO?
		Individuo filho = (Individuo) Common.DeepCopy(populacao.get(indexI));
		Individuo filha = (Individuo) Common.DeepCopy(new Individuo(EInicializacao.Mutacao));
		
		if(filho.noFuncao.size() != 0 && filha.noFuncao.size() != 0){
			int noMutacaoFilho = Common.RANDOM.nextInt((filho.noFuncao.size()));
			int noMutacaoFilha = Common.RANDOM.nextInt((filha.noFuncao.size()));
			
			Funcao fFilho = filho.noFuncao.get(noMutacaoFilho);
			Funcao fFilha = filha.noFuncao.get(noMutacaoFilha);
			
			fFilho.mutacao(fFilha);
			
			populacao.add(filho);
			populacao.add(filha);
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
			
			populacao.add(filho);
			populacao.add(filha);
		}
	}

	private void calcularFitnessPopulacao() {
		HashMap<String, Double> hm = new HashMap<String, Double>();
		hm.put("x", 0d);
		
		for(Individuo i : populacao){
			double fitness = 0.0;
			for(Map.Entry<Double, Double> entry : serieTemporal.entrySet()){
				hm.replace("x", entry.getKey());
//				System.err.println(entry.getValue());
//				System.err.println(i.calcularValor(hm));
				fitness += Math.pow(entry.getValue() - i.calcularValor(hm), 2); //REGRA PARA CALCULAR O FITNESS DEVE SER DISCUTIDA
				// COMO VAI TRATAR NAN?
			}
			
			i.fitness = fitness/serieTemporal.size();
		}
	}
	
	private void atualizarMelhorIndividuo() {
		// COMO VAI LIDAR COM OS LIMITES DA MINIMIZACAO OU MAXIMIZACAO?
		if(Parametros.TIPO_DE_OTIMIZACAO == "MINIMIZACAO"){
			menorIndividuo();
		}else{
			maiorIndividuo();
		}
	}

	private void menorIndividuo() {
		// MANTER A MENOR ARVORE
		double menor = melhorIndividuo.fitness;
		for(Individuo i: populacao){
			if(i.fitness < menor){
				menor = i.fitness;
				melhorIndividuo = (Individuo) Common.DeepCopy(i);
			}
		}
	}
	
	private void maiorIndividuo() {
		// MANTER A MENOR ARVORE
		double maior = melhorIndividuo.fitness;
		for(Individuo i: populacao){
			if(i.fitness > maior){
				maior = i.fitness;
				melhorIndividuo = (Individuo) Common.DeepCopy(i);
			}
		}
		
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
		
		ImageIO.write(awtImage, "png", new File(Parametros.NOME_CAMINHO_SALVAR_FITNESS+"/fitness.png"));
		
	}
}
