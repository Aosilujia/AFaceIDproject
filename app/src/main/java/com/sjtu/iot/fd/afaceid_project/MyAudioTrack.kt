/**
 *@date 2018/11/24
 *@author dingfeng dingfengnju@gmail.com
 */
package com.sjtu.iot.fd.afaceid_project

import android.app.Notification
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Handler
import android.os.Message
import android.util.Log

/*
* AudioTrack是安卓较为底层的音频播放接口，一般靠Write函数将数据写到缓冲区，实现长音频或循环播放。比MediaPlayer的好处是可以直接塞数组数据，MediaPlayer必须音频文件
* MediaPlayer一般只能播放音频文件，而且设置为loop循环的时候，两次播放之间有很短的空隙，比如10s-0.1s-10s，我们搞特征提取的时候就会有一定影响
* 但是write将一段数据写到数组中的过程，也需要block一些运算时间，最终结果可能还是会有间隙
* 可能最好还是在生成时候把音频数据生成的长一些
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
    private val audioTrack: AudioTrack = AudioTrack(
        audioAttributes,
        audioFormat,
        ConfigInfo.sampleRateInHz * 4 * 2,
        AudioTrack.MODE_STREAM,
        AudioManager.AUDIO_SESSION_ID_GENERATE
    )
    private var playing: Boolean = false

    fun stopPlay() {
        playing=false
        audioTrack.stop()
    }

    fun startPlay() {
        start()
    }

    override fun run() {
        playing=true
        audioTrack.play()
        Message.obtain(handler, 0, "start play").sendToTarget()
        while (playing) {
            val floatArray = waveGenerator.getNext()
            var result=audioTrack.write(floatArray, 0, floatArray.size, AudioTrack.WRITE_NON_BLOCKING)
            Log.v(ConfigInfo.logTag,"result = "+result.toString())
            if(result<0)
            {
                break
            }
        }
        Message.obtain(handler, 0, "stop play").sendToTarget()
    }
}