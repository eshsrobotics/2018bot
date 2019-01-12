/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1759.robot;

import org.usfirst.frc.team1759.robot.subsystems.TankDrive;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;

//import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

//import edu.wpi.first.wpilibj.ADXRS450_Gyro;
//import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;

//import org.usfirst.frc.team1759.robot.subsystems.LowerArm;
//import org.usfirst.frc.team1759.robot.subsystems.RaiseArm;
//import org.usfirst.frc.team1759.robot.commands.FollowPath;
//import org.usfirst.frc.team1759.robot.commands.ExpelCommand;
//import org.usfirst.frc.team1759.robot.commands.GoEncoder;
//import org.usfirst.frc.team1759.robot.subsystems.Arm;
//import org.usfirst.frc.team1759.robot.subsystems.Intake;
import org.usfirst.frc.team1759.robot.subsystems.TankDrive;
import org.usfirst.frc.team1759.robot.subsystems.Cameras;

//import org.usfirst.frc.team1759.robot.MatchData;


//import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Command;
//import edu.wpi.first.wpilibj.interfaces.Gyro;
//import edu.wpi.first.wpilibj.ADXRS450_Gyro;
//import edu.wpi.first.wpilibj.CounterBase.EncodingType;
//import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
//import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
//import models.Graph;
//import models.Vector2;
//import models.Point;
//import models.Constants;
//import models.TestableCommandInterface;
//import wrappers.EncoderWrapper;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends IterativeRobot {
	private TankDrive tank;
	private OI oi;
	private DoubleSolenoid grip;
	
	/**
	 * Used to set the threshold for the throttle. If the throttle is greater than positive threshold, it is up. If it is less 
	 * than negative threshold, it is down. If it is between positive and negative threshold, it remains in it's current state.
	 */
	private static final double THROTTLE_THRESHOLD = 1 / 3.0;	 


	@Override
	public void robotInit() {
		try {
			System.out.println("hello !!!!!!!!!!!!!!!!!");
			grip = new DoubleSolenoid(2,3);
			// Streams usb camera video feed straight to Dashboard.
			// CameraServer.getInstance().startAutomaticCapture();
			//public static DoubleSolenoid arm = new DoubleSolenoid(0,1);
			// Initialize drive.
			oi = new OI();
			tank = new TankDrive();
			CameraServer.getInstance().startAutomaticCapture();
			Cameras.cameraRun();
		}
		catch(Exception e) {
			System.out.println("-----------");
			System.out.println(e.getMessage());
		}	
	}

	public void disabledInit() {

	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	public void autonomousInit() {
		Scheduler.getInstance().run();
	}

	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	public void teleopInit() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopPeriodic() {
		try {
		Scheduler.getInstance().run();
		tank.tankDrive(oi);
		
		if(oi.rightJoystick.getThrottle() > THROTTLE_THRESHOLD || oi.armIn.get()) {
				grip.set(DoubleSolenoid.Value.kForward);
				//Arm.raise();
		} else if(oi.rightJoystick.getThrottle() < -1.0 * THROTTLE_THRESHOLD || oi.armOut.get()) {
			grip.set(DoubleSolenoid.Value.kReverse);
			//Arm.lower();
		} else {
			/**
			 * If neither condition is true, the arm will remain in it's current state. If we utilize the kOff setting for the solenoid, it will move as
			 * gravity, inertia, etc. dictates. We want the arm to remain in a current position, so it does nothing otherwise.
			 */
		}
		}
		catch(Exception e) {
			System.out.println("-----------");
			System.out.println(e.getMessage());
		}
	}
}