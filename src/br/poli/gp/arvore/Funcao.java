package br.poli.gp.arvore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Funcao implements Serializable {

	private static final long serialVersionUID = -885342105895835143L;
	
	public String valor;
	public int numeroMaximoTermo;
	public List<Funcao> nos;
	public Funcao pai;
	
	public static HashMap<String, Integer> index; // PRECISA?

	public Funcao(){}
	
	public Funcao(String valor){
		this.nos = new ArrayList<Funcao>();
		this.valor = valor;
	}

	public abstract double calcularExpressao(HashMap<String, Double> hm);
	
	public String toString(){
		String temp = valor;
		for(Funcao e : nos){
			temp += "\n[" + e.toString() + "]";
		}
		return temp;
	}

	public void atualizarReferenciaNosFuncao(Funcao pai, List<Funcao> noFuncao) {
		this.pai = pai;
		for(Funcao f : nos){
			noFuncao.add(f);
			f.atualizarReferenciaNosFuncao(this, noFuncao);
		}
	}

	public void crossover(Funcao fFilha) {
		
		Funcao paiFilha = fFilha.pai;
		
		if (paiFilha != null){
			paiFilha.nos.remove(fFilha);
			paiFilha.nos.add(this);
		}
		
		fFilha.pai = this.pai;
		
		if (this.pai != null){
			this.pai.nos.remove(this);
			this.pai.nos.add(fFilha);
		}
		
		this.pai = paiFilha;
	}
	
	public void mutacao(Funcao fFilha) {
		
		Funcao paiFilha = fFilha.pai;
		
		if (paiFilha != null){
			paiFilha.nos.remove(fFilha);
			paiFilha.nos.add(this);
		}
		
		fFilha.pai = this.pai;
		
		if (this.pai != null){
			this.pai.nos.remove(this);
			this.pai.nos.add(fFilha);
		}
		
		this.pai = paiFilha;
	}

}