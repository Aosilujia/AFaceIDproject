package com.sjtu.iot.fd.afaceid_project

import java.io.InputStream

interface WaveGenerator {
    fun getNext(): FloatArray
    fun bufferSize(): Int
    fun fileStream(): InputStream
}