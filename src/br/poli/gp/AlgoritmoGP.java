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
import java.util.spi.TimeZoneNameProvider;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.plaf.SliderUI;

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
import br.poli.gp.util.SplineLineChart;

public class AlgoritmoGP {
	//jmathplot
	// OTIMIZAR MELHORINDIVIDUO c resilient backpropagation
	//     <X     , Y     >
	ArrayList<Individuo> populacao;
	Individuo melhorIndividuo;
	Individuo otimizadoIndividuo;
	int tamanhoJanela;
	Map<Integer, Double> serieTemporal;
	public double[] serie;
	public double[] previsto;
	public double[] serieTreinamento;
	public double[] previstoTreinamento;
	public int simulacao;
	public int validarBase;
	double taxaMutacaoCruzamento;
	int numeroFuncao;
    int tamanhaMaximoArvore;
    int numeroPopulacao;
    double mediaSerieTemporal;
    double desvioPadraoSerieTemporal;
    double fitnessValidacao;
	
	public AlgoritmoGP(EInicializacao tipoInicializacao, HashMap<Integer, Double> serieTemporal, 
			double taxaMutacaoCruzamento_, int numeroFuncao_, int tamanhaMaximoArvore_, int numeroPopulacao_, double media, double desvioPadrao) throws IOException{
		populacao = new ArrayList<Individuo>();
		this.serieTemporal = serieTemporal;
		taxaMutacaoCruzamento = taxaMutacaoCruzamento_;
//		System.out.println(this.taxaMutacaoCruzamento);
		numeroFuncao = numeroFuncao_;
//		System.out.println(numeroFuncao);
		tamanhaMaximoArvore = tamanhaMaximoArvore_;
//		System.out.println(tamanhaMaximoArvore);
		validarBase = (int) Math.ceil(serieTemporal.size()*Parametros.TAXA_VALIDACAO);
//		System.out.println(validarBase);
		numeroPopulacao = numeroPopulacao_;
//		System.out.println(numeroPopulacao);
		mediaSerieTemporal = media;
//		System.out.println(mediaSerieTemporal);
		desvioPadraoSerieTemporal = desvioPadrao;
//		System.out.println(desvioPadraoSerieTemporal);
		criarNovosIndividuos(numeroPopulacao_, tipoInicializacao);
	}
	
	public AlgoritmoGP(EInicializacao tipoInicializacao, HashMap<Integer, Double> serieTemporal) throws IOException{
		populacao = new ArrayList<Individuo>();
		this.serieTemporal = serieTemporal;
		this.taxaMutacaoCruzamento = Parametros.TAXA_CRUZAMENTO_MUTACAO;
		this.validarBase = (int) Math.ceil(serieTemporal.size()*Parametros.TAXA_VALIDACAO);
		this.numeroPopulacao = Parametros.NUMERO_MAXIMO_POPULACAO;
		criarNovosIndividuos(this.numeroPopulacao, tipoInicializacao);
	}

	public AlgoritmoGP(int profundidadeIndividuo, HashMap<Integer, Double> serieTemporal) throws IOException{
		populacao = new ArrayList<Individuo>();
		this.serieTemporal = serieTemporal;
		this.taxaMutacaoCruzamento = Parametros.TAXA_CRUZAMENTO_MUTACAO;
		this.validarBase = (int) Math.floor(serieTemporal.size()*Parametros.TAXA_VALIDACAO);
		this.numeroPopulacao = Parametros.NUMERO_MAXIMO_POPULACAO;
		criarNovosIndividuos(this.numeroPopulacao, profundidadeIndividuo);	
	}

