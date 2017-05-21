package br.poli.gp;

public class Parametros {
	
	// Configuracao experimento
	public static final int NUMERO_TOTAL_SIMULACAO = 30;
	public static final int NUMERO_TOTAL_ITERACAO = 100;//100,6000
	public static final int ITERACAO_BREAK = 1;
	public static final String TIPO_DE_OTIMIZACAO = "MINIMIZACAO";
	
	// Bases
	public static final String[] Bases = {"sunspot", "lynx", "stock", "redwine", "accidentalDeathUSA", "airlines",
										"coloradoRiver", "dowJones", "electricity", "lakeerie", "ibm", "nsw",
										  "pollution"};

	
	//public static final String Base = "ibm"; 
	//public static final String Base = "sunspot"; 
	//public static final String Base = "stock"; 
	//public static final String Base = "redwine"; 
	//public static final String Base = "accidentalDeathUSA"; 
	//public static final String Base = "airlines"; 
	//public static final String Base = "coloradoRiver"; 
	//public static final String Base = "dowJones"; 
	//public static final String Base = "airlines"; 
	//public static final String Base = "coloradoRiver"; OK
	//public static final String Base = "dowJones"; OK
	//public static final String Base = "electricity"; 
	//public static final String Base = "lakeerie"; 
	//public static final String Base = "lynx"; 
	//public static final String Base = "nsw"; 
	//public static final String Base = "pollution";
		
	// Configuracao arvore
	public static final int TAMANHO_MAXIMO_PROFUNDIDADE_ARVORE = 6;
	public static final int NUMERO_TOTAL_FUNCAO = 9;
	public static final int NUMERO_TOTAL_VARIAVEL = 2; //(TAMANHO DA JANELA)
	public static final int NUMERO_MAXIMO_POPULACAO = 20; //20
	public static final int TAMANHO_MAXIMO_MUTACAO_PROFUNDIDADE = 3;
	// 3
	
	public static final double CHANCE_CRIACAO_VARIAVEL = 0.5d; 
	public static final double TAXA_CRUZAMENTO_MUTACAO = 1.00d;
	public static final String TAXA_CRUZAMENTO_MUTACAO_DECRESCENTE = "EXPONENCIAL"; //"LINEAR", "EXPONENCIAL" ou "Nenhuma"
	public static final double TAXA_CRUZAMENTO_MUTACAO_DECAIMENTO_EXPONENCIAL = 2.0d;
	
	
	//Output
//	public static final String NOME_CAMINHO_SALVAR_FITNESS = "/resultados/"+Base+"/"+Base+"_mean_"+NUMERO_TOTAL_SIMULACAO;
//	public static final String SERIES = "/resultados/"+Base+"/"+Base;
//	public static final String CONVERGENCIA = "/resultados/"+Base+"/"+Base+"_convergencia_";

	//Otimizacao
	public static final boolean OTIMIZAR = false;
	public static final boolean OTIMIZAR_SEMPRE = false;
	public static final boolean ESTRATEGIA_EVOLUCAO_WORST = false;
	public static final boolean SIMULATED_ANNEALING = false;
	public static final boolean HILL_CLIMBING = false;
	public static final boolean VARIAR_JANELA = false;
	public static final boolean ESTRATEGIA_EVOLUCAO = false;
	//Muta��o de Descendente
	public static final boolean MUTACAO_CLASSIFICADA = false;
	
	//Se ativo, o fitness calculado ir� ignorar express�es Infinitas ou NaN ao inv�s de reescrever o indiv�duo
	public static final boolean GP_CANONICA = false;
	
	//Configuracao Simulated Annealing
	public static final double TEMPERATURA_FINAL_SIM_ANN = 0.1;
	public static final double TEMPERATURA_INICIAL_SIM_ANN = 0.9;
	public static final double ALFA_SIM_ANN = 0.001;
	public static final int NUMERO_MAXIMO_GERACAO_SIMULATEDANNEALING = 10;
	
	//Configuracao Hill Climbing
	public static final int HILL_CLIMBING_MAX_ITERACAO = 10;
	public static final int NUMERO_MAXIMO_VARIAVEL = 10;
	
	//Configuracao Estrategia Evolucionaria
	public static final int ESTRATEGIA_EVOLUCAO_ITERACAO = 20;
	public static final double TAXA_VALIDACAO = 0.20;
	public static final boolean ESTRATEGIA_EVOLUCAO_TODOS = false;
	
	//Adicionar Novos Individuos enquanto roda o codigo
	public static final int TAMANHO_NOVOS_INDIVIDUOS = 4;
	public static final int NUMERO_NOVOS_INDIVIDUOS = 5;
	public static final boolean GERAR_NOVOS_INDIVIDUOS = false;
	public static final boolean SIMPLIFICAR_ARVORE = true;
	
	
}
