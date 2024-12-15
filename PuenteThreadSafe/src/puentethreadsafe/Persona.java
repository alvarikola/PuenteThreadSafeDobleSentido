package puentethreadsafe;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Persona implements Runnable {
    private final String idPersona;
    private final int tiempoPaso;
    private final int pesoPersona;
    private final String sentido;
    private final Puente puente;

    public Persona(String idPersona, int tiempoPaso, int pesoPersona, String sentido, Puente puente) {
        this.idPersona = idPersona;
        this.tiempoPaso = tiempoPaso;
        this.pesoPersona = pesoPersona;
        this.sentido = sentido;
        this.puente = puente;
    }

    public String getIdPersona() {
        return idPersona;
    }

    public int getTiempoPaso() {
        return tiempoPaso;
    }

    public int getPesoPersona() {
        return pesoPersona;
    }

    public String getSentido() {
        return sentido;
    }

    @Override
    public void run() {
        System.out.printf(">>> La %s con %d kilos quiere cruzar en %d segundos y en sentido %s.\n" +
                          "    Estado del Puente: %d personas, %d kilos.\n",
                          idPersona, pesoPersona, tiempoPaso, sentido, puente.getNumeroPersonas(), puente.getPesoPersonas());
        
        // Intentar entrar
        try {
            puente.entrar(this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Cruza el puente
        try {
            Thread.sleep(tiempoPaso * 100); // Simula el tiempo de cruce
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Salir
        puente.salir(this);
    }
}

