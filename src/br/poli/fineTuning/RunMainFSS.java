package br.poli.fineTuning;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import br.poli.gp.AlgoritmoGP;
import br.poli.gp.Common;
import br.poli.gp.EInicializacao;
import br.poli.gp.Parametros;
import br.poli.output.Output;

public class RunMainFSS {

	public static void main(String[] args) throws IOException {
		double[] respostas = new double[5];
		for (int i = 0; i < 5; i++) {
			Output.outputList.add(new Output(Parametros.Bases[0]));
			FishSchoolSearch fss = new FishSchoolSearch(Parametros.Bases[0]);
			respostas[i] = fss.run(i);
			double media = Common.CalcularMedia(respostas);
			double desvio = Common.CalcularDesvioPadrao(respostas);
	
	//		System.out.println("base: " + base + "/med: " + media + "/desvio: " + desvio);
			
			File directory = new File("./");
			File f = new File(directory.getAbsolutePath()+"/resultados/thread_"+ Parametros.Bases[0] + "_FSS_MLP.txt");
			PrintWriter gravarArq = new PrintWriter(f);
		    
			Output out = Output.getOutputByList(Parametros.Bases[0]);
			
			out.texto[0] = "med: " + media + "/desvio: " + desvio;
			
		    gravarArq.write(out.toString());
	
		    gravarArq.close();
		}
//		for (int i = 0; i < Parametros.Bases.length; i++) {
//			System.out.println(Parametros.Bases[i]);
//			FishSchoolSearch fss = new FishSchoolSearch(Parametros.Bases[i]);
//			
//		}
		
		
	}

}
