package com.example.smartstudyai.sensors


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager



class SensorCollector(context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    private var ax = 0f
    private var ay = 0f
    private var az = 0f
    private var gx = 0f
    private var gy = 0f
    private var gz = 0f
    private var listener: ((SensorData)->Unit)? = null
    fun start(callback:(SensorData)->Unit)
    {
        listener = callback
        sensorManager.registerListener(this, accelerometer, 20_000)
        sensorManager.registerListener(this, gyroscope, 20_000)
    }

    fun stop(){ sensorManager.unregisterListener(this) }

    override fun onSensorChanged(event: SensorEvent?)
    {
        if(event == null)
            return

        when(event.sensor.type){
            Sensor.TYPE_ACCELEROMETER -> {
                ax = event.values[0]
                ay = event.values[1]
                az = event.values[2]
            }

            Sensor.TYPE_GYROSCOPE -> {
                gx = event.values[0]
                gy = event.values[1]
                gz = event.values[2]
            }
        }

        listener?.invoke(
            SensorData(
                timestamp = System.currentTimeMillis(),
                ax = ax,
                ay = ay,
                az = az,

                gx = gx,
                gy = gy,
                gz = gz
            )
        )
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {  }
}