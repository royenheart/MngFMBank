package com.royenheart.basicsets.exceptions;

/**
 * 自定义异常类，异常树根
 * @author RoyenHeart
 */
abstract public class BankException extends Exception {

    protected String messages;

    abstract void getMessages();

    public BankException(String messages) {
        this.messages = messages;
    }

}
