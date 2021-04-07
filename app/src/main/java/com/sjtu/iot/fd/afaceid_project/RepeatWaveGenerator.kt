package com.sjtu.iot.fd.afaceid_project

import android.media.AudioManager
import android.net.Uri
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer


abstract class RepeatWaveGenerator :WaveGenerator {
    abstract var fin:FileInputStream
    private var musicData=FloatArray(0)

    /*input parameter is a string of source uri */
    constructor(resourceIdentifier:String){
        val resourceUri= Uri.parse(resourceIdentifier)
        val resourceFile=File(resourceUri.path)
        fin=FileInputStream(resourceFile)
        val bufferSize:Int= (resourceFile.length()/ (java.lang.Byte.SIZE)).toInt()
        val byteBuffer= ByteArray(bufferSize)
        musicData=FloatArray(bufferSize/4)
        fin.read(byteBuffer)
        val buffer=ByteBuffer.wrap(byteBuffer)
        val floatBuffer=buffer.asFloatBuffer()
        floatBuffer.get(musicData)

    }


    override fun getNext(): FloatArray {
        return musicData
    }



}