	public double runGP(int sim) throws IOException {
		simulacao = sim;
		tamanhoJanela = Parametros.NUMERO_TOTAL_VARIAVEL;
		
		atualizarMelhorIndividuo();
		double[] getFit = new double[Parametros.NUMERO_TOTAL_ITERACAO/Parametros.ITERACAO_BREAK];
		
		int size = 0;
		
		for (int iteracao = 0; iteracao < Parametros.NUMERO_TOTAL_ITERACAO; iteracao++) {
			reproduzir();
			calcularFitnessPopulacao();
			removerMenosAdaptados();
			
			//Gerar novos individuos durante as iteracoes
			if (Common.RANDOM.nextDouble() > 0.5 && Parametros.GERAR_NOVOS_INDIVIDUOS){
				criarNovosIndividuos(Parametros.NUMERO_NOVOS_INDIVIDUOS, Parametros.TAMANHO_NOVOS_INDIVIDUOS);
			}
			
			// Atualizar melhor individuo
			Individuo _melhorIndi = this.melhorIndividuo;
			atualizarMelhorIndividuo();

			//Caso o individuo tenha sido alterado
			if(Common.RANDOM.nextDouble() > 0.5 && Parametros.OTIMIZAR){
				
				if (_melhorIndi != this.melhorIndividuo || (iteracao==Parametros.NUMERO_TOTAL_ITERACAO-1)){
					//System.out.println(this.melhorIndividuo.toString());
					otimizarMelhorIndividuo();			
				}
			}else if(Parametros.OTIMIZAR_SEMPRE){
				otimizarMelhorIndividuo();
			}
			
			
			if(iteracao%Parametros.ITERACAO_BREAK==0){
//				otimizarMelhorIndividuo();
				getFit[size] = this.melhorIndividuo.fitness;
				size++;
			}
			
			if (Parametros.TAXA_CRUZAMENTO_MUTACAO_DECRESCENTE.equals("LINEAR")){
				this.taxaMutacaoCruzamento -= Parametros.TAXA_CRUZAMENTO_MUTACAO/Parametros.NUMERO_TOTAL_ITERACAO;
			} else if (Parametros.TAXA_CRUZAMENTO_MUTACAO_DECRESCENTE.equals("EXPONENCIAL")){
				this.taxaMutacaoCruzamento =  Parametros.TAXA_CRUZAMENTO_MUTACAO * Math.pow(Math.E, (-iteracao/Parametros.NUMERO_TOTAL_ITERACAO * Parametros.TAXA_CRUZAMENTO_MUTACAO_DECAIMENTO_EXPONENCIAL)); 
			}else if (Parametros.TAXA_CRUZAMENTO_MUTACAO_DECRESCENTE.equals("MENOS")){
				this.taxaMutacaoCruzamento -= 0.01;
			}
			
			//System.out.println("IT: " + iteracao + "/" + Parametros.NUMERO_TOTAL_ITERACAO + "  /  " + this.melhorIndividuo.fitness);
		}
		
		calcularFitnessIndividuoFinal(this.melhorIndividuo);

		//Imprimir graficos do fitness
		//showFitness(getFit);	
		//showBothExpressionTreinamento();
		//showBothExpression();
		//System.out.println(this.melhorIndividuo.toString());
		//return getFit;
		return fitnessValidacao;
	}
	
	private void criarNovosIndividuos(int numeroIndividuos, int profundidadeIndividuo){
		for (int i = 0; i < numeroIndividuos; i++){
			populacao.add(new Individuo(profundidadeIndividuo, numeroFuncao));
		}
	}
	
	private void criarNovosIndividuos(int numeroIndividuos, EInicializacao tipoInicializacao){
		for (int i = 0; i < numeroIndividuos; i++){
			populacao.add(new Individuo(tipoInicializacao, numeroFuncao, tamanhaMaximoArvore));
		}
	}

	private void showBothExpressionTreinamento() throws IOException {
		// The labels for the chart
		String[] labels = new String[this.serieTreinamento.length];
		for (int i = 0; i < labels.length; i++) {
			labels[i] = Integer.toString((i));
		}
		
		//System.out.println("FINAL-----------");
		for (int i = 0; i < this.serieTreinamento.length; i++) {
			//this.serieTreinamento[i] = (this.serieTreinamento[i]*this.desvioPadraoSerieTemporal) + mediaSerieTemporal;
			this.serieTreinamento[i] = Math.pow(Math.E, this.serieTreinamento[i]);
			//System.out.println(this.serieTreinamento[i]);
			//this.previstoTreinamento[i] = (this.previstoTreinamento[i]*this.desvioPadraoSerieTemporal) + mediaSerieTemporal;
			this.previstoTreinamento[i] = Math.pow(Math.E, this.previstoTreinamento[i]);
			//System.out.println(this.previstoTreinamento[i]);
			//x -> x.setValue((x.getValue() - media)/desvioPadrao)
		}
		//Instantiate an instance of this demo module
		DemoModule demo = new SplineLineChart(labels, this.previstoTreinamento, this.serieTreinamento, "Tempo", "Resposta", "Séries");

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
		ImageIO.write(awtImage, "png", new File(directory.getAbsolutePath() + Parametros.SERIES+this.simulacao+"_bothSeries_Treinamento.png"));

	}
	
