package puentethreadsafe;


public class Puente {
    // Constantes
    private static final int MAXIMO_PERSONAS = 4; // Número máximo de personas en el puente
    private static final int MAXIMO_PESO = 300; // Peso máximo en el puente
    private static final int MAXIMO_PERSONAS_SENTIDO = 3; // Máximo de personas por sentido (NORTE o SUR)
    
    // Variables
    private int numeroPersonas = 0; // Total de personas en el puente
    private int pesoPersonas = 0; // Peso total de las personas en el puente
    private int numeroPersonasNorte = 0; // Personas cruzando hacia el norte
    private int numeroPersonasSur = 0; // Personas cruzando hacia el sur
    private String sentido;

    // Constructor
    public Puente() {}

    // Métodos de acceso
    public int getNumeroPersonas() {
        return numeroPersonas;
    }

    public int getPesoPersonas() {
        return pesoPersonas;
    }

    // Método para permitir que una persona entre al puente
    public void entrar(Persona persona) throws InterruptedException {
        synchronized (this) {
            String sentidoPersona = persona.getSentido();
            
            // Si no hay personas cruzando, cualquiera puede pasar
            while ((numeroPersonas + 1 > MAXIMO_PERSONAS) ||
                    (pesoPersonas + persona.getPesoPersona() > MAXIMO_PESO) ||
                    // Si ya hay personas cruzando en el mismo sentido, bloqueamos
                    (sentidoPersona.equals("NORTE") && numeroPersonasNorte >= MAXIMO_PERSONAS_SENTIDO) ||
                    (sentidoPersona.equals("SUR") && numeroPersonasSur >= MAXIMO_PERSONAS_SENTIDO)) {
                System.out.printf("*** La %s debe esperar.\n", persona.getIdPersona());
                this.wait(); // El hilo espera
            }

            // Si el puente está vacío, puede entrar la primera persona y marcar el sentido
            if (numeroPersonas == 0) {
                // Establecer el sentido solo cuando el puente está vacío
                this.sentido = sentidoPersona;
            }

            // Si el puente no está lleno y el peso no excede, permitimos que entre la persona
            numeroPersonas++;
            pesoPersonas += persona.getPesoPersona();

            // Si entra hacia el norte o sur, incrementamos el contador de ese sentido
            if (sentidoPersona.equals("NORTE")) {
                numeroPersonasNorte++;
            } else {
                numeroPersonasSur++;
            }

            System.out.printf(">>> La %s entra. Estado del puente: %d personas, %d kilos.\n",
                    persona.getIdPersona(), numeroPersonas, pesoPersonas);
        }
    }

    // Método para permitir que una persona salga del puente
    public void salir(Persona persona) {
        synchronized (this) {
            String sentidoPersona = persona.getSentido();
            
            // Actualizamos el número de personas y el peso
            numeroPersonas--;
            pesoPersonas -= persona.getPesoPersona();

            if (sentidoPersona.equals("NORTE")) {
                numeroPersonasNorte--;
            } else {
                numeroPersonasSur--;
            }

            // Si ya no hay personas en el puente, se borra el sentido
            if (numeroPersonas == 0) {
                sentido = null;
            }

            // Notificamos a todos los hilos esperando para que puedan intentar entrar
            this.notifyAll();

            System.out.printf(">>> La %s sale. Estado del puente: %d personas, %d kilos.\n",
                    persona.getIdPersona(), numeroPersonas, pesoPersonas);
        }
    }
}
