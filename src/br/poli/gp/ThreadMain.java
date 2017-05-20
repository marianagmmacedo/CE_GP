package br.poli.gp;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import ChartDirector.ChartViewer;
import br.poli.fineTuning.FishSchoolSearch;
import br.poli.gp.util.DemoModule;
import br.poli.gp.util.LineChart;
import br.poli.mlp.MultiLayerPerceptron;
import br.poli.output.Output;

public class ThreadMain extends Thread {

	static Stack<String> stackString;
	
	public static void startStack(String[] listString){
		stackString = new Stack<String>();
		for(String s : listString){
			stackString.push(s);
		}
	}
	
	public static void startStack(){
		stackString = new Stack<String>();
		for(String s : Parametros.Bases){
			stackString.push(s);
		}
	}
	
	public static void startMultiThreading(int threadCount){
		Thread[] tList = new Thread[threadCount];
		
		for (int i = 0; i < threadCount; i++){
			tList[i] = new ThreadMain();
			tList[i].start();
		}
	}

	public void run() {
		System.out.println("Thread Running!");		
		
		try{
			while (true){
				String base = stackString.pop();
				
				HashMap<Integer, Double> serieTemporal = Common.lerBase(base);
				Output.outputList.add(new Output(base));
				
				double[] mediaDesvio = {Common.CalcularMedia(serieTemporal), Common.CalcularDesvioPadrao(serieTemporal)};
				
				if (base.equals("lynx"))
					Common.NormalizarLN(serieTemporal);
				else
					Common.Normalizar2(serieTemporal);
				
				double[] respostas = new double[30];

				//System.out.println("Base: " + base);

				for (int i = 0; i < 30;){

					System.out.println(base + ", Sim: " + (i + 1));
					
//					MultiLayerPerceptron mlp = new MultiLayerPerceptron(base, 20, null, -1);
//					mlp.forwardBackward();
//					double d = mlp.evaluate(base, false);
					
//					FishSchoolSearch fss = new FishSchoolSearch(base);
//					double d = fss.run(i);
					
					AlgoritmoGP gp = new AlgoritmoGP(EInicializacao.Completa, serieTemporal, Parametros.TAXA_CRUZAMENTO_MUTACAO
							, Parametros.NUMERO_TOTAL_FUNCAO, Parametros.TAMANHO_MAXIMO_PROFUNDIDADE_ARVORE
							, Parametros.NUMERO_MAXIMO_POPULACAO, mediaDesvio[0], mediaDesvio[1], base);

					double d = gp.runGP(i);
					
					if (!(Double.isInfinite(d) || Double.isNaN(d))){
						respostas[i]=d;
						i++;
					}
				}

				double media = Common.CalcularMedia(respostas);
				double desvio = Common.CalcularDesvioPadrao(respostas);

//				System.out.println("base: " + base + "/med: " + media + "/desvio: " + desvio);
				
				File directory = new File("./");

				File f = new File(directory.getAbsolutePath()+"/resultados/thread_"+ base + "_GP_withSimplification.txt");

				PrintWriter gravarArq = new PrintWriter(f);
			    
				Output out = Output.getOutputByList(base);
				
				out.texto[0] = "med: " + media + "/desvio: " + desvio;
				
			    gravarArq.write(out.toString());
			    
	//		    System.out.println("asdf");

			    gravarArq.close();
			}
		} catch (EmptyStackException e) {
			System.out.println("Thread terminou pois n�o h� mais base a ser processada.");
		} catch (Exception e) {
			System.out.println(e.toString());
			System.out.println(e.getMessage());
			System.out.println("Deu Caca");
		}		
	}
	
	private static void showFitness(double[] fit, String Base) throws IOException {

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
		ImageIO.write(awtImage, "png", new File(directory.getAbsolutePath() + "/resultados/"+Base+"/"+Base+"_mean_"+Parametros.NUMERO_TOTAL_SIMULACAO+".png"));
	}
}