	private void showBothExpression() throws IOException {
		// The labels for the chart
		String[] labels = new String[validarBase];
		for (int i = 0; i < labels.length; i++) {
			labels[i] = Integer.toString((i));
		}
		
		//System.out.println("FINAL-----------");
		for (int i = 0; i < this.serie.length; i++) {
			//this.serie[i] = (this.serie[i]*this.desvioPadraoSerieTemporal) + mediaSerieTemporal;
			this.serie[i] = Math.pow(Math.E, this.serie[i]);
			//System.out.println(this.serie[i]);
			//this.previsto[i] = (this.previsto[i]*this.desvioPadraoSerieTemporal) + mediaSerieTemporal;
			this.previsto[i] = Math.pow(Math.E, this.previsto[i]);
			//System.out.println(this.previsto[i]);
			//x -> x.setValue((x.getValue() - media)/desvioPadrao)
		}
		//Instantiate an instance of this demo module
		DemoModule demo = new SplineLineChart(labels, this.previsto, this.serie, "Tempo", "Resposta", "Séries");

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
		ImageIO.write(awtImage, "png", new File(directory.getAbsolutePath() + Parametros.SERIES+this.simulacao+"_bothSeries.png"));

	}

	private void calcularFitnessIndividuoFinal(Individuo i) {
		HashMap<String, Double> hm = new HashMap<String, Double>();
		this.serie = new double[validarBase];
		this.previsto = new double[validarBase];
		for (int numeroJanela = 0; numeroJanela < this.tamanhoJanela; numeroJanela++) {
			hm.put("X"+numeroJanela, 0d);
		}
		double fitness = 0.0;
		int all = 0;
		for(int walk = serieTemporal.size()-(this.tamanhoJanela)-validarBase; walk < (serieTemporal.size()-(this.tamanhoJanela)); walk++){
			int aux = 0;
			for (int numeroJanela = 0; numeroJanela < this.tamanhoJanela; numeroJanela++) {
				hm.replace("X"+numeroJanela, (serieTemporal.get(walk+aux) ));
				aux++;
			}

			this.serie[all] = serieTemporal.get(walk+this.tamanhoJanela);
			this.previsto[all] = i.calcularValor(hm);
			fitness += Math.pow( ( ((this.serie[all]*this.desvioPadraoSerieTemporal) + mediaSerieTemporal)  - ((this.previsto[all]*this.desvioPadraoSerieTemporal) + mediaSerieTemporal) ) , 2);
			//fitness += Math.pow(Math.pow(Math.E, this.serie[all]) - Math.pow(Math.E, this.previsto[all]), 2);
			//System.out.println("serie"+this.serie[all]);
//			System.out.println("previsto"+this.previsto[all]);
			
			all++;

		}	
		//System.out.println("validacao");
		//System.out.println(Math.sqrt(fitness/(validarBase)));
		fitnessValidacao = Math.sqrt(fitness/(validarBase));
		
		hm = new HashMap<String, Double>();
		this.serieTreinamento = new double[(serieTemporal.size()-this.tamanhoJanela-validarBase)];
		this.previstoTreinamento = new double[(serieTemporal.size()-this.tamanhoJanela-validarBase)];
		for (int numeroJanela = 0; numeroJanela < this.tamanhoJanela; numeroJanela++) {
			hm.put("X"+numeroJanela, 0d);
		}
		double fitness2 = 0.0;
		int all2 = 0;
		for(int walk = 0; walk < (serieTemporal.size()-this.tamanhoJanela-validarBase); walk++){
			int aux = 0;
			for (int numeroJanela = 0; numeroJanela < this.tamanhoJanela; numeroJanela++) {
				hm.replace("X"+numeroJanela, (serieTemporal.get(walk+aux) ));
				aux++;
			}

			this.serieTreinamento[all2] = serieTemporal.get(walk+this.tamanhoJanela);
			this.previstoTreinamento[all2] = i.calcularValor(hm);
			fitness2 += Math.pow( ( ((this.serieTreinamento[all2]*this.desvioPadraoSerieTemporal) + mediaSerieTemporal)  - ((this.previstoTreinamento[all2]*this.desvioPadraoSerieTemporal) + mediaSerieTemporal) ) , 2);
			//fitness2 += Math.pow(Math.pow(Math.E, this.serieTreinamento[all2]) - Math.pow(Math.E, this.previstoTreinamento[all2]), 2);
			//System.out.println("serie"+this.serie[all]);
//			System.out.println("previsto"+this.previsto[all]);
			
			all2++;

		}	
		//System.out.println("treinamento");
		//System.out.println(Math.sqrt(fitness2/(serieTemporal.size()-this.tamanhoJanela-validarBase)));
		
		
		
	}

