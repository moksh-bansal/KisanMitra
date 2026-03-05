package com.appdev.kisanmitra.ml

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder

class PlantDiseaseClassifier(context: Context) {

    private var interpreter: Interpreter

    init {

        val model = context.assets.open("crop_disease_model.tflite")
        val modelBytes = model.readBytes()
        val buffer = ByteBuffer.allocateDirect(modelBytes.size)
        buffer.order(ByteOrder.nativeOrder())
        buffer.put(modelBytes)

        interpreter = Interpreter(buffer)
    }

    fun predict(bitmap: Bitmap): FloatArray {

        val resized = Bitmap.createScaledBitmap(bitmap,128,128,true)

        val input = ByteBuffer.allocateDirect(4*128*128*3)
        input.order(ByteOrder.nativeOrder())

        for(y in 0 until 128){
            for(x in 0 until 128){

                val pixel = resized.getPixel(x,y)

                input.putFloat(((pixel shr 16) and 0xFF).toFloat())
                input.putFloat(((pixel shr 8) and 0xFF).toFloat())
                input.putFloat((pixel and 0xFF).toFloat())
            }
        }

        val output = Array(1){FloatArray(38)}

        interpreter.run(input,output)

        return output[0]
    }
}