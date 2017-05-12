package br.poli.gp;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import br.poli.gp.util.ContourChart;
import br.poli.gp.util.DemoModule;
import br.poli.gp.util.LineChart;
import ChartDirector.ChartViewer;

public class RunMain {
	
	
	static double[] dataXH;
	static double[] dataYH1;
	static double[] dataZH;
	
	public static void main(String[] args) throws IOException {
		
		
		HashMap<Integer, Double> serieTemporal = Common.lerBase(Parametros.Base);
		double[] mediaDesvio = Common.Normalizar2(serieTemporal);
//		double[] mediaDesvio = new double[2];
//		mediaDesvio[0] = 1.0;
//		mediaDesvio[1] = 1.0;
//		Common.NormalizarLN(serieTemporal);
		/*
		while(true){			
			Individuo i = new Individuo(6);
			System.out.println("PRE:" + i.toString());
			System.out.println("P: " + i.calcularValor(null));
			//new AlgoritmoGp
			//double d = i.calcularValor(null);
			//System.out.println("Solul��o: " + i.calcularValor(null));
			i.otimizarArvore();
			System.out.println("POS:" + i.toString());
			System.out.println("P: " + i.calcularValor(null));
			//if ((d+"").contains("NaN")) break;
		}*/
		
		
		// TRANSFORMACAO LINEAR ENTRE 0 E 1
		double[] meanSimulacao = new double[Parametros.NUMERO_TOTAL_ITERACAO/Parametros.ITERACAO_BREAK];
		StringBuilder sb = new StringBuilder();
		
		dataXH = new double[Parametros.NUMERO_TOTAL_SIMULACAO];
		dataYH1 = new double[Parametros.NUMERO_TOTAL_SIMULACAO];
		dataZH = new double[Parametros.NUMERO_TOTAL_SIMULACAO];
		int numeroFuncao = 10;
		double taxaMutacao = Parametros.TAXA_CRUZAMENTO_MUTACAO;
		int numeroPopulacao = 20;
		double meanSim = 0.0;
		int tamanhaMaximoArvore = Parametros.TAMANHO_MAXIMO_PROFUNDIDADE_ARVORE;
		for (int simulacao = 0; simulacao < Parametros.NUMERO_TOTAL_SIMULACAO; simulacao++) {
			System.out.println(simulacao);
			AlgoritmoGP gp = new AlgoritmoGP(EInicializacao.Completa, serieTemporal, taxaMutacao
					, Parametros.NUMERO_TOTAL_FUNCAO, tamanhaMaximoArvore
					, numeroPopulacao, mediaDesvio[0], mediaDesvio[1]);
			//double[] each = gp.runGP(simulacao);
			double each = gp.runGP(simulacao);
			meanSim += each;
//			dataXH[simulacao] = taxaMutacao;
//			dataYH1[simulacao] = (double) numeroPopulacao/100;
//			dataZH[simulacao] = each[each.length-1];
			//numeroPopulacao++;
//			System.out.println(dataXH[simulacao]);
//			System.out.println(dataYH1[simulacao]);
//			System.out.println(dataZH[simulacao]);
			//System.out.println(each[each.length-1]);
			//System.out.println(numeroPopulacao);
//			if(simulacao<=14){
//				tamanhaMaximoArvore += 1;
//			}else if(simulacao>14 && simulacao<=35){
//				taxaMutacao += 0.01;
//				tamanhaMaximoArvore = 2;
//			}else if(simulacao>15 && simulacao<=35){
//				tamanhaMaximoArvore += 1;
//				taxaMutacao -= 0.01;
//			}else if(simulacao>35){
//				tamanhaMaximoArvore += 2;
//			}
//			
//			System.out.println(each[each.length-1]);
//			sb.append(" " + each[each.length-1]);
//			sb.append("\n");
//			for (int i = 0; i < each.length; i++) {
//				meanSimulacao[i] += each[i];
//			}
//			System.out.println("GP:" + gp.toString());
//			System.out.println("GP:" + gp.melhorIndividuo.fitness);
//			System.out.println("GP:" + gp.populacao.get(0).toString());
//			System.out.println("GP:" + gp.populacao.get(0).fitness);
			
		}
//		StringBuilder sb2 = new StringBuilder();
//		for (int i = 0; i < meanSimulacao.length; i++) {
//			meanSimulacao[i] /= meanSimulacao.length;
//			sb2.append(" " + meanSimulacao[i]);
//			sb2.append("\n");
//		}
		
		
//		showFitness(meanSimulacao);
//		File directory = new File("./");
//		
//		PrintWriter p = new PrintWriter(new File(directory.getAbsolutePath() + Parametros.NOME_CAMINHO_SALVAR_FITNESS+"_result_final.csv"));
//		p.write(sb.toString());
//		p.flush();
//		p.close();
//		
//		PrintWriter p2 = new PrintWriter(new File(directory.getAbsolutePath() + Parametros.NOME_CAMINHO_SALVAR_FITNESS+"_all_it.csv"));
//		p2.write(sb2.toString());
//		p2.flush();
//		p2.close();
		
		//heatMap();
		meanSim /= 30; 
		System.out.println(meanSim);
		System.out.println("FINISHED");
	}
	
	private static void showFitness(double[] fit) throws IOException {

		// The labels for the chart
		String[] labels = new String[fit.length];
		double[] data0 = new double[fit.length];

		for (int i = 0; i < fit.length; i++) {
			labels[i] = Integer.toString((i)*Parametros.ITERACAO_BREAK);
			//        	System.out.println(labels[i]);
		}

		//Instantiate an instance of this demo module
		DemoModule demo = new LineChart(labels, fit, "Iteration", "Fitness Mean", "Fitness mean x Iteration");

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
		ImageIO.write(awtImage, "png", new File(directory.getAbsolutePath() + Parametros.NOME_CAMINHO_SALVAR_FITNESS+".png"));
	}
	
	public static void heatMap() throws IOException {
		//Instantiate an instance of this demo module
	
		
		DemoModule demo = new ContourChart(dataXH, dataYH1, dataZH, "Taxa de mutação", "Tamanho máximo da árvore");

		//Create and set up the main window
		JFrame frame2 = new JFrame(demo.toString());
//		frame2.addWindowListener(new WindowAdapter() {
//			public void windowClosing(WindowEvent e) {System.exit(0);} });
		frame2.getContentPane().setBackground(Color.white);

		// Create the chart and put them in the content pane
		ChartViewer viewer_ = new ChartViewer();
		demo.createChart(viewer_, 0);
		frame2.getContentPane().add(viewer_);

		// Display the window
		frame2.pack();
		frame2.setVisible(true);
		
		// save image
		Container content_ = frame2.getContentPane();
		BufferedImage awtImage = new BufferedImage(frame2.getWidth(),frame2.getHeight(),BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = awtImage.createGraphics();

		content_.printAll(g2d);

		g2d.dispose();
		File directory = new File("./");
		ImageIO.write(awtImage, "png", new File(directory.getAbsolutePath() + Parametros.SERIES+"heatmap.png"));
       
	}
}
