package com.example.msu432encryptedchat

import android.annotation.SuppressLint
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class CryptoMethod
{
    private var secretKeySpec: SecretKeySpec? = null

    @SuppressLint("GetInstance")
    private fun encrypt(message :String): String
    {
        val encryptedMessage: String?
        val cipher = Cipher.getInstance("AES")

        // Encodes the contents of this string using the specified character set and returns the
        // resulting byte array.
        val messageByte = message.toByteArray()

        // Creates a new array of the specified size, with all elements initialized to zero.

        // Initialize cipher to encryption mode with secretKeySpec.
        cipher!!.init(Cipher.ENCRYPT_MODE, secretKeySpec)
        // The data is encrypted, depending on how this cipher was initialized.
        val encryptedByte: ByteArray? = cipher.doFinal(messageByte)

        // Sets the returnString to the string of encryptedByte as ISO-8859-1
        encryptedMessage = (encryptedByte!!.toString(Charsets.ISO_8859_1))

        return encryptedMessage
    }

    @SuppressLint("GetInstance")
    private fun aesDecryptionMethod(encryptedMessage: String): String
    {
        val decipher = Cipher.getInstance("AES")

        // Encodes the contents of this string using the specified character set
        // and returns the resulting byte array from ISO-8859-1.
        val encryptedByte = encryptedMessage.toByteArray(charset("ISO-8859-1"))

        // Sets var to string input
        val decryptedMessage: String?

        // An array of bytes

        // Initialize cipher to decryption mode with secretKeySpec.
        decipher!!.init(Cipher.DECRYPT_MODE, secretKeySpec)

        // Decrypts data in a single-part operation, or finishes a multiple-part operation
        val decryption: ByteArray = decipher.doFinal(encryptedByte)

        // Converts the data from the specified array of bytes to characters as a string.
        decryptedMessage = String(decryption)

        // Returns the string
        return decryptedMessage
    }

    fun getKey(): PrivateKey
    {
        val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        return keyStore.getKey("MyKeyAlias", null) as PrivateKey
    }

    fun genKeys(): String {
        val keyPairGenerator : KeyPairGenerator =
            KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore")

        keyPairGenerator.initialize(KeyGenParameterSpec.Builder(
                    "key1", KeyProperties.PURPOSE_DECRYPT)
            .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP).build())

        val keyPair: KeyPair = keyPairGenerator.generateKeyPair()
        val cipher: Cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding")

        cipher.init(Cipher.DECRYPT_MODE, keyPair.private)

        // The key pair can also be obtained from the Android Keystore any time as follows:
        val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        val privateKey: PrivateKey = keyStore.getKey("key1", null) as PrivateKey
        val publicKey: PublicKey = keyStore.getCertificate("key1").getPublicKey()

        return publicKey.toString()
    }
}