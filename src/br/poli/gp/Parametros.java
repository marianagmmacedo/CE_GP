package br.poli.gp;

public class Parametros {
	
	// Configuracao experimento
	public static final int NUMERO_TOTAL_SIMULACAO = 1;
	public static final int NUMERO_TOTAL_ITERACAO = 10000;
	public static final int ITERACAO_BREAK = 500;
	public static String TIPO_DE_OTIMIZACAO = "MINIMIZACAO";
	
	// Bases
	//public static final String Base = "ibm"; 
	//public static final String Base = "sunspot"; 
	// public static final String Base = "stock"; OK
	//public static final String Base = "redwine"; 
	//public static final String Base = "accidentalDeathUSA"; OK
	//public static final String Base = "airlines"; OK
	//public static final String Base = "coloradoRiver"; OK
	//public static final String Base = "dowJones"; OK
	public static final String Base = "electricity"; 
	//public static final String Base = "lakeerie"; OK
	//public static final String Base = "lynx"; 
	//public static final String Base = "nsw"; OK
	//public static final String Base = "pollution";
		
	// Configuracao arvore
	public static final int TAMANHO_MAXIMO_PROFUNDIDADE_ARVORE = 8;
	public static final int NUMERO_TOTAL_FUNCAO = 8;
	public static final int NUMERO_TOTAL_VARIAVEL = 2; //(TAMANHO DA JANELA)
	public static final int NUMERO_MAXIMO_POLPULACAO = 5;
	public static final double CHANCE_CRIACAO_VARIAVEL = 0.5d; 
	public static final double TAXA_CRUZAMENTO_MUTACAO = 0.7;
	public static final int TAMANHO_MAXIMO_MUTACAO_PROFUNDIDADE = 4;
	public static final String NOME_CAMINHO_SALVAR_FITNESS = "/resultados/"+Base+"/"+Base+"_mean_"+NUMERO_TOTAL_SIMULACAO;
	public static final String SERIES = "/resultados/"+Base+"/"+Base;
	public static final String CONVERGENCIA = "/resultados/"+Base+"/"+Base+"_convergencia_";
	public static final int NUMERO_MAXIMO_GERACAO_SIMULATEDANNEALING = 10;
	
	
	// Configuracao Simulated Annealing
	public static final double TEMPERATURA_FINAL_SIM_ANN = 0.1;
	public static final double TEMPERATURA_INICIAL_SIM_ANN = 0.9;
	public static final double ALFA_SIM_ANN = 0.001;
	
	public static final boolean OTIMIZAR = true;
	public static final boolean SIMULATED_ANNEALING = false;
	public static final boolean HILL_CLIMBING = false;
	public static final boolean VARIAR_JANELA = false;
	public static final boolean ESTRATEGIA_EVOLUCAO = true;
	
	public static final int HILL_CLIMBING_MAX_ITERACAO = 10;
	public static final int NUMERO_MAXIMO_VARIAVEL = 10;
	public static final int ESTRATEGIA_EVOLUCAO_ITERACAO = 200;
	public static final double TAXA_VALIDACAO = 0.30;
	public static final boolean ESTRATEGIA_EVOLUCAO_TODOS = false;
	
	
}
