package Exceptions;

public class SaveNotFoundException extends NullPointerException {
    public SaveNotFoundException(String message) {
        super(message);
    }
    @Override
    public String toString() {
        return super.getMessage() + "\nError: SaveNotFoundException{\"Saved game may not be there\"}";
    }
}
