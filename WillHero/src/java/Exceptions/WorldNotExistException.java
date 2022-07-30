package Exceptions;

public class WorldNotExistException extends IndexOutOfBoundsException {
    public WorldNotExistException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return super.getMessage() + "\nError: WorldNotExistException{\"World may not be there\"}";
    }

}

