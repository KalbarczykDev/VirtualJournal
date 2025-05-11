package dev.kalbarczyk.virtualjournal.utils.audio

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import java.io.File

class AndroidAudioRecorder(
    private val context: Context
) : AudioRecorder {

    private var recorder: MediaRecorder? = null

    private fun createRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context).apply {}
        } else MediaRecorder()
    }

    override fun start(outputFile: File) {
        try {
            if (recorder != null) {
                Log.w("AudioRecorder", "Recorder already exists. Releasing it.")
                recorder?.release()
                recorder = null
            }

            recorder = createRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(outputFile.absolutePath)

                prepare()
                start()
            }

            Log.d("AudioRecorder", "Recording started: ${outputFile.absolutePath}")
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Failed to start recording", e)
        }
    }

    override fun stop() {
        try {
            recorder?.stop()
            recorder?.reset()
            recorder?.release()
            recorder = null
            Log.d("AudioRecorder", "Recording stopped")
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Failed to stop recording", e)
        }
    }

}