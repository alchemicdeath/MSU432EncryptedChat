package com.example.msu432encryptedchat

class User
{
    var name: String?  = null
    var email: String? = null
    var uid: String? = null

    constructor(name: String?, email: String?, uid: String?)
    {
        this.name = name
        this.email = email
        this.uid = uid
    }
}