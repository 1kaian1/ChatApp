package com.example.chatapp;

public class MessageModel {
    private Long messageId;
    private String senderId;
    private String message;

    public MessageModel(Long messageId, String senderId, String message) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }

}
