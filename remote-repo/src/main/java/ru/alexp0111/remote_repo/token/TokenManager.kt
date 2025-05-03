package ru.alexp0111.remote_repo.token

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

private const val ALIAS = "token_encryption_key"
private const val TRANSFORMATION =
    "${KeyProperties.KEY_ALGORITHM_AES}/${KeyProperties.BLOCK_MODE_GCM}/${KeyProperties.ENCRYPTION_PADDING_NONE}"

class TokenManager @Inject constructor(
    private val context: Context,
) {

    /**
     * Получение экземпляра KeyStore.
     */
    private fun getKeyStore(): KeyStore {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        return keyStore
    }

    /**
     * Создает секретный ключ в KeyStore, если его ещё нет.
     */
    private fun ensureSecretKeyExists() {
        val keyStore = getKeyStore()
        if (!keyStore.containsAlias(ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            val spec = KeyGenParameterSpec.Builder(
                ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).apply {
                setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                setRandomizedEncryptionRequired(true)
            }.build()
            keyGenerator.init(spec)
            keyGenerator.generateKey()
        }
    }

    /**
     * Шифрует входящий токен с использованием KeyStore.
     *
     * @param plainText токен в открытом виде.
     * @return байтовый массив зашифрованного токена.
     */
    private fun encryptToken(plainText: String): ByteArray {
        ensureSecretKeyExists()
        val keyStore = getKeyStore()
        val secretKeyEntry = keyStore.getEntry(ALIAS, null) as KeyStore.SecretKeyEntry
        val secretKey = secretKeyEntry.secretKey

        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv = cipher.iv
        val encryptedContent = cipher.doFinal(plainText.encodeToByteArray())

        val outputStream = ByteArrayOutputStream()
        outputStream.write(iv)
        outputStream.write(encryptedContent)
        return outputStream.toByteArray()
    }

    /**
     * Дешифрует токен, полученный из хранилища.
     *
     * @param encryptedToken токен в зашифрованном виде.
     * @return открытый токен в виде строки.
     */
    private fun decryptToken(encryptedToken: ByteArray): String {
        ensureSecretKeyExists()
        val keyStore = getKeyStore()
        val secretKeyEntry = keyStore.getEntry(ALIAS, null) as KeyStore.SecretKeyEntry
        val secretKey = secretKeyEntry.secretKey

        val inputStream = ByteArrayInputStream(encryptedToken)
        val iv = ByteArray(12)
        inputStream.read(iv)
        val content = ByteArray(inputStream.available())
        inputStream.read(content)

        val cipher = Cipher.getInstance(TRANSFORMATION)
        val gcmParamSpec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParamSpec)
        val decryptedContent = cipher.doFinal(content)
        return decryptedContent.decodeToString()
    }
    fun saveToken(tokenType: String, token: String) {
        val encryptedToken = encryptToken(token)
        val sharedPrefs = context.getSharedPreferences("token_storage", Context.MODE_PRIVATE)
        sharedPrefs.edit().putString(tokenType, Base64.encodeToString(encryptedToken, Base64.DEFAULT))
            .apply()
    }

    fun getToken(tokenType: String): String? {
        val sharedPrefs = context.getSharedPreferences("token_storage", Context.MODE_PRIVATE)
        val encodedToken = sharedPrefs.getString(tokenType, null) ?: return null
        val decodedToken = Base64.decode(encodedToken, Base64.DEFAULT)
        return decryptToken(decodedToken)
    }

    fun deleteToken(tokenType: String) {
        val sharedPrefs = context.getSharedPreferences("token_storage", Context.MODE_PRIVATE)
        sharedPrefs.edit().remove(tokenType).apply()
    }

    companion object TokenType {
        const val ACCESS = "access"
        const val REFRESH = "refresh"
    }
}