package com.example.msu432encryptedchat

import android.annotation.SuppressLint
import android.util.Log
import java.math.BigDecimal
import kotlin.math.IEEErem
import kotlin.math.pow

object MessageTransform
{
    // This function returns the gcd or greatest common divisor
    @SuppressLint("SuspiciousIndentation")
    fun gcd(a: Double, h: Double): Double
    {
        var a = a
        var h = h
        var temp: Double

        while (true)
        {
            temp = a % h

            if (temp == 0.0)
                return h
                println("true")

            a = h
            h = temp
        }
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

    var d = ChatActivity().setD()
    var n = ChatActivity().setN()
    fun setValues(ds: Double, ns: Double)
    {
        d = ds
        n = ns
        Log.v("Values","d $d\nn $n")
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
            var m = (message[i].toInt().toBigDecimal().pow(d.toInt())).remainder(n.toBigDecimal())
            println("$m")
            dMsg += m.toInt().toChar()
        }

        // [Debugging] Log.v("Decrypt", dMsg)
        return dMsg
    }

}