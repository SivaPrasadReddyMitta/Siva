package uk.ac.tees.mad.univid.Utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.VibrationEffect
import android.os.Vibrator
import kotlin.math.sqrt

class ShakeDetector(private val onShake: () -> Unit, private val context: Context) : SensorEventListener {

    private var shakeTimestamp: Long = 0
    private var shakeCount = 0

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val gX = x / SensorManager.GRAVITY_EARTH
            val gY = y / SensorManager.GRAVITY_EARTH
            val gZ = z / SensorManager.GRAVITY_EARTH

            val gForce = sqrt((gX * gX + gY * gY + gZ * gZ).toDouble()).toFloat()

            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                val now = System.currentTimeMillis()
                if (shakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return
                }

                if (shakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                    shakeCount = 0
                }

                shakeTimestamp = now
                shakeCount++

                if (shakeCount >= SHAKE_COUNT_THRESHOLD) {
                    onShake()
                    vibrateDevice()
                    shakeCount = 0
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    private fun vibrateDevice() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    companion object {
        private const val SHAKE_THRESHOLD_GRAVITY = 2.7F
        private const val SHAKE_SLOP_TIME_MS = 500
        private const val SHAKE_COUNT_RESET_TIME_MS = 3000
        private const val SHAKE_COUNT_THRESHOLD = 3
    }
}