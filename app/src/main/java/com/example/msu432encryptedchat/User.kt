package com.example.msu432encryptedchat

class User
{
    var name: String?  = null
    private var email: String? = null
    var uid: String? = null
    private var public : String? = null

    constructor()

    constructor(name: String?, email: String?, uid: String?, public: String)
    {
        this.name = name
        this.email = email
        this.uid = uid
        this.public = public
    }
}