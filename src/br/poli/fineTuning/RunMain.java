package br.poli.fineTuning;

import java.io.IOException;
import java.util.HashMap;

import br.poli.gp.AlgoritmoGP;
import br.poli.gp.Common;
import br.poli.gp.EInicializacao;
import br.poli.gp.Parametros;

public class RunMain {

	public static void main(String[] args) throws IOException {
		
//		for (int i = 0; i < 1; i++) {
		for (int i = 0; i < Parametros.Bases.length; i++) {
			System.out.println(Parametros.Bases[i]);
			FishSchoolSearch fss = new FishSchoolSearch(Parametros.Bases[i]);
			
		}
		
//		}
		
	}

}
