package Exceptions;

public class AlreadyResurrectedException extends Exception {
    public AlreadyResurrectedException(String message) {
            super(message);
    }
    @Override
    public String toString() {
        return super.getMessage() + "\nError: AlreadyResurrectedException{\"Player is resurrected already\"}";
    }

}
