package com.example.msu432encryptedchat


import android.util.Log
import kotlin.math.pow

object MessageTransform
{
    // Retrieve the current users private keys
    private var d = ChatActivity().setD()
    private var n = ChatActivity().setN()
    // Sets the current users keys for decryption
    fun setValues(ds: Double, ns: Double)
    {
        d = ds
        n = ns
        Log.v("Values","d $d\nn $n")
    }
    // Will return the encrypted message
    fun encryptMessage( e: Int, n: Int, message: String): String
    {
        // Convert Message to ByteArray
        println("Message $message")

        var eMsg = ""

        for(i in message.indices)
        {
            // encrypt element
            val c = ((message[i].code.toDouble().pow(e))%n)
            // encrypted.add(c)
            println("Index: ${message[i]} Char: $c")
            eMsg += c.toInt().toChar()
        }
        // for(i in message.indices){Log.v("", encrypted[i])}
        // Log.v("Print string: ", eMsg)
        return eMsg
    }

    // Takes in the users encryption key and then decrypts the message.
    // Will return the decrypted message
    fun decryptMessage(message : String): String
    {
        println(d)
        println(n)
        message.toByteArray()

        // For converted message
        var dMsg = ""

        for(i in message.indices)
        {
            val m = (message[i].code.toBigDecimal().pow(d.toInt())).remainder(n.toBigDecimal())
            println("$m")
            dMsg += m.toInt().toChar()
        }

        // [Debugging] Log.v("Decrypt", dMsg)
        return dMsg
    }
}