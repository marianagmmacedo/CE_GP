package GP;

import java.util.HashMap;
import java.util.Random;

public class Numero extends Expressao{
	
	public Numero() {
		super(random.nextDouble()+"");
	}

	@Override
	public double calcularExpressao() {
		return Double.parseDouble(this.valor);
	}

	

}
