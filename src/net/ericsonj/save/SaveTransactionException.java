package net.ericsonj.save;

/**
 *
 * @author ejoseph
 */
public class SaveTransactionException extends Exception{

    public SaveTransactionException(String message) {
        super(message);
    }

    public SaveTransactionException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
