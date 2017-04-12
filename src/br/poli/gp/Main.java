package br.poli.gp;

import java.util.HashMap;
import java.util.Random;

import br.poli.gp.arvore.Funcao;

public class Main {

	public static void main(String[] args) {
		
		AlgoritmoGP gp = new AlgoritmoGP();
		AlgoritmoGP clone = new AlgoritmoGP();
		
		clone.populacao.set(0, (Individuo)Common.DeepCopy(gp.populacao.get(0)));
				
		System.out.println("GP:" + gp.toString());
		System.out.println("CL:" + clone.toString());
		
		
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		hm.put("x", 0);
		
		Funcao.index = hm;
		for (int x = 0; x < 10; x++){
			hm.replace("x", x);
			gp.calcularValor();
		}
	}
}
