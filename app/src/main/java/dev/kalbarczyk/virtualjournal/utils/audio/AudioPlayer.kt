package dev.kalbarczyk.virtualjournal.utils.audio

import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun stop()
}