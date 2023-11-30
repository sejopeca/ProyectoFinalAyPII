import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class laberinto {

    // Función para copiar una matriz de enteros a una matriz String
    public static String[][] copiarMatrizEnterosAString(int[][] matrizEnteros) {
        String[][] matrizStrings = new String[matrizEnteros.length][matrizEnteros[0].length];

        for (int i = 0; i < matrizEnteros.length; i++) {
            for (int j = 0; j < matrizEnteros[i].length; j++) {
                matrizStrings[i][j] = String.valueOf(matrizEnteros[i][j]);
            }
        }

        return matrizStrings;
    }

    // Función para mostrar una matriz String
    public static void mostrarMatriz(String[][] matriz) {
        for (String[] fila : matriz) {
            for (String elemento : fila) {
                System.out.print(elemento + " ");
            }
            System.out.println();
        }
    }

    // Función para marcar el camino encontrado con '©' en la matriz
    private static void marcarCaminoEncontrado(String[][] matriz, List<int[]> camino) {
        for (int[] coordenadas : camino) {
            int x = coordenadas[0];
            int y = coordenadas[1];
            matriz[x][y] = "©"; // Reemplazar el elemento del camino con '©'
        }
    }

    // Función para cambiar un elemento de una matriz
    private static void cambiarDato(String[][] matriz, int n) {
        for (int i = 0; i < n + 2; i++) {
            for (int j = 0; j < n + 2; j++) {
                if (matriz[i][j].equals("1")) {
                    matriz[i][j] = "O";
                } else {
                    if (matriz[i][j].equals("0")) {
                        matriz[i][j] = "■";
                    }
                }
            }
        }

    }

    // Función para rellenar los bordes de la matriz con ■
    public static void fillMatrix(String[][] matrizExpandida, int i, int j) {
        if (i >= matrizExpandida.length) {
            return;
        }

        if (j < matrizExpandida[0].length) {
            if (i == 0 || i == matrizExpandida.length - 1 || j == 0 || j == matrizExpandida[0].length - 1) {
                matrizExpandida[i][j] = "■";
            }
            fillMatrix(matrizExpandida, i, j + 1);
        } else {
            fillMatrix(matrizExpandida, i + 1, 0);
        }
    }

    // Función para copiar la matriz, a otra matriz de tamaño *2
    public static void copyMatrix(String[][] matrizExpandida, String[][] matrizOriginal, int i, int j, int n) {
        if (i >= n) {
            return;
        }

        if (j < n) {
            matrizExpandida[i + 1][j + 1] = matrizOriginal[i][j];
            copyMatrix(matrizExpandida, matrizOriginal, i, j + 1, n);
        } else {
            copyMatrix(matrizExpandida, matrizOriginal, i + 1, 0, n);
        }
    }

    // validar que el tamaño de los datos ingresados sea 1 y un numero
    public static String numValid(String x, int op, int n) {
        Scanner sc = new Scanner(System.in);
        boolean numeroValido = false;
        if (op == 1) {
            while (!numeroValido) {
                System.out.print("Por favor, ingresa un número entre 8 y 35: ");
                x = sc.nextLine();

                if (x.matches("[0-9]{1,2}")) {
                    int numero = Integer.parseInt(x);
                    if (numero >= 8 && numero <= 35) {
                        numeroValido = true;
                    } else {
                        System.out.println("El número no está dentro del rango de 8 y 35. Intenta de nuevo.");
                    }
                } else {
                    System.out.println("Entrada inválida. Debe ingresar un número entero.");
                }
            }
        }

        if (op == 2) {
            while (!numeroValido) {
                System.out.print("Por favor, ingresa un número entre 0 y " + (n - 1) + ": ");
                x = sc.nextLine();

                if (x.matches("[0-9]+")) {
                    int numero = Integer.parseInt(x);
                    if (numero >= 0 && numero <= n - 1) {
                        numeroValido = true;
                    } else {
                        System.out.println(
                                "El número no está dentro del rango de 0 y " + (n - 1) + ". Intenta de nuevo.");
                    }
                } else {
                    System.out.println("Entrada inválida. Debe ingresar un número entero.");
                }
            }
        }
        return x;

    }

    // Función validar ahora devuelve las coordenadas validadas
    public static int[] validar(int fila, int columna, int n) {
        Scanner sc = new Scanner(System.in);

        while (!(fila == 0 && columna >= 0 && columna < n) &&
                !(fila >= 0 && fila < n && columna == n - 1) &&
                !(fila == n - 1 && columna >= 0 && columna < n) &&
                !(fila >= 0 && fila < n && columna == 0)) {
            System.out.println("Las coordenadas deben estar en el borde de la matriz");
            System.out.print("i: ");
            fila = Integer.parseInt(numValid("m", 2, n)); // Usuario inserta la fila
            System.out.print("j: ");
            columna = Integer.parseInt(numValid("m", 2, n)); // Usuario inserta la columna
        }
        // Devolver las coordenadas validadas como un arreglo
        return new int[] { fila, columna };
    }

    public static int[][] generarLaberintoHastaL(int[][] laberinto, List<int[]> camino, int[] inicio, int[] fin,
            int n) {
        while (camino == null) {
            laberinto = generarLaberinto(n);

            // Encontrar el camino solo si hay solución
            camino = encontrarCamino(laberinto, inicio, fin);
        }
        return laberinto;
    }

    public static List<int[]> generarLaberintoHastaC(int[][] laberinto, List<int[]> camino, int[] inicio, int[] fin,
            int n) {
        while (camino == null) {
            laberinto = generarLaberinto(n);

            // Encontrar el camino solo si hay solución
            camino = encontrarCamino(laberinto, inicio, fin);
        }
        return camino;
    }

    // Generar un laberinto de manera recursiva
    private static int[][] generarLaberinto(int n) {
        int[][] laberinto = new int[n][n];
        Random random = new Random();
        generarLaberintoRecursivo(laberinto, random, 0, 0, n);
        return laberinto;
    }

    private static void generarLaberintoRecursivo(int[][] laberinto, Random random, int row, int col, int n) {
        // Verificar si se ha llegado al final del laberinto
        if (row < n) {
            if (col < n) {
                // Asignar un valor aleatorio (0 o 1) a la celda actual
                laberinto[row][col] = random.nextInt(2);
                // Llamar recursivamente para la siguiente celda en la misma fila
                generarLaberintoRecursivo(laberinto, random, row, col + 1, n);
            } else {
                // Llamar recursivamente para la siguiente fila
                generarLaberintoRecursivo(laberinto, random, row + 1, 0, n);
            }
        }
    }

    // Encontrar un camino en el laberinto utilizando búsqueda en profundidad (DFS)
    private static List<int[]> encontrarCamino(int[][] laberinto, int[] inicio, int[] fin) {
        int n = laberinto.length;
        boolean[][] visitado = new boolean[n][n];
        List<int[]> camino = new ArrayList<>();

        // Llamar a la función DFS para encontrar el camino
        if (dfs(laberinto, inicio[0], inicio[1], fin[0], fin[1], visitado, camino)) {
            return camino;
        } else {
            return null;
        }
    }

    private static boolean dfs(int[][] laberinto, int x, int y, int finX, int finY, boolean[][] visitado,
            List<int[]> camino) {
        // Verificar condiciones para continuar la búsqueda
        if (x < 0 || x >= laberinto.length || y < 0 || y >= laberinto.length || laberinto[x][y] == 0
                || visitado[x][y]) {
            return false;
        }

        // Marcar la celda como visitada y agregarla al camino
        visitado[x][y] = true;
        camino.add(new int[] { x, y });

        // Verificar si se ha llegado al punto final
        if (x == finX && y == finY) {
            return true;
        }

        // Definir las direcciones posibles (arriba, abajo, izquierda, derecha)
        int[][] direcciones = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

        // Llamar recursivamente para explorar las direcciones posibles
        return explorarDirecciones(laberinto, x, y, finX, finY, visitado, camino, direcciones, 0);
    }

    private static boolean explorarDirecciones(int[][] laberinto, int x, int y, int finX, int finY,
            boolean[][] visitado, List<int[]> camino, int[][] direcciones, int dirIndex) {
        // Verificar si hay más direcciones para explorar
        if (dirIndex < direcciones.length) {
            // Calcular las nuevas coordenadas
            int nuevoX = x + direcciones[dirIndex][0];
            int nuevoY = y + direcciones[dirIndex][1];

            // Llamar recursivamente para la nueva posición
            if (dfs(laberinto, nuevoX, nuevoY, finX, finY, visitado, camino)) {
                return true;
            }

            // Llamar recursivamente para la siguiente dirección
            return explorarDirecciones(laberinto, x, y, finX, finY, visitado, camino, direcciones, dirIndex + 1);
        }

        // Si no hay camino desde este punto, retroceder
        camino.remove(camino.size() - 1);
        return false;
    }

    // Mostrar el camino encontrado en el laberinto
    private static void mostrarCamino(List<int[]> camino, int index) {
        // Verificar si hay más pasos en el camino
        if (index < camino.size()) {
            // Mostrar la coordenada actual y llamar recursivamente para el siguiente paso
            System.out.print(Arrays.toString(camino.get(index)) + " - ");
            mostrarCamino(camino, index + 1);
        }
    }

    public static void main(String[] args) {
        // Configurar la entrada del usuario
        Scanner sc = new Scanner(System.in);
        System.out.println("Inserte la dimensión de la matriz: ");
        String m = numValid("m", 1, 0);// Usuario inserta el tamaño de la matriz
        int n = Integer.parseInt(m);
        System.out.println("Inserte el Inicio");
        System.out.print("i: ");
        String filAI = numValid("m", 2, n); // Usuario inserta la fila inicial
        int filaI = Integer.parseInt(filAI);
        System.out.print("j: ");
        String columnAI = numValid("m", 2, n); // Usuario inserta la columna inicial
        int columnaI = Integer.parseInt(columnAI);
        int[] inicioCoords = validar(filaI, columnaI, n);
        filaI = inicioCoords[0];
        columnaI = inicioCoords[1];
        System.out.println("Inserte el final");
        System.out.print("i: ");
        String filAF = numValid("m", 2, n); // Usuario inserta la fila final
        int filaF = Integer.parseInt(filAF);
        System.out.print("j: ");
        String columnAF = numValid("m", 2, n); // Usuario inserta la columna final
        int columnaF = Integer.parseInt(columnAF);
        int[] finalCoords = validar(filaF, columnaF, n);
        filaF = finalCoords[0];
        columnaF = finalCoords[1];
        sc.close();

        String[][] laberintoBig = new String[n + 2][n + 2];
        String[][] laberintoBig2 = new String[n + 2][n + 2];

        // Inicializar variables para el laberinto y el camino
        List<int[]> camino = null;
        int[][] laberinto = null;

        int[] inicio = { filaI, columnaI };
        int[] fin = { filaF, columnaF };

        // Generar un laberinto hasta que se encuentre un camino
        laberinto = generarLaberintoHastaL(laberinto, camino, inicio, fin, n);
        camino = generarLaberintoHastaC(laberinto, camino, inicio, fin, n);

        // Mostrar información del laberinto y el camino

        System.out.println("\nLaberinto:");
        System.out.println("");
        String[][] matrizStrings = copiarMatrizEnterosAString(laberinto); // Pasa la matriz a String
        copyMatrix(laberintoBig2, matrizStrings, 0, 0, n);
        marcarCaminoEncontrado(matrizStrings, camino); // Marca el camino encontrado con x
        copyMatrix(laberintoBig, matrizStrings, 0, 0, n); // Copia la matriz a una mas grande
        fillMatrix(laberintoBig, 0, 0); // rellena los bordes de la matriz mas grande con 0
        fillMatrix(laberintoBig2, 0, 0);
        cambiarDato(laberintoBig, n);
        cambiarDato(laberintoBig2, n);
        mostrarMatriz(laberintoBig2); // muestra la matriz
        System.out.println("\nLaberinto con camino marcado:");
        System.out.println("");
        mostrarMatriz(laberintoBig);
        System.out.println("");
        System.out.println("Punto Inicial: " + Arrays.toString(inicio));
        System.out.println("Punto Final: " + Arrays.toString(fin));

        System.out.println("\nCamino Encontrado:");
        mostrarCamino(camino, 0);
    }
}
