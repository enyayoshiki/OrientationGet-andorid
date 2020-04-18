package com.example.orientationget

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity(),SensorEventListener{
    private val matrixSize = 16
    //センサーの値
    private var mgValue = FloatArray(3)
    private var acValue = FloatArray(3)

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("Not yet implemented")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val inR = FloatArray(matrixSize)
        val outR = FloatArray(matrixSize)
        val I = FloatArray(matrixSize)
        val orValue = FloatArray(3)

        if(event == null) return
        when (event.sensor.type){
            Sensor.TYPE_ACCELEROMETER -> acValue = event.values.clone()
            Sensor.TYPE_MAGNETIC_FIELD -> mgValue = event.values.clone()
        }

        SensorManager.getRotationMatrix(inR, I , acValue , mgValue)
        //携帯を水平にもち、アクティビティはポートレイト
        SensorManager.remapCoordinateSystem(inR , SensorManager.AXIS_X,SensorManager.AXIS_Y,outR)
        SensorManager.remapCoordinateSystem(inR, SensorManager.AXIS_X, SensorManager.AXIS_Z, outR)
        SensorManager.getOrientation(outR,orValue)

        val strBuild = StringBuilder()
        strBuild.append("方角角（アジマス）")
        strBuild.append(rad2Deg(orValue[2]))
        strBuild.append("¥n")
        strBuild.append("傾斜角(ピッチ):")
        strBuild.append(rad2Deg(orValue[1]))
        strBuild.append("¥n")
        strBuild.append("回転角（ロール）:")
        strBuild.append(rad2Deg(orValue[2]))
        strBuild.append("¥n")
        txt01.text = strBuild.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    private fun rad2Deg(rad : Float): Int {
        return Math.floor(Math.toDegrees(rad.toDouble())).toInt()
    }

    public override fun onResume() {
        super.onResume()
        val sensorManager = getSystemService(Context.SENSOR_SERVICE)as SensorManager
        sensorManager.unregisterListener(this)
    }

    override fun onPause() {
        super.onPause()
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    sensorManager.unregisterListener(this)
    }
}
