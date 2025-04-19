package teatromoroappv4;

import java.text.DecimalFormat;
import java.util.Scanner;

public class TeatroMoroAppV4 {
    static int entradasVendidas = 0;
    static double ingresosTotales = 0;
    static int numeroBoleta = 1;

    static int vipCounter = 1;
    static int plateaBajaCounter = 1;
    static int plateaAltaCounter = 1;
    static int palcosCounter = 1;

    static final int MAX_ENTRADAS = 100;
    static final String[] zonas = { "VIP", "Platea Baja", "Platea Alta", "Palcos" };
    static final int[] tarifas = { 30000, 15000, 18000, 13000 };
    static final DecimalFormat df = new DecimalFormat("#,###");

    static String zonaSeleccionada = "";
    static int cantidadEntradas = 0;
    static String tipoCliente = "";
    static int edadCliente = 0;
    static int totalPagar = 0;
    static boolean hayReserva = false;
    static boolean hayCompra = false;
    static String[] identificadoresEntradas;

    static int vipDisponibles = MAX_ENTRADAS / 4;
    static int plateaBajaDisponibles = MAX_ENTRADAS / 4;
    static int plateaAltaDisponibles = MAX_ENTRADAS / 4;
    static int palcosDisponibles = MAX_ENTRADAS / 4;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            infoSistema();
            menuPrincipal();
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1 -> repetirReserva(sc);
                case 2 -> repetirCompra(sc);
                case 3 -> modificarCompra(sc);
                case 4 -> imprimeBoletos();
                case 5 -> cancelarReserva();
                case 6 -> System.out.println("Gracias por usar el sistema.");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 6);
        sc.close();
    }

    private static void infoSistema() {
        System.out.println("================================================");
        System.out.println("> Sistema de Gestión de Entradas - Teatro Moro <");
        System.out.println("================================================");
        System.out.println("Entradas vendidas: " + entradasVendidas);
        System.out.println("Entradas disponibles: " + (MAX_ENTRADAS - entradasVendidas));
        System.out.println("Distribución de entradas por zona: ");
        System.out.println("VIP: " + vipDisponibles + " entradas disponibles");
        System.out.println("Platea Baja: " + plateaBajaDisponibles + " entradas disponibles");
        System.out.println("Platea Alta: " + plateaAltaDisponibles + " entradas disponibles");
        System.out.println("Palcos: " + palcosDisponibles + " entradas disponibles");
        System.out.println("Ingresos totales: $" + df.format(ingresosTotales));
        System.out.println("================================================");
    }

    public static void menuPrincipal() {

        System.out.println("\n===============================================");
        System.out.println("Menú Principal");
        System.out.println("===============================================");
        System.out.println("1. Reservar entradas");
        System.out.println("2. Comprar entradas");
        System.out.println("3. Modificar compra");
        System.out.println("4. Imprimir entradas");
        System.out.println("5. Cancelar reserva");
        System.out.println("6. Salir");
        System.out.println("================================================");
        System.out.print("\n> Seleccione una opción: ");
    }

    private static void repetirReserva(Scanner sc) {
        do {
            reservaEntradas(sc);
            System.out.print("¿Desea reservar más entradas? (s/n): ");
        } while (sc.nextLine().equalsIgnoreCase("s"));
    }

    private static void repetirCompra(Scanner sc) {
        do {
            comprarEntradas(sc);
            System.out.print("¿Desea comprar más entradas? (s/n): ");
        } while (sc.nextLine().equalsIgnoreCase("s"));
    }

    private static void comprarEntradas(Scanner sc) {
        if (!hayReserva) {
            System.out.println("No hay reserva activa. ¿Desea ingresar sus datos para comprar directamente? (s/n)");
            if (sc.nextLine().equalsIgnoreCase("s")) {
                reservaEntradas(sc);
            } else {
                return;
            }
        }

        int tarifaBase = tarifas[obtenerIndiceZona(zonaSeleccionada)];
        double subtotal = tarifaBase * cantidadEntradas;
        double descuento = 0;

        if (tipoCliente.equals("Estudiante"))
            descuento = 0.10;
        else if (tipoCliente.equals("Tercera edad"))
            descuento = 0.15;
        else if (cantidadEntradas >= 5)
            descuento = 0.20;

        totalPagar = (int) (subtotal * (1 - descuento));

        actualizarDisponibilidad(zonaSeleccionada, cantidadEntradas);
        entradasVendidas += cantidadEntradas;
        ingresosTotales += totalPagar;

        hayCompra = true;
        hayReserva = false;

        System.out.println("Compra confirmada.");
        imprimirBoleta();
        System.out.println("================================================");
    }

    private static void reservaEntradas(Scanner sc) {
        System.out.println("\n================================================");
        System.out.println("Reserva de entradas");
        System.out.println("\nIngrese su edad: ");
        edadCliente = sc.nextInt();// PUNTO DE DEPURACIÓN 1: edadCliente debe mostrar el valor ingresado por el usuario
        sc.nextLine();

        
        // Define tarifa
        if (edadCliente < 0) {
            System.out.println("Edad inválida. Intente nuevamente.");
            return;
        } else if (edadCliente < 18) {
            tipoCliente = "Estudiante";
        } else if (edadCliente >= 60) {
            tipoCliente = "Tercera edad";
        } else {
            tipoCliente = "General";
        }
        System.out.println("\nTarifa aplicada: " + tipoCliente);
        System.out.println("================================================");

        
        // Seleccionar zona
        mostrarPlanoTeatro();
        System.out.println("\nSeleccione la zona: ");
        int opcionZona = sc.nextInt();// PUNTO DE DEPURACIÓN 2: opcionZona debe indicar la zona elegida (1 a 4)
        sc.nextLine();

        System.out.println("Ingrese la cantidad de entradas: ");
        cantidadEntradas = sc.nextInt();
        sc.nextLine();

        zonaSeleccionada = zonas[opcionZona - 1];
        boolean disponible = verificarDisponibilidad(zonaSeleccionada, cantidadEntradas);
        // PUNTO DE DEPURACIÓN 3: cantidadEntradas y zonaSeleccionada deben tener valores válidos
        if (disponible) {
            identificadoresEntradas = generaId(zonaSeleccionada, cantidadEntradas);
            System.out.println("Entradas reservadas con éxito.");
            System.out.println("Favor vuelve al menú principal y selecciona la opción 2 para confirmar la compra.");
            System.out.println("===============================================");
            hayReserva = true;
            hayCompra = false;
        } else {
            System.out.println("No hay entradas disponibles en la zona seleccionada.");
            hayCompra = false;
            hayReserva = false;
        }
    }

    private static boolean verificarDisponibilidad(String zona, int cantidad) {
        return switch (zona) {
            case "VIP" -> cantidad <= vipDisponibles;
            case "Platea Baja" -> cantidad <= plateaBajaDisponibles;
            case "Platea Alta" -> cantidad <= plateaAltaDisponibles;
            case "Palcos" -> cantidad <= palcosDisponibles;
            default -> false;
        };
    }

    private static void actualizarDisponibilidad(String zona, int cantidad) {
        switch (zona) {
            case "VIP" -> vipDisponibles -= cantidad;
            case "Platea Baja" -> plateaBajaDisponibles -= cantidad;
            case "Platea Alta" -> plateaAltaDisponibles -= cantidad;
            case "Palcos" -> palcosDisponibles -= cantidad;
        }
    }

    private static void modificarCompra(Scanner sc) {
        if (!hayReserva && !hayCompra) {
            System.out.println("No hay ninguna compra o reserva registrada para modificar.");
            return;
        }

        System.out.println("¿Desea cambiar la cantidad de entradas? (s/n)");
        String respuesta = sc.nextLine();
        if (respuesta.equalsIgnoreCase("s")) {
            System.out.print("Nueva cantidad: ");
            int nuevaCantidad = sc.nextInt();
            sc.nextLine();
            cantidadEntradas = nuevaCantidad;
            hayCompra = false;
            hayReserva = true;
            System.out.println("Cantidad actualizada. Recuerde volver a confirmar su compra.");
        }
    }

    private static void imprimirBoleta() {
        if (!hayCompra) {
            System.out.println("Debe realizar una compra antes de imprimir la boleta.");
            return;
        }
        System.out.println("=========================================");
        System.out.println("================ BOLETA ================");
        System.out.println("Boleta N°: " + numeroBoleta++);
        // PUNTO DE DEPURACIÓN 4: edadCliente y zonaSeleccionada deberían reflejar los datos del usuario
        System.out.println("Edad del cliente: " + edadCliente);
        System.out.println("Zona: " + zonaSeleccionada);
        // PUNTO DE DEPURACIÓN 5: cantidadEntradas deberían reflejar el resultado de la compra
        System.out.println("Entradas: " + cantidadEntradas);
        // PUNTO DE DEPURACIÓN 6: totalPagar debería reflejar el resultado de la compra
        System.out.println("Tipo Cliente: " + tipoCliente);
        System.out.println("Total a pagar: $" + df.format(totalPagar));
        // PUNTO DE DEPURACIÓN 7: identificadoresEntradas debe contener todos los códigos generados
        System.out.println("Número(s) de entrada(s):");
        for (String id : identificadoresEntradas) {
            System.out.println("- " + id);
        }
        System.out.println("=========================================");
    }

    private static void cancelarReserva() {
        if (!hayReserva || hayCompra) {
            System.out.println("No hay una reserva activa para cancelar.");
            return;
        }

        identificadoresEntradas = null;
        zonaSeleccionada = "";
        cantidadEntradas = 0;
        tipoCliente = "";
        edadCliente = 0;
        totalPagar = 0;
        hayReserva = false;

        System.out.println("La reserva ha sido cancelada correctamente.");
    }

    private static int obtenerIndiceZona(String zona) {
        for (int i = 0; i < zonas.length; i++) {
            if (zonas[i].equals(zona))
                return i;
        }
        return -1;
    }

    //Esto es simulado, al no poder utilizar estructuras de control 
    //se limita a generar los ids para las últimas entradas compradas
    private static String[] generaId(String zona, int cantidad) {
        String[] ids = new String[cantidad];
        for (int i = 0; i < cantidad; i++) {
            String prefix = switch (zona) {
                case "VIP" -> "V-" + String.format("%02d", vipCounter++);
                case "Platea Baja" -> "PB-" + String.format("%02d", plateaBajaCounter++);
                case "Platea Alta" -> "PA-" + String.format("%02d", plateaAltaCounter++);
                case "Palcos" -> "P-" + String.format("%02d", palcosCounter++);
                default -> "X-00";
            };
            ids[i] = prefix;
        }
        return ids;
    }

    private static void imprimeBoletos() {
        if (!hayCompra || identificadoresEntradas == null) {
            System.out.println("No hay ninguna compra registrada para imprimir.");
            return;
        }
        System.out.println("\n--- ENTRADAS COMPRADAS ---");
        for (String id : identificadoresEntradas) {
            System.out.println("> ============== ENTRADA INDIVIDUAL ============== <");
            System.out.println("\n   Entrada:  "+ id);
            System.out.println("\n   Zona:  "+ zonaSeleccionada);
            System.out.println("\n   Tipo Cliente:  "+ tipoCliente);
            System.out.println("\n   Precio Final: $"+ df.format(totalPagar / cantidadEntradas));
            System.out.println("> =============================================== <");
        }

    }

    public static void mostrarPlanoTeatro() {
        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║                PLANO DEL TEATRO MORO               ║");
        System.out.println("╠════════════════════════════════════════════════════╣");
        System.out.println("║                    ESCENARIO                       ║");
        System.out.println("║════════════════════════════════════════════════════║");
        System.out.println("║                  [1] VIP ($30.000)                 ║");
        System.out.println("║ [2] Platea Baja ($15.000)    [4] Palcos ($13.000)  ║");
        System.out.println("║                [3] Platea Alta ($18.000)           ║");
        System.out.println("╚════════════════════════════════════════════════════╝");
    }
}
