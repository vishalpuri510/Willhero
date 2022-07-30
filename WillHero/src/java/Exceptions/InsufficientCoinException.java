package Exceptions;

public class InsufficientCoinException extends Exception {
    public InsufficientCoinException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return super.getMessage() + "\nError: InsufficientCoinException{\"Not enough coins to resurrect\"}";
    }
}
