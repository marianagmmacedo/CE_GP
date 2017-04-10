package GP;

public class Subtracao extends Expressao{

	public Subtracao() {
		super("-");
		this.numeroMaximoTermo = Parametros.tamanhoMaximoArvoreLargura;
	}

	@Override
	public double calcularExpressao() {
		double subtracao = 0.0;
		for (int numeroNos = 0; numeroNos < this.nos.size(); numeroNos++) {
			subtracao -= this.nos.get(numeroNos).calcularExpressao();
		}
		return subtracao;
	}

}
