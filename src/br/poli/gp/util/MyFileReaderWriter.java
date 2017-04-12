package br.poli.gp.util;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;


public class MyFileReaderWriter {

    public static List<String> createListFromFile(String path){

        Charset charsetUTF8 = Charset.forName("ISO-8859-1");
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(path), charsetUTF8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static void createFileFromList(List<String> lines, String fileName){

        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter(fileName, true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(String line : lines){
            out.println(line);
        }
        out.close();
    }

    public static void createBestFitnessEvolutionFile(double matrix[][], String fileName){

        PrintWriter out = null;
        NumberFormat formatter = new DecimalFormat("#########.#######");
        
        try {
            out = new PrintWriter(new FileWriter(fileName, false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        out.print(";");
        
        for (int i = 0; i < matrix[0].length; i++) {
			out.print("Simulation "+(i+1)+";");
		}
        
        out.println();
        
        for (int i = 0; i < matrix.length; i++) {
        	out.print("Iteration "+(i+1)+";");
            for (int j = 0; j < matrix[0].length; j++) {
                out.print(formatter.format(matrix[i][j]));
                if(j == matrix[0].length - 1){
                    out.println();
                }else{
                    out.print(";");
                }
            }
        }
        out.close();
    }

    public static void createFileFromVector(double vector[], String fileName){
        PrintWriter out = null;
        NumberFormat formatter = new DecimalFormat("#########.#######");
        
        try {
            out = new PrintWriter(new FileWriter(fileName, false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("matrix.length = "+matrix.length);
        for (int i = 0; i < vector.length; i++) {
            out.println(formatter.format(vector[i]));
        }
        out.close();
    }

    public static void createFileFromVectorBoxplot(double vector[], String fileName){
        PrintWriter out = null;
        NumberFormat formatter = new DecimalFormat("#########.#######");
        try {
            out = new PrintWriter(new FileWriter(fileName, false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("matrix.length = "+matrix.length);
        for (int i = 0; i < vector.length; i++) {
            out.println(formatter.format(vector[i]));
        }
        out.close();
    }
    
    public static void createFileFromDouble(double value, String fileName){
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter(fileName, false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println(value);
        out.close();
    }

    public static double[] createVectorFromMatrix(double matrix[][]){

        double vector[] = new double[matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                vector[i] += matrix[i][j];
            }
            vector[i] /= matrix[0].length;
        }
        return vector;
    }

    public static double[] createBestFitnessVector(double matrix[][]){

       double vector[] = new double[matrix[0].length];
        
        for (int i = 0; i < matrix[0].length; i++) {
            vector[i] = matrix[matrix.length-1][i];
        }
        return vector;
    }

    public static String changeSubstring(String string, String from, String to){
        return string.replace(from, to);
    }
    
    public static void writeLineAtEndOfFile(String line, String fileName){

        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter(fileName, true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println(line);
        out.close();
    }
}
