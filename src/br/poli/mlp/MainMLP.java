package br.poli.mlp;

import java.io.IOException;

import br.poli.gp.Parametros;

public class MainMLP {

	public static void main(String[] args) throws IOException {
//		{"sunspot0", "lynx1", "stock2", "redwine3", "accidentalDeathUSA4", "airlines5",
//		"coloradoRiver6", "dowJones7", "electricity8", "lakeerie9", "ibm10", "nsw11",
//		  "pollution12"};
		String base = Parametros.Bases[1];
		System.out.println(base);
		MultiLayerPerceptron mlp = new MultiLayerPerceptron(base, 20, null, -1);
		mlp.forwardBackward();
		mlp.evaluate(base, false);
	}

}
