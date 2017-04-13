package br.poli.gp.arvore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.poli.gp.arvore.funcao.Numero;
import br.poli.gp.arvore.funcao.Potencia;
import br.poli.gp.arvore.funcao.RaizQuadrada;
import br.poli.gp.arvore.funcao.Soma;
import br.poli.gp.arvore.funcao.Variavel;

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
			if(nos!=null){
			for(Funcao f : nos){
				noFuncao.add(f);
				f.atualizarReferenciaNosFuncao(this, noFuncao);
			}
		}
	}
	
	public Funcao otimizarFuncao(Arvore raiz, Funcao pai, int elementIndex){
		
		//Se a instância for variável
		if (this instanceof Variavel){
			//Soma
			if (pai != null){
				if (pai instanceof Soma){
				List<Funcao> fTemp = new ArrayList<Funcao>();
				for (Funcao no : pai.nos){
					if (no instanceof Numero && ((Numero)no).valorNumerico == 0d){
						fTemp.add(no);
					}
				}
				pai.nos.removeAll(fTemp);
				
				if (pai.nos.size() == 1){
					if (pai.pai != null){
						pai.pai.nos.set(pai.pai.nos.indexOf(pai), this);
					}
				}
			}
			return null;
		}
		
		//Se ela for número
		if (this instanceof Numero) return this;
				
		boolean podeOtimizar = true;
		
		for (int i = 0; i < this.nos.size(); i++){
			podeOtimizar = podeOtimizar && (nos.get(i).otimizarFuncao(raiz, this, i) != null);
		}
		
		if (podeOtimizar){
			Numero n = new Numero(this.calcularExpressao(null));
			if (pai!=null){
				pai.nos.set(elementIndex, n);
				return pai;
			} else{
				return raiz.no = n;
			}
		}
		
		if (podeOtimizar){
			return pai;
		} else {
			return null;
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