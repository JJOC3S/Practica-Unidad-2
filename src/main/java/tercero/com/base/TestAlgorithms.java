package tercero.com.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import tercero.com.base.controller.DataStruc.List.LinkedList;
import tercero.com.base.controller.exceptions.ListEmptyException;

public class TestAlgorithms {


    public static LinkedList<Integer> leerEnteros() throws IOException {
        String ruta = "data" + File.separator + "numeros.txt";
        LinkedList<Integer> lista = new LinkedList<>();
        BufferedReader br = new BufferedReader(new FileReader(ruta));
        String linea;

        while ((linea = br.readLine()) != null) {
            Integer numero = Integer.parseInt(linea.trim());
            lista.add(numero);
        }


        br.close();

        return lista;
    }

    public static void main(String[] args) throws ListEmptyException {
        LinkedList<Integer> lista = new LinkedList<>();
        LinkedList<Integer> lista2 = new LinkedList<>();
        LinkedList<Integer> lista3 = new LinkedList<>();
        LinkedList<Integer> lista4 = new LinkedList<>();

        try {
            lista = leerEnteros();
            lista2 = leerEnteros();
            lista3 = leerEnteros();
            lista4 = leerEnteros();
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }

        // Agregar n elementos random a las listas
        int randomSize = 15000;
        for (int i = 0; i < randomSize; i++) {
            lista3.add((int) (Math.random() * randomSize));
            lista4.add((int) (Math.random() * randomSize));
        }

        System.out.println("         COMPARACION DE ALGORITMOS       ");
        System.out.println("==========================================");

        System.out.println("Resultados con " + lista.getLength() + " datos del archivo:");
        System.out.println("------------------------------------------");

        // Ordenacion por Shell
        int startTime = (int) System.currentTimeMillis();
        lista.shellSort(1);
        int difTime = (int) System.currentTimeMillis() - startTime;
        System.out.printf("Shell Sort: %6d ms\n", difTime);
        // Ordenacion por Quick
        startTime = (int) System.currentTimeMillis();
        lista2.quickSort(1);
        difTime = (int) System.currentTimeMillis() - startTime;
        System.out.printf("Quick Sort: %6d ms", difTime);

        System.out.println("\nResultados con " + lista3.getLength() + " datos (archivo + aleatorios):");
        System.out.println("------------------------------------------");

        // Ordenacion por Shell
        startTime = (int) System.currentTimeMillis();
        lista3.shellSort(1);
        difTime = (int) System.currentTimeMillis() - startTime;
        System.out.printf("Shell Sort: %6d ms\n", difTime);

        // Ordenacion por Quick
        startTime = (int) System.currentTimeMillis();
        lista4.quickSort(1);
        difTime = (int) System.currentTimeMillis() - startTime;
        System.out.printf("Quick Sort: %6d ms\n", difTime);

    }
}