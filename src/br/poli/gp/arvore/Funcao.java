package br.poli.gp.arvore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.poli.gp.arvore.funcao.Numero;
import br.poli.gp.arvore.funcao.Potencia;
import br.poli.gp.arvore.funcao.RaizQuadrada;
import br.poli.gp.arvore.funcao.Soma;
import br.poli.gp.arvore.funcao.Subtracao;
import br.poli.gp.arvore.funcao.Variavel;

public abstract class Funcao implements Serializable {

	private static final long serialVersionUID = -885342105895835143L;

	public String valor;
	public Funcao esquerda, direita;
	public boolean apenasNoEsquerdo;
	public Funcao pai;
	public Arvore arvore;

	public Funcao(){}

	public Funcao(String valor){
		this.valor = valor;
	}

	public abstract double calcularExpressao(HashMap<String, Double> hm);

	public abstract String toString();
	
	public void corrigirNos(Funcao pai, List<Funcao> noFuncao, Arvore arvore){
		noFuncao.add(this);
		this.arvore = arvore;
		
		if (esquerda!=null)
			esquerda.corrigirNos(this, noFuncao, arvore);
		
		if (direita!=null)
			direita.corrigirNos(this, noFuncao, arvore);
		
	}

	public boolean otimizarFuncao(Funcao pai){

		//Se ela for variavel
		if (this instanceof Variavel) return false;

		//Se ela for numero
		if (this instanceof Numero) return true;

		boolean podeOtimizar = true;

		if (direita==null) {
			podeOtimizar = podeOtimizar && esquerda.otimizarFuncao(this);
		} else {
			boolean temp1 = esquerda.otimizarFuncao(this);
			boolean temp2 = direita.otimizarFuncao(this);
			podeOtimizar = podeOtimizar && temp1 && temp2;			
		}

		if (podeOtimizar){
			Numero n = new Numero(this.calcularExpressao(null));
			if (pai!=null){
				if (pai.esquerda == this){
					pai.esquerda = n;
				} else {
					pai.direita = n;
				}				
			} else {
				arvore.no = n;
			}
		}
		
		return podeOtimizar;
	}


	public void crossover(Funcao fFilha) {

		Funcao paiFilha = fFilha.pai;

		if (paiFilha==null){
			fFilha.arvore.no = this;
		} else {
			if (paiFilha.esquerda == fFilha){
				paiFilha.esquerda = this;
			} else {
				paiFilha.direita = this;
			}

			fFilha.pai = this.pai;
		}

		if (pai == null){
			arvore.no = fFilha;
		} else {
			if (pai.esquerda == this){
				pai.esquerda = fFilha;
			} else {
				pai.direita = fFilha;
			}

			this.pai = paiFilha;
		}

		Arvore temp = this.arvore;
		this.arvore = fFilha.arvore;
		fFilha.arvore = temp;

	}
}