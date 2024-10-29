package puentethreadsafe;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Persona implements Runnable{
    // Atributos.
    private final String idPersona;
    private final int tiempoPaso;
    private final int pesoPersona;
    private final Puente puente;
    
    //Contructor

    public Persona(String idPersona, int tiempoPaso, int pesoPersona, Puente puente) {
        this.idPersona = idPersona;
        this.tiempoPaso = tiempoPaso;
        this.pesoPersona = pesoPersona;
        this.puente = puente;
    }
    
    //Getters y setters
    public String getIdPersona() {
        return idPersona;
    }
    public int getTiempoPaso() {
        return tiempoPaso;
    }
    public int getPesoPersona() {
        return pesoPersona;
    }
    
    // Método run().
    @Override
    public void run() {
        System.out.printf(">>> La %s con %d kilos quiere cruzar en %d segundos.\n" + 
                          "    Estado del Puente: %d personas, %d kilos.\n",
            idPersona, pesoPersona, tiempoPaso, puente.getNumeroPersonas(), puente.getPesoPersonas());
        // Entrar
        try {
            puente.entrar(this);
        } catch (InterruptedException e) {
        }
        //Cruzar
        try {
            Thread.sleep(tiempoPaso * 100);
        } catch (InterruptedException e) {
            
        }
        
        //Salir
        puente.salir(this);
    }
    
}
