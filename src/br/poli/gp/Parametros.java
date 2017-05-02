package br.poli.gp;

public class Parametros {
	public static final int TAMANHO_MAXIMO_PROFUNDIDADE_ARVORE = 10;
	public static final int NUMERO_TOTAL_SIMULACAO = 1;
	public static final int NUMERO_TOTAL_ITERACAO = 40;
	public static final int NUMERO_TOTAL_FUNCAO = 10;
	public static final int NUMERO_TOTAL_VARIAVEL = 2; //(TAMANHO DA JANELA)
	public static final int NUMERO_MAXIMO_POLPULACAO = 30;
	public static final double CHANCE_CRIACAO_VARIAVEL = 0.5d; 
	public static final double TAXA_CRUZAMENTO_MUTACAO = 0.2;
	public static final int TAMANHO_MAXIMO_MUTACAO_PROFUNDIDADE = 3;
	public static final String NOME_CAMINHO_SALVAR_FITNESS = "/resultados/fitness.png";
	public static final int NUMERO_MAXIMO_GERACAO_SIMULATEDANNEALING = 10;
	public static final double TEMPERATURA_FINAL_SIM_ANN = 1;
	public static final double TEMPERATURA_INICIAL_SIM_ANN = 1000;
	public static final double ALFA_SIM_ANN = 0.9;
	public static final int CADA_GRAFICO = 20;
	public static String TIPO_DE_OTIMIZACAO = "MINIMIZACAO";
}
