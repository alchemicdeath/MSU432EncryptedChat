package com.example.msu432encryptedchat

import kotlin.random.Random

internal object GenerateUserKeys
{
    private var list : MutableList<Int> = mutableListOf()

    fun generateKeys(): Array<Int>
    {
        val listPrim : MutableList<Int> = mutableListOf(7, 11, 13, 17,
                                                        19, 23, 29, 31, 37, 41,
                                                        43, 47, 53, 59, 61, 67,
                                                        71, 73, 79, 83, 89, 97)

        val p = listPrim[Random.nextInt(listPrim.size/2)]
        val q = listPrim[Random.nextInt(listPrim.size/2) + 11]

        // Get N
        val n = p * q
        println("n: $n")
        println("p: $p")
        println("q: $q")

        val phi = (p-1)*(q-1)

        println("phi: $phi")


        list.clear()
        // Generate e by finding a coprime
        for( i in 2 until phi/2)
        {
            if (gcd(i, phi) == 1)
                list.add(i)
        }

        // e is for public key exponent
        var e = 2
        for (i in 2..phi)
        {
            if (gcd(e, phi) == 1)
            {
                break
            }
            e++
        }

        // d is for private key exponent
        val d : Int
        var k = 0
        while(true)
        {
            if((1 + k * phi) % e == 0)
            {
                d  = (1 + (k * phi)) / e
                break
            }
            k++
        }

        if (e == d)
        {
            generateKeys()
        }
        println("d: $d")
       // Log.e("###############","E: $e N: $n D: $d P: $p Q: $q Phi:$phi")
        return arrayOf(e,n,d)
    }

    private fun gcd(a : Int, b : Int) : Int
    {
        return if (a == 0)
            b
        else
            gcd(b % a, a)
    }

   /* @JvmStatic
    fun main(args: Array<String>)
    {
        val keys: Array<Int> = generateKeys()
        val e = keys[0]
        val n = keys[1]
        val d = keys[2]
        val pri = "$d:$n"
        val pub = "$e:$n"

        println("e/n $pub")
        println("d/n $pri")

        val msgToEncrypt = "Message Test"

        val encryptedMsg = MessageTransform.encryptMessage(e, n, msgToEncrypt)
        println("Encrypted: $encryptedMsg")

        val decryptedMsg = MessageTransform.decryptMessage(d, n, encryptedMsg)
        println("Decrypted: $decryptedMsg")
    }*/
}