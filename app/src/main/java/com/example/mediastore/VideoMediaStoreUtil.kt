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

class VideoMediaStoreUtil(
    private val context: Context
) {

    @RequiresApi(Build.VERSION_CODES.Q)
    suspend fun saveVideo(file: File){
        withContext(Dispatchers.IO){
            // Content resolver :->
            val resolver = context.contentResolver

            val videoCollection = MediaStore.Video.Media.getContentUri(

                // Where you want to save the video :->
                MediaStore.VOLUME_EXTERNAL_PRIMARY  // External storage.

            )

            // Checking when the video is saved or not :->
            val timeInMillis = System.currentTimeMillis()

            // Going to put some data to video :->
            val videoDetails = ContentValues().apply {

                // Path were the video will be saved.
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_MOVIES)

                // Other Details :->
                put(MediaStore.Video.Media.DISPLAY_NAME, "${timeInMillis}_video")
                put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
                put(MediaStore.Video.Media.DATE_ADDED, timeInMillis)
                put(MediaStore.Video.Media.IS_PENDING, 1)
            }

            // Getting uri from video content resolver in our video collection :->
            val videoMediaStoreUri = resolver.insert(videoCollection, videoDetails)
            videoMediaStoreUri?.let { uri ->
                try {

                    // Saving the video :->
                    resolver.openOutputStream(uri)?.use { outputStream ->
                        // Reading the input stream and copying it to output stream :->
                        resolver.openInputStream(
                            Uri.fromFile(file)
                        ).use { inputStream ->
                            inputStream?.copyTo(outputStream)
                        }
                    }

                    // Updating the video details :->
                    videoDetails.clear()
                    videoDetails.put(MediaStore.Video.Media.IS_PENDING, 0)
                    resolver.update(uri, videoDetails, null, null)

                } catch (e: Exception){
                    e.printStackTrace()
                    resolver.delete(uri, null, null)
                }
            }
        }
    }
}