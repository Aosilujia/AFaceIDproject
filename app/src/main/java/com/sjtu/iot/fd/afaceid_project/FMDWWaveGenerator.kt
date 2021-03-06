/**
 *@date 2018/11/24
 *@author dingfeng dingfengnju@gmail.com
 */
package com.sjtu.iot.fd.afaceid_project

import java.io.FileInputStream
import java.io.InputStream


class FMDWWaveGenerator : WaveGenerator {
    companion object {
        const val FREQUENCY_MARGIN = 1000
        const val BASE_FREQUENCY = 17000 + 175
        const val FREQUENCY_NUM = 1
        const val T = 1 / 48000.0
        const val BUFFER_SIZE = 4096
    }

    var time: Double = 0.0
    val buffer: FloatArray = FloatArray(BUFFER_SIZE)

    constructor() {

    }

    private fun getValue(): Float {
        var result: Double = 0.0
        for (i in 0..FREQUENCY_NUM) {
            val frequency = i * FREQUENCY_MARGIN + BASE_FREQUENCY
            result += Math.cos(2 * Math.PI * frequency * time)
        }
        time += T
        return result.toFloat()
    }

    override fun getNext(): FloatArray {
        for (i in 0..BUFFER_SIZE - 1) {
            buffer[i] = getValue()
        }
        return buffer
    }

    override fun bufferSize(): Int {
        TODO("Not yet implemented")
    }

    override fun fileStream(): InputStream {
        TODO("Not yet implemented")
    }
}