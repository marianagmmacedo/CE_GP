package GP;

import java.util.HashMap;

public class Seno extends Expressao{

	public Seno() {
		super("seno");
		this.numeroMaximoTermo = 1;
	}

	@Override
	public double calcularExpressao() {		
		return Math.sin(nos.get(0).calcularExpressao());
	}

}
