package de.fun2code.android.pawserver;

/**
 * Created by Roman on 22.06.2017.
 */

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.List;

public class SensorLis implements SensorEventListener {
    private SensorManager sensorManager;
    private List<Sensor> sensors;
    private Sensor sensorAccel;
    private Sensor sensorOrient;
    private Sensor sensorLight;
    private Sensor sensorProximity;
    private Sensor sensorPressure;
    private Sensor sensorGyroscope;
    private Sensor sensorDeviceTemperature;
    private Sensor sensorAmbientTemperature;
    private Sensor sensorRelativeHumidity;
    private Sensor sensorStepCounter;
    private int orientAccuracy;
    private float brightness;
    private float proximity;
    private float pressure;
    private float ambientTemperature;
    private float relativeHumidity;
    private float stepCounter;
    private long lastOrientTimestamp;
    private float orientBearing;
    private float last_x;
    private float last_y;
    private float last_z;
    private long lastUpdate = -1L;
    private long currentTime = -1L;
    private float dataX;
    private float dataY;
    private float dataZ;
    private float dataForce;
    private float gyroX;
    private float gyroY;
    private float gyroZ;
    private final int DATA_X = 0;
    private final int DATA_Y = 1;
    private final int DATA_Z = 2;
    private static final int TYPE_RELATIVE_HUMIDITY = 12;
    private static final int TYPE_AMBIENT_TEMPERATURE = 13;
    private static final int TYPE_STEP_COUNTER = 19;

    public SensorLis(Context context) {
        this.sensorManager = (SensorManager)context.getSystemService("sensor");
        this.sensors = this.sensorManager.getSensorList(1);
        if(this.sensors.size() > 0) {
            this.sensorAccel = (Sensor)this.sensors.get(0);
        }

        this.sensors = this.sensorManager.getSensorList(3);
        if(this.sensors.size() > 0) {
            this.sensorOrient = (Sensor)this.sensors.get(0);
        }

        this.sensors = this.sensorManager.getSensorList(5);
        if(this.sensors.size() > 0) {
            this.sensorLight = (Sensor)this.sensors.get(0);
        }

        this.sensors = this.sensorManager.getSensorList(8);
        if(this.sensors.size() > 0) {
            this.sensorProximity = (Sensor)this.sensors.get(0);
        }

        this.sensors = this.sensorManager.getSensorList(6);
        if(this.sensors.size() > 0) {
            this.sensorPressure = (Sensor)this.sensors.get(0);
        }

        this.sensors = this.sensorManager.getSensorList(4);
        if(this.sensors.size() > 0) {
            this.sensorGyroscope = (Sensor)this.sensors.get(0);
        }

        this.sensors = this.sensorManager.getSensorList(7);
        if(this.sensors.size() > 0) {
            this.sensorDeviceTemperature = (Sensor)this.sensors.get(0);
        }

        this.sensors = this.sensorManager.getSensorList(12);
        if(this.sensors.size() > 0) {
            this.sensorRelativeHumidity = (Sensor)this.sensors.get(0);
        }

        this.sensors = this.sensorManager.getSensorList(13);
        if(this.sensors.size() > 0) {
            this.sensorAmbientTemperature = (Sensor)this.sensors.get(0);
        }

        this.sensors = this.sensorManager.getSensorList(19);
        if(this.sensors.size() > 0) {
            this.sensorStepCounter = (Sensor)this.sensors.get(0);
        }

    }

    public void startSensing() {
        if(this.sensorAccel != null) {
            this.sensorManager.registerListener(this, this.sensorAccel, 1);
        }

        if(this.sensorOrient != null) {
            this.sensorManager.registerListener(this, this.sensorOrient, 1);
        }

        if(this.sensorLight != null) {
            this.sensorManager.registerListener(this, this.sensorLight, 1);
        }

        if(this.sensorProximity != null) {
            this.sensorManager.registerListener(this, this.sensorProximity, 1);
        }

        if(this.sensorPressure != null) {
            this.sensorManager.registerListener(this, this.sensorPressure, 1);
        }

        if(this.sensorGyroscope != null) {
            this.sensorManager.registerListener(this, this.sensorGyroscope, 1);
        }

        if(this.sensorDeviceTemperature != null) {
            this.sensorManager.registerListener(this, this.sensorDeviceTemperature, 7);
        }

        if(this.sensorRelativeHumidity != null) {
            this.sensorManager.registerListener(this, this.sensorRelativeHumidity, 12);
        }

        if(this.sensorAmbientTemperature != null) {
            this.sensorManager.registerListener(this, this.sensorAmbientTemperature, 13);
        }

        if(this.sensorStepCounter != null) {
            this.sensorManager.registerListener(this, this.sensorStepCounter, 19);
        }

    }

    public void stopSensing() {
        this.sensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == 1 && event.values.length == 3) {
            this.currentTime = System.currentTimeMillis();
            long diffTime = this.currentTime - this.lastUpdate;
            this.lastUpdate = this.currentTime;
            this.dataX = event.values[0];
            this.dataY = event.values[1];
            this.dataZ = event.values[2];
            this.dataForce = Math.abs(this.dataX + this.dataY + this.dataZ - this.last_x - this.last_y - this.last_z) / (float)diffTime * 10000.0F;
            this.last_x = this.dataX;
            this.last_y = this.dataY;
            this.last_z = this.dataZ;
        } else if(event.sensor.getType() == 3) {
            this.orientAccuracy = event.accuracy;
            this.lastOrientTimestamp = System.currentTimeMillis();
            this.orientBearing = event.values[0];
        } else if(event.sensor.getType() == 5) {
            this.brightness = event.values[0];
        } else if(event.sensor.getType() == 8) {
            this.proximity = event.values[0];
        } else if(event.sensor.getType() == 6) {
            this.pressure = event.values[0];
        } else if(event.sensor.getType() == 4) {
            this.gyroX = event.values[0];
            this.gyroY = event.values[1];
            this.gyroZ = event.values[2];
        } else if(event.sensor.getType() == 12) {
            this.relativeHumidity = event.values[0];
        } else if(event.sensor.getType() == 13) {
            this.ambientTemperature = event.values[0];
        } else {
            if(event.sensor.getType() != 19) {
                return;
            }

            this.stepCounter = event.values[0];
        }

    }

    public float getX() {
        return this.dataX;
    }

    public float getY() {
        return this.dataY;
    }

    public float getZ() {
        return this.dataZ;
    }

    public float getForce() {
        return this.dataForce;
    }

    public int getOrientAccuracy() {
        return this.orientAccuracy;
    }

    public long getLastOrientTimestamp() {
        return this.lastOrientTimestamp;
    }

    public float getOrientBearing() {
        return this.orientBearing;
    }

    public float getBrightness() {
        return this.brightness;
    }

    public float getProximity() {
        return this.proximity;
    }

    public float getPressure() {
        return this.pressure;
    }

    public float getGyroX() {
        return this.gyroX;
    }

    public float getGyroY() {
        return this.gyroY;
    }

    public float getGyroZ() {
        return this.gyroZ;
    }

    public float getRelativeHumidity() {
        return this.relativeHumidity;
    }

    public float getAmbientTemperature() {
        return this.ambientTemperature;
    }

    public float getStepCounter() {
        return this.stepCounter;
    }
}

