package me.ANONIMUS.mcprotocol.exception;

public class BetterException extends RuntimeException {
    public BetterException(final String message) {
        super(message);
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }
}