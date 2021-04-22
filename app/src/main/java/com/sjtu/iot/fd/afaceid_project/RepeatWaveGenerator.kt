package com.sjtu.iot.fd.afaceid_project

import android.media.AudioManager
import android.net.Uri
import android.renderscript.ScriptGroup
import android.util.Log
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.lang.Exception
import java.nio.ByteBuffer


class RepeatWaveGenerator :WaveGenerator {
    private var musicData=FloatArray(0)
    private var bufferSize: Int=0
    var fileStream:InputStream?=null

    /*input parameter is a string of source uri */
    constructor(fileInputStream: InputStream){
        fileStream=fileInputStream
        /*try {
            bufferSize= fileInputStream.available()
            val byteBuffer = ByteArray(bufferSize)
            musicData = FloatArray(bufferSize / 4)
            fileInputStream.read(byteBuffer)
            val buffer = ByteBuffer.wrap(byteBuffer)
            val floatBuffer = buffer.asFloatBuffer()
            floatBuffer.get(musicData)
            Log.v(ConfigInfo.logTag,"music Data initialize:"+musicData.size)
        }
        catch (e:Exception){
            print(e)
        }*/
    }


    override fun getNext(): FloatArray {
        return musicData
    }

    override fun bufferSize(): Int {
        return bufferSize
    }

    override fun fileStream(): InputStream {
        return fileStream!!
    }


}