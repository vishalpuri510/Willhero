package Exceptions;

public class DeadBossOrcException extends ClassCastException{
    public DeadBossOrcException (String message) {
        super(message);
    }

    @Override
    public String toString() {
        return super.getMessage() + "\nError:  DeadBossOrcException{\"Boss ki nikal gyi sauce\"}";
    }
}
