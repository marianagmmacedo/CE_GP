package GP;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Arvore {

	
	private Expressao nos;
	private int tamanhoAtualArvore;

	public Expressao getNos() {
		return nos;
	}

	public void setNos(Expressao nos) {
		this.nos = nos;
	}

	public String toString(){
		return ("\n[nos:" + nos.toString() + "]");
	}

	public double calcularValor() {
		return nos.calcularExpressao();
	}
    
}
