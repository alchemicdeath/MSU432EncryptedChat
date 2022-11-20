package com.example.msu432encryptedchat

class Message
{
    var message: String? = null
    var senderId: String? = null

    constructor()

    constructor(message1: String?, senderId: String?)
    {
        this.message = message
        this.senderId = senderId
    }
}