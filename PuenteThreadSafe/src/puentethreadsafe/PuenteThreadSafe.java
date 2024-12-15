package puentethreadsafe;

import java.util.Random;

/*
    Sistema que controla el paso de personas por un puente:
    Las personas pueden pasar en los dos sentidos.
    No puede haber más de cuatro personas a la vez.
    No puede haber más de tres personas en cada sentido.
    No puede haber más de 300 kg de peso en ningún momento.
    El tiempo entre la llegada de dos personas es aleatorio entre 1 y 30 segundos.
    El tiempo en atravesar el puente es aleatorio, entre 10 y 50 segundos.
    Las personas tienen un peso aleatorio entre 40 y 120 kg.
*/

public class PuenteThreadSafe {

    public static void main(String[] args) {
        final int MINIMO_TIEMPO_LLEGADA = 1;
        final int MAXIMO_TIEMPO_LLEGADA = 30;
        final int MINIMO_TIEMPO_PASO = 10;
        final int MAXIMO_TIEMPO_PASO = 50;
        final int MINIMO_PESO_PERSONA = 40;
        final int MAXIMO_PESO_PERSONA = 120;
        
        // Instancia del puente
        final Puente puente = new Puente();
        int numeroPersona = 0;
        
        // Bucle infinito creando personas para cruzar el puente
        while (true) {
            // Crear una persona
            numeroPersona++;
            String idPersona = "Persona " + numeroPersona;
            int tiempoLlegada = numeroAleatorio(MINIMO_TIEMPO_LLEGADA, MAXIMO_TIEMPO_LLEGADA);
            int tiempoPaso = numeroAleatorio(MINIMO_TIEMPO_PASO, MAXIMO_TIEMPO_PASO);
            int pesoPersona = numeroAleatorio(MINIMO_PESO_PERSONA, MAXIMO_PESO_PERSONA);
            String sentido = numeroAleatorio(0, 1) == 0 ? "NORTE" : "SUR";
            System.out.printf("La %s llegará en %d segundos, en sentido %s, pesa %d kilos y tardará %d segundos en cruzar. \n",
                        idPersona, tiempoLlegada, sentido, pesoPersona, tiempoPaso);
            
            // Crear el hilo de la persona
            Thread hiloPersona = new Thread(new Persona(idPersona, tiempoPaso, pesoPersona, sentido, puente));
            
            // Esperar antes de que llegue la siguiente persona
            try {
                Thread.sleep(tiempoLlegada * 100); // Espera el tiempo entre llegadas
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Iniciar el hilo de la persona
            hiloPersona.start();
        }
    }

    public static int numeroAleatorio(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}
