package puentethreadsafe;

import java.util.Random;

/*
    Sistema que controla el paso de personas por un puente:
    - Siempre pasan en la misma dirección.
    - No pueden pasar más de tres personas a la vez.
    - No puede soportar más de 200 Kg de peso en ningún momento
    - El tiempo de llegada de las personas es aleatorio, entre 1 y 30 segundos.
    - El tiempo de paso de las personas es aleatorio entren 10 y 50 segundos.
    - Las personas tienen un peso aleatorio entre 40 y 120 Kg
*/

public class PuenteThreadSafe {

   
    public static void main(String[] args) {
        // Constantes 
        final int MINIMO_TIEMPO_LLEGADA = 1;
        final int MAXIMO_TIEMPO_LLEGADA = 30;
        final int MINIMO_TIEMPO_PASO = 10;
        final int MAXIMO_TIEMPO_PASO = 50;
        final int MINIMO_PESO_PERSONA = 40;
        final int MAXIMO_PESO_PERSONA = 120;
        
        //Variables
        final Puente puente = new Puente();
        String idPersona = "";
        int tiempoLlegada = 0;
        int tiempoPaso = 0;
        int pesoPersona = 0;
        
        //Bucle infinito creando personas para cruzar el puente
        int numeroPersona = 0;
        while (true) {
            //Crear una persona
            numeroPersona++;
            idPersona = "Persona " + numeroPersona; 
            tiempoLlegada = numeroAleatorio(MINIMO_TIEMPO_LLEGADA, MAXIMO_TIEMPO_LLEGADA);
            tiempoPaso = numeroAleatorio(MINIMO_TIEMPO_PASO, MAXIMO_TIEMPO_PASO);
            pesoPersona = numeroAleatorio(MINIMO_PESO_PERSONA, MAXIMO_PESO_PERSONA);
            System.out.printf("La %s llegará en %d segundos, pesa %d kilos y tardará %d segundos en cruzar. \n",
                        idPersona, tiempoLlegada, pesoPersona, tiempoPaso);
            Thread hiloPersona = new Thread(new Persona(idPersona, tiempoPaso, pesoPersona, puente));
            // Esperar a que llegue
            try {
                Thread.sleep(tiempoPaso * 100);
            } catch (InterruptedException e) {
            }
            hiloPersona.start();
        }
    }
    
    public static int numeroAleatorio(int valorMinimo, int valorMaximo) {
        Random r = new Random();
        return valorMinimo + r.nextInt(valorMaximo - valorMinimo + 1);
    }
}
