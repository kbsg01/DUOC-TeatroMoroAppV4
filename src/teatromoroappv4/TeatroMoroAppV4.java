package teatromoroappv4;

import java.text.DecimalFormat;
import java.util.Scanner;

public class TeatroMoroAppV4 {
    //Atributos de entrada
    static String zona = "";
    static String tipoCliente = "";
    static int tarifaBase = 0;
    static int edadCliente = 0;
    static int nEntradas = 0;
    static int valorTotal = 0;
    
    //Información del sistema
    static final String[] zonas = {"VIP", "Platea Baja", "Platea Alta", "Palcos"};
    static final String[] tipoTarifa = {"Estudiante", "Tercera Edad", "General"};
    static final int[] tarifasGeneral = {30000, 15000, 18000, 13000};
    static final DecimalFormat df = new DecimalFormat("#,###");
    
    static int entradasVendidas = 0;
    static int maxEntradas = 100;
    static int entradasDisponibles = maxEntradas - entradasVendidas;
    static int distribucionEntradas[] = calcularDistribucionEntradas();
    static int numeroEntrada = 0;
    static double ingresosTotales = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            infoSistema();
            menuPrincipal();
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                // TODO: Implementar la lógica para cada opción
                case 1 -> System.out.println("Opción reserva");
                case 2 -> System.out.println("Opción compra");
                case 3 -> System.out.println("Opción modificación");
                case 4 -> System.out.println("Opción imprimir boleto");
                case 5 -> System.out.println("Gracias por usar el sistema."); 
                default -> System.out.println("Opción inválida.");
            }

        } while (opcion != 7);

        sc.close();
    }

    private static void infoSistema() {
        System.out.println("================================================");
        System.out.println("> Sistema de Gestión de Entradas - Teatro Moro <");
        System.out.println("================================================");
        System.out.println("Entradas vendidas: " + entradasVendidas);
        System.out.println("Entradas disponibles: " + entradasDisponibles);
        System.out.println("Distribución de entradas por zona: ");
        for (int i = 0; i < zonas.length; i++) {
            System.out.println(zonas[i] + ": " + distribucionEntradas[i]);
        }
        System.out.println("Ingresos totales: $" + df.format(ingresosTotales));
        System.out.println("================================================");
    }

    public static void menuPrincipal(){
        System.out.println("\n===============================================");
        System.out.println("1. Reservar entradas");
        System.out.println("2. Comprar entradas");
        System.out.println("3. Modificar compra");
        System.out.println("4. Imprimir boleto");
        System.out.println("5. Salir");
        System.out.println("================================================");
        System.out.print("\n> Seleccione una opción: ");
    }

    public static int[] calcularDistribucionEntradas() {
        int[] distribucion = new int[zonas.length];
        for (int i = 0; i < zonas.length; i++) {
            distribucion[i] = maxEntradas / zonas.length;
        }
        return distribucion;
    }
}
