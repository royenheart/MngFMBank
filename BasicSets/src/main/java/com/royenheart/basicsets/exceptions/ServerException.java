package com.royenheart.basicsets.exceptions;

/**
 * @author RoyenHeart
 */
abstract public class ServerException extends BankException {

    protected Object whichThread;

    abstract Object getThread();

    public ServerException(String messages, Object whichThread) {
        super(messages);
        this.whichThread = whichThread;
    }
}
