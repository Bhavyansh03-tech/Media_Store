package com.example.mediastore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mediastore.ui.theme.MediaStoreTheme
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // If you want to save audio file from raw resource :->
        val audioMediaStoreUtil = AudioMediaStoreUtil(this)
        // val resId = audioMediaStoreUtil.getRawAudioFile(R.raw.audio_file)
        // runBlocking {
            // audioMediaStoreUtil.saveAudioFile(resId)
        // }

        setContent {
            MediaStoreTheme {

            }
        }
    }
}