package org.usfirst.frc.team1759.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.interfaces.Gyro;

/**
 *
 * @author Spencer Moore
 *
 *         This class is used to keep track of all sensors on the 2018bot. 
 *         This will keep track of the gyro,
 *         accelerometer, ultrasonic, and other sensors present. This class is
 *         used similarly to the RobotMap or OI classes, as a placeholder of
 *         sorts to help with sorting.
 *
 **/

public class Sensors {
	
	public static Gyro gyro = new ADXRS450_Gyro();

	public static Accelerometer accel = new BuiltInAccelerometer();
}