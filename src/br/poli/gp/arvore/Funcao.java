package br.poli.gp.arvore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.encog.ensemble.training.LevenbergMarquardtFactory;

import br.poli.gp.arvore.funcao.Divisao;
import br.poli.gp.arvore.funcao.Multiplicacao;
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

	public Funcao otimizarFuncao(Funcao pai){

		if (this instanceof Numero || this instanceof Variavel) return this;

		esquerda = esquerda.otimizarFuncao(this);
		
		if (direita!=null){
			direita = direita.otimizarFuncao(this);
		}

		if (this instanceof Soma){
			//Replace + x 0 with x.
			Numero numero = (Numero)((esquerda instanceof Numero)?esquerda:(direita instanceof Numero)?direita:null);

			if (numero!=null && numero.valorNumerico == 0d){
				return (esquerda != numero)?esquerda:direita;
			}
		} else if (this instanceof Subtracao){
			//Replace - x x with 0.
			Variavel variavel = (Variavel)((esquerda instanceof Variavel)?esquerda:(direita instanceof Variavel)?direita:null);

			if (variavel!=null){
				Funcao temp = ((esquerda!=variavel)?esquerda:direita);
				if (temp instanceof Variavel && temp.valor.equals(variavel.valor)){
					return new Numero(0d);
				}
			}
		} else if (this instanceof Multiplicacao){
			//Replace * x 0 with 0.
			//Replace * x 1 with x.
			Numero numero = (Numero)((esquerda instanceof Numero)?esquerda:(direita instanceof Numero)?direita:null);

			if (numero!=null){
				if (numero.valorNumerico == 0d){
					return numero;
				} else if (numero.valorNumerico == 1d){

					return  (esquerda != numero)?esquerda:direita;
				}
			}
		} else if (this instanceof Divisao){
			//Replace / 0 x with 0.
			//Replace / x x with 1.
			Numero numero = (Numero)((esquerda instanceof Numero)?esquerda:(direita instanceof Numero)?direita:null);
			
			if (numero!=null){
				if (numero.valorNumerico == 0d){
					return numero;
				}

				/* Replace / x 1 with x.
				if ((numero==direita) && numero.valorNumerico == 1d){
					return esquerda;
				}*/
			} 
			
			Variavel variavel = (Variavel)((esquerda instanceof Variavel)?esquerda:(direita instanceof Variavel)?direita:null);
			
			if (variavel != null && variavel.valor.equals(((esquerda!=variavel)?esquerda:direita).valor)){
				return new Numero(1d);
			}
		}
		
		if (esquerda instanceof Numero && direita != null && direita instanceof Numero){
			return new Numero(this.calcularExpressao(null));
		}

		return this;
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

	public void parseToDoubleList(HashMap<String, Double> variableValues, List<Double> doubleList) {
		if (this instanceof Numero) {
			doubleList.add(((Numero)this).valorNumerico);	
			return;
		}
		
		if (this instanceof Variavel) {
			doubleList.add(variableValues.get(this.valor));
			return;
		}
		
		esquerda.parseToDoubleList(variableValues, doubleList);
		
		if (direita != null)
			direita.parseToDoubleList(variableValues, doubleList);
	}
}