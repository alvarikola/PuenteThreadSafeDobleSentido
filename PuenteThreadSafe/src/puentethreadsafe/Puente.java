package puentethreadsafe;


public class Puente {
    // Constantes
    private static final int MAXIMO_PERSONAS = 3;
    private static final int MAXIMO_PESO = 200;
    // Variables
    private int numeroPersonas = 0;
    private int pesoPersonas = 0;
    // Contructor
    public Puente() {
    }
    // Getters
    public int getNumeroPersonas() {
        return numeroPersonas;
    }
    public int getPesoPersonas() {
        return pesoPersonas;
    }
    // Entrar
    public void entrar(Persona persona) throws InterruptedException {
        synchronized(this) {
            while ((numeroPersonas +1 > MAXIMO_PERSONAS) ||
                    (pesoPersonas + persona.getPesoPersona() > MAXIMO_PESO)) {
                System.out.printf("*** La %s debe esperar.\n", persona.getIdPersona());
                this.wait();
            }
            numeroPersonas++;
            pesoPersonas += persona.getPesoPersona();
            System.out.printf(">>> La %s entra. Estado del puente: %d personas, %d kilos.\n",
                    persona.getIdPersona(), numeroPersonas, pesoPersonas);
        }
    }
    // Salir
    public void salir(Persona persona) {
        synchronized(this){
            numeroPersonas--;
            pesoPersonas -= persona.getPesoPersona();
            this.notifyAll();
            System.out.printf(">>> La %s sale. Estado del puente: %d personas, %d kilos.\n",
                    persona.getIdPersona(), numeroPersonas, pesoPersonas);
        }
    }
}