	private void removerMenosAdaptados() {
		if (populacao.size() > this.numeroPopulacao){
			populacao.sort((i1, i2) -> Double.compare(i1.fitness, i2.fitness));
			populacao.subList(this.numeroPopulacao, populacao.size()).clear();
		}
	}

	private void reproduzir() {
		// restrigir a arvore resposta
		int tamanhoPopulacao = populacao.size();
		for (int individuoIndex = 0; individuoIndex < tamanhoPopulacao; individuoIndex++) {
			Individuo i = populacao.get(individuoIndex);
			if(Common.RANDOM.nextDouble() > this.taxaMutacaoCruzamento){
				gerarDescendentes(i, populacao.get(Common.RANDOM.nextInt(populacao.size())));
			}else{
				//Mutacao so ocorre se o individuo nao for o melhor, para evitar perda de informacoes
				if (i!=melhorIndividuo){
					mutarDescendente(i);
				}					
			}
		}		
	}

	private void mutarDescendente(Individuo individuo) {		
		if(individuo.noFuncao.size() != 0){
			individuo.fitnessJaCalculado = false;
			int noMutacaoFilho = Common.RANDOM.nextInt((individuo.noFuncao.size()));
			Funcao fFilho = individuo.noFuncao.get(noMutacaoFilho);
			fFilho.crossover((new Individuo(EInicializacao.Mutacao, numeroFuncao, tamanhaMaximoArvore)).arvore.no);
			individuo.otimizarArvore();
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

	private Double calcularFitnessIndividuo(Individuo i, int janela) {
		HashMap<String, Double> hm = new HashMap<String, Double>();

		for (int numeroJanela = 0; numeroJanela < janela; numeroJanela++) {
			hm.put("X"+numeroJanela, 0.0);
		}
		double fitness = 0.0;

		for(int walk = 0; walk < (serieTemporal.size()-janela-validarBase); walk++){
			
			for (int numeroJanela = 0; numeroJanela < janela; numeroJanela++) {
				hm.replace("X"+numeroJanela, serieTemporal.get(walk+numeroJanela));
			}
			
			Double x = i.calcularValor(hm);
			
			if(x.isNaN() || x.isInfinite()){ 
				return x;
			}else{
				fitness += Math.pow(serieTemporal.get(walk+janela) - x, 2);
			}
			
		}

		i.fitness = Math.sqrt(fitness/(serieTemporal.size()-janela-validarBase));
		//System.out.println("i.fitnessssssssssss  "+ i.fitness);
		i.fitnessJaCalculado = true;
		return i.fitness;
	}

	private void calcularFitnessPopulacao() {

		for(Individuo i : populacao){
			if (!i.fitnessJaCalculado){
				Double fitness = calcularFitnessIndividuo(i, this.tamanhoJanela);
//				System.out.println(fitness);
				int times = 0;
				while( fitness.isNaN() || fitness.isInfinite() ){
					//System.out.println("while");
					if(times > 5){
						i = new Individuo(EInicializacao.Completa, numeroFuncao, tamanhaMaximoArvore);
					}
					fitness = calcularFitnessIndividuo(i, this.tamanhoJanela);
					otimizarMelhorIndividuo(i);
					times++;
				}
			}
		}
	}
	
	private void otimizarMelhorIndividuo(Individuo individuo) {

		double[] position = getConstantes(individuo);
		double o = 1.0;
		double T = (1/Math.sqrt(position.length));
		if(position.length>0){
			double[] newPosition = new double[position.length];
			for (int i = 0; i < Parametros.ESTRATEGIA_EVOLUCAO_ITERACAO; i++) {
				for (int each = 0; each < position.length; each++) {
					//							if(Common.RANDOM.nextDouble()>0.5){
					//								newPosition[each] = position[each] + o * getGaussian(0, 1.0);
					//							}else{
					newPosition[each] = position[each] - o * getGaussian(0, 1.0);
					//							}
					if(newPosition[each]<=0.0){
						newPosition[each] += Common.RANDOM.nextDouble();
					}
				}
				double delta = atualizarConstantes(position, individuo) - atualizarConstantes(newPosition, individuo);
				if(delta > 0){
					position = newPosition;
				}
				o *= Math.pow(Math.E, T*getGaussian(0, 1.0));

			}
			if(otimizadoIndividuo.fitness < individuo.fitness){
				individuo = (Individuo) Common.DeepCopy(otimizadoIndividuo);
				//System.out.println("melhor otimizarMelhorIndividuo i ");
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

		for (int numeroJanela = 0; numeroJanela < tamanhoJanela; numeroJanela++) {
			hm.put("X"+numeroJanela, 0d);
		}

		for(int walk = 0; walk < (serieTemporal.size()-tamanhoJanela); walk++){
			int aux = 0;
			for (int numeroJanela = 0; numeroJanela < tamanhoJanela; numeroJanela++) {
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

		double[][] XOR_IDEAL = new double[serieTemporal.size()-tamanhoJanela][tamanhoJanela]; 

		for(int walk = 0; walk < (serieTemporal.size()-tamanhoJanela); walk++){
			int aux = 0;

			for (int numeroJanela = 0; numeroJanela < tamanhoJanela; numeroJanela++) {
				XOR_IDEAL[walk][numeroJanela] = serieTemporal.get(numeroJanela + aux);
				aux++;
			}
		}

		return XOR_IDEAL;
	}


	private void otimizarMelhorIndividuo() {

		if(Parametros.SIMULATED_ANNEALING){
			double temperatura = Parametros.TEMPERATURA_INICIAL_SIM_ANN;
			double[] position = getConstantes(this.melhorIndividuo);
			if(position.length>0){
				double[] newPosition = new double[position.length];
				while (temperatura > Parametros.TEMPERATURA_FINAL_SIM_ANN) {	
					for (int each = 0; each < position.length; each++) {
						//						System.out.print(position[each]);
						newPosition[each] = position[each]+(Math.random() *temperatura* (Math.random() > 0.5 ? 1 : -1));
					}
					//					System.out.println("/");
					double delta = atualizarConstantes(position, this.melhorIndividuo) - atualizarConstantes(newPosition, this.melhorIndividuo);
					if(delta > 0){
						position = newPosition;
					}else{
						double rand = Common.RANDOM.nextDouble();
						if(rand < Math.pow(Math.E, (-(delta)/temperatura)) ){
							position = newPosition;
						}
					}
					temperatura *= (1-Parametros.ALFA_SIM_ANN);

				}
				//				System.out.println(otimizadoIndividuo.fitness);
				//				System.out.println(melhorIndividuo.fitness);
				if(otimizadoIndividuo.fitness < melhorIndividuo.fitness){
					melhorIndividuo = (Individuo) Common.DeepCopy(otimizadoIndividuo);
					System.out.println("melhor");
				}

			}
		}else if(Parametros.HILL_CLIMBING){
			double[] position = getConstantes(this.melhorIndividuo);
			if(position.length>0){
				double[] newPosition = new double[position.length];
				for (int i = 0; i < Parametros.HILL_CLIMBING_MAX_ITERACAO; i++) {
					for (int each = 0; each < position.length; each++) {
						newPosition[each] = position[each] + (Math.random()*1*(Math.random() > 0.5 ? 1 : -1));
					}
					double delta = atualizarConstantes(position, this.melhorIndividuo) - atualizarConstantes(newPosition, this.melhorIndividuo);
					if(delta > 0){
						position = newPosition;
					}

				}
				if(otimizadoIndividuo.fitness < melhorIndividuo.fitness){
					melhorIndividuo = (Individuo) Common.DeepCopy(otimizadoIndividuo);
					System.out.println("melhor");
				}

			}
		}else if(Parametros.VARIAR_JANELA){
			// TA ERRADO, UTILIZAR PARA MUDANCA DE JANELA
			System.out.println("Parametros.VARIAR_JANELA");
			for (int i = 2; i < Parametros.NUMERO_MAXIMO_VARIAVEL; i++) {
				//				System.out.println(i);
				otimizadoIndividuo = (Individuo) Common.DeepCopy(melhorIndividuo);
				calcularFitnessIndividuo(otimizadoIndividuo, i);
				if(otimizadoIndividuo.fitness < melhorIndividuo.fitness){
					tamanhoJanela = i;
					System.out.println("MUDEI JANELA     " + i);
					melhorIndividuo = (Individuo) Common.DeepCopy(otimizadoIndividuo);
				}
			}
		}else if(Parametros.ESTRATEGIA_EVOLUCAO_TODOS){ 
			for (int eachPOP = 0; eachPOP < this.populacao.size(); eachPOP++) {
				double[] position = getConstantes(this.populacao.get(eachPOP));
				double o = 1.0;
				double T = 1/Math.sqrt(position.length);
				if(position.length>0){
					double[] newPosition = new double[position.length];
					for (int i = 0; i < Parametros.ESTRATEGIA_EVOLUCAO_ITERACAO; i++) {
						for (int each = 0; each < position.length; each++) {
							newPosition[each] = position[each] + o * getGaussian(0, 1.0)* (Math.random()*1*(Math.random() > 0.5 ? 1 : -1));
//							if(newPosition[each]<=0.0){
//								newPosition[each] += Common.RANDOM.nextDouble();
//							}
						}
						double delta = atualizarConstantes(position, this.populacao.get(eachPOP)) - atualizarConstantes(newPosition, this.populacao.get(eachPOP));
						if(delta > 0){
							position = newPosition;
						}
						o *= Math.pow(Math.E, T*getGaussian(0, 1.0));

					}
					if(otimizadoIndividuo.fitness < this.populacao.get(eachPOP).fitness){
						this.populacao.set(eachPOP, (Individuo) Common.DeepCopy(otimizadoIndividuo));
						System.out.println("melhor");
					}

				}
			}

		}else if(Parametros.ESTRATEGIA_EVOLUCAO){ 
//			System.out.println("ESTRATEGIA_EVOLUCAO");
			double[] position = getConstantes(this.melhorIndividuo);
			double o = 0.5;
			double T = (1/Math.sqrt(position.length));
			if(position.length>0){
				double[] newPosition = new double[position.length];
				for (int i = 0; i < Parametros.ESTRATEGIA_EVOLUCAO_ITERACAO; i++) {
					for (int each = 0; each < position.length; each++) {
						//							if(Common.RANDOM.nextDouble()>0.5){
						//								newPosition[each] = position[each] + o * getGaussian(0, 1.0);
						//							}else{
						newPosition[each] = position[each] - o * getGaussian(0, 1.0);
						//							}
						if(newPosition[each]<=0.0){
							newPosition[each] += Common.RANDOM.nextDouble();
						}
					}
					double delta = atualizarConstantes(position, this.melhorIndividuo) - atualizarConstantes(newPosition, this.melhorIndividuo);
					if(delta > 0){
						position = newPosition;
					}
					o *= Math.pow(Math.E, T*getGaussian(0, 1.0));

				}
				if(otimizadoIndividuo.fitness < this.melhorIndividuo.fitness){
					this.melhorIndividuo = (Individuo) Common.DeepCopy(otimizadoIndividuo);
					System.out.println("melhor");
				}

			}
		} 
			
	    if(Parametros.ESTRATEGIA_EVOLUCAO_WORST){
			double[] position = getConstantes(this.populacao.get(this.populacao.size()-1));
			double o = 1.0;
			double T = (1/Math.sqrt(position.length));
			if(position.length>0){
				double[] newPosition = new double[position.length];
				for (int i = 0; i < Parametros.ESTRATEGIA_EVOLUCAO_ITERACAO; i++) {
					for (int each = 0; each < position.length; each++) {
						//							if(Common.RANDOM.nextDouble()>0.5){
						//								newPosition[each] = position[each] + o * getGaussian(0, 1.0);
						//							}else{
						newPosition[each] = position[each] - o * getGaussian(0, 1.0);
						//							}
						if(newPosition[each]<=0.0){
							newPosition[each] += Common.RANDOM.nextDouble();
						}
					}
					double delta = atualizarConstantes(position, this.populacao.get(this.populacao.size()-1)) - atualizarConstantes(newPosition, this.populacao.get(this.populacao.size()-1));
					if(delta > 0){
						position = newPosition;
					}
					o *= Math.pow(Math.E, T*getGaussian(0, 1.0));

				}
				if(otimizadoIndividuo.fitness < this.melhorIndividuo.fitness){
					this.populacao.set(this.populacao.size()-1, (Individuo) Common.DeepCopy(otimizadoIndividuo));
					System.out.println("melhor WORST");
				}

			}
		}


		//		// TODO Auto-generated method stub
		//		// EACH EPOCH iS ONE MORE INDIVIDUAL
		//		BasicNetwork network = new BasicNetwork();
		//		network.addLayer(new BasicLayer(null,true, 2));
		//		network.addLayer(new BasicLayer(new ActivationSigmoid(),true, 3));
		//		network.addLayer(new BasicLayer(new ActivationSigmoid(),false, 1));
		//		network.getStructure().finalizeStructure();
		//		network.reset();
		//
		//		// cada constante 
		//		// melhorIndividuo.noFuncao.
		//		//
		//		//           +
		//		//         /   \
		//		//        *     3
		//		//      /   \
		//		//     11   20
		//		// melhor individuo: fitness 
		//		//  xor_input = { { 11, 20, 3 } }
		//		// XORIDEAL é a resposta final que eu quero chegar da serietemporal, notepad
		//		// { {const1, const2, const3, const4 ...} }
		//
		//		// 1, 2, 3, 4, 5, 6, 7, 8, 9 Som al�
		//		//Al�, som som som, al� som ;-(
		//
		//		double XOR_INPUT[][] = prepareXOR_INPUT(melhorIndividuo); //= Arvore.getConstantes(); criar um vetor c  numeros
		//		double XOR_IDEAL[][] = prepareXOR_IDEAL(); //  serie temporal
		//
		//		MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);
		//
		//		final ResilientPropagation train = new ResilientPropagation(network, trainingSet);
		//
		//		int epoch = 1;
		//
		//		do {
		//			train.iteration();
		//			System.out.println("Epoch #" + epoch + " Error:" + train.getError());
		//			epoch++;
		//		} while(train.getError() > 0.01);
		//		train.finishTraining();
		//
		//		// test the neural network
		//		System.out.println("Neural Network Results:");
		//		for(MLDataPair pair: trainingSet ) {
		//			final MLData output = network.compute(pair.getInput());
		//			System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1)
		//					+ ", actual=" + output.getData(0) + ",ideal=" + pair.getIdeal().getData(0));
		//		}
		//
		//		PerturbationFeatureImportanceCalc d;
		//
		//		Encog.getInstance().shutdown();
	}

	private double getGaussian(double aMean, double aVariance){
		return aMean + Common.RANDOM.nextGaussian() * aVariance;
	}

	private double[] getConstantes(Individuo ind) {
		ArrayList<Double> constantesX = new ArrayList<Double>();
		constantesX = ind.getConstantes(constantesX);
		double []constantes = new double[constantesX.size()];				
		for (int ci = 0; ci < constantesX.size(); ci++) {
			constantes[ci] = (double) constantesX.get(ci);
		}
		return constantes;
	}

	private double atualizarConstantes(double[] novas, Individuo i) {
		otimizadoIndividuo = (Individuo) Common.DeepCopy(i);
		otimizadoIndividuo.atualizarConstantes(novas);
		calcularFitnessIndividuo(otimizadoIndividuo, tamanhoJanela);
		return otimizadoIndividuo.fitness;
	}

	public String toString(){
		return melhorIndividuo.toString();
	}

	private void showFitness(double[] fit) throws IOException {

		// The labels for the chart
		String[] labels = new String[fit.length];
		double[] data0 = new double[fit.length];

		for (int i = 0; i < fit.length; i++) {
			labels[i] = Integer.toString((i)*Parametros.ITERACAO_BREAK);
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
		ImageIO.write(awtImage, "png", new File(directory.getAbsolutePath() + Parametros.CONVERGENCIA+this.simulacao+".png"));
	}
}
