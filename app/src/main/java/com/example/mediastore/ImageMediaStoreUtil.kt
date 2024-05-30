package com.example.mediastore

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageMediaStoreUtil(
    private val context: Context
) {

    @RequiresApi(Build.VERSION_CODES.Q)
    suspend fun saveImage(bitmap: Bitmap){
        withContext(Dispatchers.IO){
            // Content resolver :->
            val resolver = context.contentResolver

            val imageCollection = MediaStore.Images.Media.getContentUri(

                // Where you want to save the image :->
                MediaStore.VOLUME_EXTERNAL_PRIMARY  // External storage.

            )

            // Checking when the image is saved or not :->
            val timeInMillis = System.currentTimeMillis()

            // Going to put some data to image :->
            val imageDetails = ContentValues().apply {

                // Path were the image will be saved.
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)

                // Other Details :->
                put(MediaStore.Images.Media.DISPLAY_NAME, "${timeInMillis}_image" + ".jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.DATE_TAKEN, timeInMillis)
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }

            // Getting uri from image content resolver in our image collection :->
            val imageMediaStoreUri = resolver.insert(imageCollection, imageDetails)
            imageMediaStoreUri?.let { uri ->
                try {

                    // Saving the image :->
                    resolver.openOutputStream(uri)?.let { outputStream ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    }

                    // Updating the image details :->
                    imageDetails.clear()
                    imageDetails.put(MediaStore.Images.Media.IS_PENDING, 0)
                    resolver.update(uri, imageDetails, null, null)

                } catch (e: Exception){
                    e.printStackTrace()
                    resolver.delete(uri, null, null)
                }
            }
        }
    }
}