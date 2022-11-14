package com.example.msu432encryptedchat

class GenerateUserKeys
{
    // Generate p,q,n,d,e
    // Generate 2 Random Primes
    // p = prime1
    // q = prime2
    // n = p*q : Public Key
    //


    fun phi(n: Int): Int
    {
        var n = n
        var r = n
        var i = 2
        while (i * i <= n) {
            if (n % i == 0) {
                r -= r / i
                while (n % i == 0) {
                    n /= i
                }
            }
            i++
        }
        r -= r / n
        return r
    }
}