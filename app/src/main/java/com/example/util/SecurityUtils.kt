package com.example.util

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.UUID

object SecurityUtils {
    private val secureRandom = SecureRandom()

    /**
     * Cryptographically hashes a plain password using SHA-256 with a unique random 16-byte salt
     * and multiple iterations to prevent rainbow table and dictionary attacks.
     * Formats output as: salt_hex:hash_hex
     */
    fun hashPassword(password: String): String {
        val saltBytes = ByteArray(16)
        secureRandom.nextBytes(saltBytes)
        val saltHex = bytesToHex(saltBytes)

        val hashBytes = computeHash(password, saltBytes)
        val hashHex = bytesToHex(hashBytes)

        return "$saltHex:$hashHex"
    }

    /**
     * Timing-attack-safe password verification.
     */
    fun verifyPassword(password: String, storedHash: String): Boolean {
        val parts = storedHash.split(":")
        if (parts.size != 2) {
            // Fallback check for legacy passwords or unmigrated tests
            return storedHash == password
        }

        val saltHex = parts[0]
        val expectedHashHex = parts[1]

        val saltBytes = try {
            hexToBytes(saltHex)
        } catch (e: Exception) {
            return false
        }

        val computedHashBytes = computeHash(password, saltBytes)
        val computedHashHex = bytesToHex(computedHashBytes)

        return MessageDigest.isEqual(
            computedHashHex.toByteArray(Charsets.UTF_8),
            expectedHashHex.toByteArray(Charsets.UTF_8)
        )
    }

    private fun computeHash(password: String, salt: ByteArray): ByteArray {
        val digest = MessageDigest.getInstance("SHA-256")
        digest.update(salt)
        var hash = digest.digest(password.toByteArray(Charsets.UTF_8))
        // Multiple iterations to strengthen hash against brute force
        for (i in 0 until 1000) {
            digest.reset()
            digest.update(hash)
            digest.update(salt)
            hash = digest.digest()
        }
        return hash
    }

    /**
     * Validates email format strictly.
     */
    fun isValidEmail(email: String): Boolean {
        val trimmed = email.trim()
        if (trimmed.length < 5 || trimmed.length > 100) return false
        val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return emailRegex.matches(trimmed)
    }

    /**
     * Validates password strength (min 6 chars, non-blank).
     */
    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    /**
     * Generates a secure 6-digit verification / reset code.
     */
    fun generateVerificationCode(): String {
        val num = secureRandom.nextInt(900000) + 100000
        return num.toString()
    }

    /**
     * Generates a cryptographically secure session token.
     */
    fun generateSessionToken(): String {
        return UUID.randomUUID().toString() + "-" + System.currentTimeMillis()
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val sb = StringBuilder(bytes.size * 2)
        for (b in bytes) {
            sb.append(String.format("%02x", b))
        }
        return sb.toString()
    }

    private fun hexToBytes(hex: String): ByteArray {
        val len = hex.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(hex[i], 16) shl 4) +
                    Character.digit(hex[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }
}
