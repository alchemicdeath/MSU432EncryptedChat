package com.example.msu432encryptedchat

import java.security.PublicKey

class User
{
    var name: String?  = null
    var email: String? = null
    var uid: String? = null
    var public : String? = null

    constructor()

    constructor(name: String?, email: String?, uid: String?, public: String)
    {
        this.name = name
        this.email = email
        this.uid = uid
        this.public = public
    }
}