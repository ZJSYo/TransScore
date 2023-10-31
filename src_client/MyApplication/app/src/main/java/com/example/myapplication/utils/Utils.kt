package com.example.myapplication.utils


import android.content.ContentResolver
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object Utils {
    @OptIn(ExperimentalEncodingApi::class)
    fun encode2Base64(filePath: String): String {
        var encodedString: String = ""
        try {
            val file = File(filePath)
            val inputStream = FileInputStream(file)
            val byteArray = ByteArray(file.length().toInt())
            inputStream.read(byteArray)
            inputStream.close()
            encodedString = Base64.encode(byteArray)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return encodedString
    }
    @OptIn(ExperimentalEncodingApi::class)
    fun convertAudioToBase64(contentResolver: ContentResolver, audioUri: Uri): String {
        var inputStream: InputStream? = null
        val byteArrayOutputStream = ByteArrayOutputStream()
        val buffer = ByteArray(4096)
        var bytesRead: Int

        try {
            inputStream = contentResolver.openInputStream(audioUri)
            while ((inputStream!!.read(buffer).also { bytesRead = it }) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        val audioBytes = byteArrayOutputStream.toByteArray()
        val base64Encoded = Base64.encode(audioBytes)
        return base64Encoded
    }
}