package GP;

import java.util.HashMap;

public class Variavel extends Expressao{

	public Variavel(String valor) {
		super(valor);
	}

	@Override
	public double calcularExpressao() {
		return this.index.get(this.valor);
	}

}
