package com.example.msu432encryptedchat

import android.util.Log
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.pow

object MessageTransform
{
    // This function returns the gcd or greatest common divisor
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

    /*
        * Encrypts the message
        * Takes in two primes to utilize for key creation
        * Will be modified to be the keys latter one
        * Will return the encrypted message
    */
    fun encryptMessage( p: Int, q: Int, message: String): String
    {
        val encrypted : MutableList<Char> = mutableListOf()     // List for the encrypted letters
        val n = p*q                                             // product of p and q
        val e = 3                                               // exponent for encryption
        message.toByteArray(charset("UTF-8"))        // Convert Message to ByteArray
       // Log.v("Print: ", message)
        var eMsg = ""
        for(element in message)
        {
            val c = element.code.toDouble().pow(e) % n               // encrypt the current index
            // Log.v("", c)
            val encryptedC = BigDecimal.valueOf(c).toBigInteger()   // convert c to BigInteger
            // Log.v("", C)
            encrypted.add(encryptedC.toInt().toChar())              // Add encrypted index to list
            // Log.v("", C.toInt().toChar())
            eMsg += encryptedC.toInt().toChar()
        }
        // for(i in message.indices){Log.v("", encrypted[i])}
        // Log.v("Print string: ", eMsg)
        return eMsg
    }

    fun decryptMessage(list: String, d: Double, n: Double): String
    {
        var dMsg = ""                                               // For converted message

        // Loop the string message and convert each one
        // into the correct value of the original message
        for(i in list.indices)
        {
            val c = list[i].code.toDouble()                         // Convert index char to double
            // Log.v("", c)
            val bigintN = BigInteger.valueOf(n.toLong())            // Convert n to BigInt for use
            // Log.v("", bigintNN)
            val encryptedC = BigDecimal.valueOf(c).toBigInteger()   // Convert c to BigInt for use
            // Log.v("", encryptedC)
            val m = (encryptedC.pow(d.toInt()))%bigintN             // Decrypt the original value
            // Log.v("", m)
            dMsg += m.toInt().toChar()                              // Add current index to string
            // Log.v("", dMsg)
        }
        // [Debugging] Log.v("Decrypt", dMsg)
        return dMsg
    }
}