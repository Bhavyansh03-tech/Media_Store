package com.example.mediastore

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class AudioMediaStoreUtil(
    private val context: Context
) {

    @RequiresApi(Build.VERSION_CODES.Q)
    suspend fun audio(file: File){
        withContext(Dispatchers.IO){
            // Content resolver :->
            val resolver = context.contentResolver

            val audioCollection = MediaStore.Audio.Media.getContentUri(

                // Where you want to save the audio :->
                MediaStore.VOLUME_EXTERNAL_PRIMARY  // External storage.

            )

            // Checking when the audio is saved or not :->
            val timeInMillis = System.currentTimeMillis()

            // Going to put some data to audio :->
            val audioDetails = ContentValues().apply {

                // Path were the audio will be saved.
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_MUSIC)

                // Other Details :->
                put(MediaStore.Audio.Media.DISPLAY_NAME, "${timeInMillis}_song")
                put(MediaStore.Audio.Media.MIME_TYPE, "audio/mpeg")
                put(MediaStore.Audio.Media.DATE_ADDED, timeInMillis)
                put(MediaStore.Audio.Media.IS_PENDING, 1)
            }

            // Getting uri from audio content resolver in our audio collection :->
            val audioMediaStoreUri = resolver.insert(audioCollection, audioDetails)
            audioMediaStoreUri?.let { uri ->
                try {

                    // Saving the audio :->
                    resolver.openOutputStream(uri)?.use { outputStream ->
                        // Reading the input stream and copying it to output stream :->
                        resolver.openInputStream(
                            Uri.fromFile(file)
                        ).use { inputStream ->
                            inputStream?.copyTo(outputStream)
                        }
                    }

                    // Updating the audio details :->
                    audioDetails.clear()
                    audioDetails.put(MediaStore.Audio.Media.IS_PENDING, 0)
                    resolver.update(uri, audioDetails, null, null)

                } catch (e: Exception){
                    e.printStackTrace()
                    resolver.delete(uri, null, null)
                }
            }
        }
    }

    // You can use this methods to get the audio file from resources :->
    fun getRawAudioFile(resourceId: Int): File{
        val inputStream = context.resources.openRawResource(resourceId)

        val file = File(context.cacheDir, "audio.mp3")

        file.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }

        return file
    }
}