package GP;

import java.util.HashMap;
import java.util.Random;

public class Main {

	public static void main(String[] args) {
		
		AlgoritmoGP gp = new AlgoritmoGP();
		System.out.println(gp.toString());
		
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		hm.put("x", 0);
		
		Expressao.index = hm;
		for (int x = 0; x < 10; x++){
			hm.replace("x", x);
			gp.calcularValor();
		}
	}

}
