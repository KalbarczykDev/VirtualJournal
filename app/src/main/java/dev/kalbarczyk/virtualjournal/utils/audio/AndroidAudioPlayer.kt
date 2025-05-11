package dev.kalbarczyk.virtualjournal.utils.audio

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.core.net.toUri
import java.io.File

class AndroidAudioPlayer(
    private val context: Context,
) : AudioPlayer {

    private var player: MediaPlayer? = null

    override fun playFile(file: File) {
        if (!file.exists()) {
            Log.e("AudioPlayer", "File does not exist: ${file.absolutePath}")
            return
        }

        try {
            MediaPlayer.create(context, file.toUri())?.apply {
                player = this
                start()
            } ?: Log.e("AudioPlayer", "Failed to create MediaPlayer instance")
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Error playing audio", e)
        }
    }

    override fun stop() {
        try {
            player?.stop()
            player?.release()
            player = null
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Error stopping player", e)
        }
    }
}