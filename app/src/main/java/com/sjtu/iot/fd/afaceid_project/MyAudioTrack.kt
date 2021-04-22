/**
 *@date 2018/11/24
 *@author dingfeng dingfengnju@gmail.com
 */
package com.sjtu.iot.fd.afaceid_project

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Handler
import android.util.Log

/*
* AudioTrack是安卓较为底层的音频播放接口，一般靠Write函数将数据写到缓冲区，实现长音频或循环播放。比MediaPlayer的好处是可以直接塞数组数据，MediaPlayer必须音频文件
* MediaPlayer一般只能播放音频文件，而且设置为loop循环的时候，两次播放之间有很短的空隙，比如10s-0.1s-10s，我们搞特征提取的时候就会有一定影响
* 但是write将一段数据写到数组中的过程，也需要block一些运算时间，最终结果可能还是会有间隙
* 可能最好还是在生成时候把音频数据生成的长一些
* 以及。。其实这个版本完全不能用
* by jinxueyi 2021.4.7
* */


class MyAudioTrack(waveGenerator: WaveGenerator,handler: Handler) : Thread() {

    private val waveGenerator: WaveGenerator = waveGenerator
    private val handler=handler
    private val audioFormat = AudioFormat.Builder().setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
        .setEncoding(AudioFormat.ENCODING_PCM_FLOAT)
        .setSampleRate(ConfigInfo.sampleRateInHz)
        .build()

    private val audioAttributes = AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build()
    private val audioBufferSize = AudioTrack.getMinBufferSize(ConfigInfo.sampleRateInHz,AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_FLOAT)
    private val audioTrack: AudioTrack = AudioTrack(
        audioAttributes,
        audioFormat,
        audioBufferSize*2,
        AudioTrack.MODE_STREAM,
        AudioManager.AUDIO_SESSION_ID_GENERATE
    )
    var playing: Boolean = false

    var started: Boolean = false

    fun isPlaying():Boolean{
        return playing
    }

    fun stopPlay() {
        playing=false
        audioTrack.stop()
    }

    fun startPlay() {
        Log.v(ConfigInfo.logTag,"------------------startPlay")
        playing=true
        start()
    }

    override fun run() {
        audioTrack.play()
        Log.v(ConfigInfo.logTag,"------------------startPlaying?")
        //Message.obtain(handler, 0, "start play").sendToTarget()
        while (playing) {
            ///val floatArray = waveGenerator.getNext()
            var offset=0
            var inputStream=waveGenerator.fileStream()
            Log.v(ConfigInfo.logTag,"---------bufferSize:"+audioBufferSize)
            val tempBuffer = ByteArray(audioBufferSize)
            while (inputStream.available()>0){
                var readCount = inputStream.read(tempBuffer)
                if (readCount == AudioTrack.ERROR_INVALID_OPERATION ||
                    readCount == AudioTrack.ERROR_BAD_VALUE) {
                    continue;
                }
                if (readCount != 0 && readCount != -1) {
                    audioTrack.write(tempBuffer, 0, readCount);
                }
            }

        }
        //Message.obtain(handler, 0, "stop play").sendToTarget()
    }
}