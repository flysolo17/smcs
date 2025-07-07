package com.jmballangca.smcsmonitoringsystem.data.security

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object CryptoUtil {

    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES/ECB/PKCS5Padding"
    private const val SECRET_KEY = "MySecretKey12345"

    fun encrypt(input: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val keySpec = SecretKeySpec(SECRET_KEY.toByteArray(), ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)
        val encryptedBytes = cipher.doFinal(input.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun decrypt(encrypted: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val keySpec = SecretKeySpec(SECRET_KEY.toByteArray(), ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, keySpec)
        val decodedBytes = Base64.decode(encrypted, Base64.DEFAULT)
        val decryptedBytes = cipher.doFinal(decodedBytes)
        return String(decryptedBytes)
    }
}