package puentethreadsafe;


public class Puente {
    // Constantes
    private static final int MAXIMO_PERSONAS = 4;
    private static final int MAXIMO_PESO = 300;
    private static final int MAXIMO_PERSONAS_SENTIDO = 3;
    // Variables
    private int numeroPersonas = 0;
    private int pesoPersonas = 0;
    private String sentido = null;
    private int numeroPersonasNorte = 0;
    private int numeroPersonasSur = 0;
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
            String sentidoPersona = persona.getSentido();
            while ((numeroPersonas +1 > MAXIMO_PERSONAS) ||
                    (pesoPersonas + persona.getPesoPersona() > MAXIMO_PESO) ||
                    (sentido != null && !sentido.equals(sentidoPersona)) ||
                    (sentidoPersona.equals("NORTE") && numeroPersonasNorte >= MAXIMO_PERSONAS_SENTIDO) ||
                    (sentidoPersona.equals("SUR") && numeroPersonasSur >= MAXIMO_PERSONAS_SENTIDO)) {
                System.out.printf("*** La %s debe esperar.\n", persona.getIdPersona());
                this.wait();
            }
            numeroPersonas++;
            pesoPersonas += persona.getPesoPersona();
            sentido = sentidoPersona;
            if (sentidoPersona.equals("NORTE")) {
                numeroPersonasNorte++;
            } else {
                numeroPersonasSur++;
            }
            System.out.printf(">>> La %s entra. Estado del puente: %d personas, %d kilos.\n",
                    persona.getIdPersona(), numeroPersonas, pesoPersonas);
        }
    }
    // Salir
    public void salir(Persona persona) {
        synchronized(this){
            String sentidoPersona = persona.getSentido();
            numeroPersonas--;
            pesoPersonas -= persona.getPesoPersona();
            
            if (sentidoPersona.equals("NORTE")) {
                numeroPersonasNorte--;
            } else {
                numeroPersonasSur--;
            }

            if (numeroPersonas == 0) {
                sentido = null;
            }
            
            this.notifyAll();
            System.out.printf(">>> La %s sale. Estado del puente: %d personas, %d kilos.\n",
                    persona.getIdPersona(), numeroPersonas, pesoPersonas);
        }
    }
}
