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
	Map<Double, Double> serieTemporal;
	
	
	public AlgoritmoGP(EInicializacao tipoInicializacao, HashMap<Double, Double> serieTemporal) throws IOException{
		populacao = new ArrayList<Individuo>();
		this.serieTemporal = serieTemporal;
		for (int cadaIndividuo = 0; cadaIndividuo < Parametros.NUMERO_TOTAL_INDIVIDUO; cadaIndividuo++) {
			populacao.add(new Individuo(tipoInicializacao));
		}
	}

	public AlgoritmoGP(int profundidadeIndividuo, HashMap<Double, Double> serieTemporal) throws IOException{
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
			atualizarMelhorIndividuo();
			getFit[iteracao] = melhorIndividuo.fitness;
			System.out.println(getFit[iteracao]);
		}
		showFitness(getFit);
	}
	
	private void removerMenosAdaptados() {
		// TODO Auto-generated method stub
		populacao.sort((i1, i2) -> Double.compare(i1.fitness, i2.fitness));
		while (populacao.size()>300){
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
				//Muta��o s� ocorre se o individuo n�o for o melhor, para evitar perda de informa��es
				if (i!=melhorIndividuo)
					mutarDescendente(i);
			}
		}		
	}

	private void mutarDescendente(Individuo individuo) {		
		if(individuo.noFuncao.size() != 0){
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

	private void calcularFitnessPopulacao() {
		HashMap<String, Double> hm = new HashMap<String, Double>();
		for (int numeroJanela = 0; numeroJanela < Parametros.NUMERO_TOTAL_VARIAVEL; numeroJanela++) {
			hm.put("X"+numeroJanela, 0d);
		}
		for(Individuo i : populacao){
			double fitness = 0.0;
			for(Double walk = 0.0; walk < (serieTemporal.size()-Parametros.NUMERO_TOTAL_VARIAVEL); walk++){
				int aux = 0;
				for (Double numeroJanela = 0.0; numeroJanela < Parametros.NUMERO_TOTAL_VARIAVEL; numeroJanela++) {
					hm.replace("X"+numeroJanela.intValue(), serieTemporal.get(walk+aux));
					aux++;
				}
				fitness += Math.pow(serieTemporal.get(walk+Parametros.NUMERO_TOTAL_VARIAVEL) - i.calcularValor(hm), 2); //REGRA PARA CALCULAR O FITNESS DEVE SER DISCUTIDA
				// COMO VAI TRATAR NAN?
			}
			
			i.fitness = fitness/serieTemporal.size();
		}
	}
	
	public void atualizarMelhorIndividuo() {
		if(Parametros.TIPO_DE_OTIMIZACAO == "MINIMIZACAO"){
			menorIndividuo();
		}else{
			maiorIndividuo();
		}
	}

	private void menorIndividuo() {
		// MANTER A MENOR ARVORE
		double menor = Double.MAX_VALUE;
		for(Individuo i: populacao){
			if(i.fitness < menor){
				menor = i.fitness;
				melhorIndividuo = i;
			}
		}
	}
	
	private void maiorIndividuo() {
		// MANTER A MENOR ARVORE
		double maior = Double.MIN_VALUE;
		for(Individuo i: populacao){
			if(i.fitness > maior){
				maior = i.fitness;
				melhorIndividuo = i;
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
		
//		File directory = new File(".\\");
		File directory = new File("./");
		ImageIO.write(awtImage, "png", new File(directory.getAbsolutePath() + Parametros.NOME_CAMINHO_SALVAR_FITNESS));
	}
}
