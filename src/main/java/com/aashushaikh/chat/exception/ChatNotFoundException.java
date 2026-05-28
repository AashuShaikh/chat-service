package com.aashushaikh.chat.exception;

public class ChatNotFoundException extends RuntimeException {
    public ChatNotFoundException(String id) {
        super("Chat not found: " + id);
    }
}
