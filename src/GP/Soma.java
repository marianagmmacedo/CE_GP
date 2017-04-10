package GP;

import java.util.HashMap;

public class Soma extends Expressao {

	public Soma() {
		super("+");
		this.numeroMaximoTermo = Parametros.tamanhoMaximoArvoreLargura;
	}

	@Override
	public double calcularExpressao() {
		double soma = 0.0;
		for (int numeroNos = 0; numeroNos < this.nos.size(); numeroNos++) {
			soma += this.nos.get(numeroNos).calcularExpressao();
		}
		return soma;
	}

	
}
