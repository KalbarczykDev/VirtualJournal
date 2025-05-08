package dev.kalbarczyk.virtualjournal.utils.audio

import java.io.File

interface AudioRecorder {
    fun start(outputFile: File)
    fun stop()
}