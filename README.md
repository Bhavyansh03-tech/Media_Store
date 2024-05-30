# Media Store Feature

This repository contains a feature that allows users to save images, videos, or audio files directly to the Media Store on an Android device using external storage. The feature is implemented in Kotlin with Jetpack Compose. This functionality is particularly useful for applications that need to store media files efficiently and access them through the device's media store.



## Features

- Save images, videos, and audio files to external storage.
- Utilize Kotlin and Jetpack Compose for modern Android development practices.
- Easy integration into existing Android projects.
## Getting Started

### Prerequisites
- Android Studio installed on your computer.
- An Android device or emulator to run the app.
- An Android device or emulator running API level 29 or higher.
### Installation

To use the `ContextualFlowRow` in your project, follow these steps:

1.> Clone the repository:

```bash
  git clone https://github.com/yourusername/MediaStoreFeature.git

```

2.> Open the project in Android Studio:
- Open Android Studio.
- Select File > Open... and navigate to the cloned repository.

3.> Sync the project:
- Ensure that Gradle syncs successfully by clicking Sync Now if prompted.
## Usage

To save an image to the media store, you can use the following function in your Jetpack Compose setup:

```bash
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

```
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are greatly appreciated.

1.> Fork the Project.\
2.> Create your Feature Branch `git checkout -b feature/AmazingFeature`.\
3.> Commit your Changes `git commit -m 'Add some AmazingFeature'`.\
4.> Push to the Branch `git push origin feature/AmazingFeature`.\
5.> Open a Pull Request

## Acknowledgements

- Inspiration from various Android development tutorials and documentation.
## Contact

For questions or feedback, please contact [@Bhavyansh03-tech](https://github.com/Bhavyansh03-tech).
