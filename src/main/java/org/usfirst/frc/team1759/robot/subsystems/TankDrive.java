package org.usfirst.frc.team1759.robot.subsystems;

import models.TankDriveInterface;

import org.usfirst.frc.team1759.robot.OI;
import org.usfirst.frc.team1759.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
//import edu.wpi.first.wpilibj.

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import models.Constants;

/**
 * This subsystem is used to control the tank drive for 2018bot. Constructor
 * calls multiple CANTalons, then assigns them into SpeedControllerGroups. Can
 * be instantiated as using a gyro or not using a gyro.
 * 
 * @author Aidan Galbreath
 */

public class TankDrive<T> extends Subsystem implements TankDriveInterface {
	DifferentialDrive myRobot;
	WPI_TalonSRX leftFront;
	WPI_TalonSRX leftBack;
	WPI_TalonSRX leftMid;
	WPI_TalonSRX rightFront;
	WPI_TalonSRX rightBack;
	WPI_TalonSRX rightMid;
	SpeedControllerGroup left;
	SpeedControllerGroup right;
	long timeMovementPressed = -1;
	boolean wasMovementLastFrame = false;
	

	public TankDrive() {

		rightFront = new WPI_TalonSRX(RobotMap.RIGHT_FRONT_PORT);
		rightBack = new WPI_TalonSRX(RobotMap.RIGHT_BACK_PORT);
		// rightMid = new WPI_TalonSRX(RobotMap.RIGHT_MID_PORT);
		// leftMid = new WPI_TalonSRX(RobotMap.LEFT_MID_PORT);
		leftFront = new WPI_TalonSRX(RobotMap.LEFT_FRONT_PORT);
		leftBack = new WPI_TalonSRX(RobotMap.LEFT_BACK_PORT);
		left = new SpeedControllerGroup(leftFront, leftBack);
		right = new SpeedControllerGroup(rightFront, rightBack);
		myRobot = new DifferentialDrive(left, right);
	}
	@Override
	public void setName(String subsystem, String name) {

	}

	@Override
	public void initDefaultCommand() {
		myRobot = new DifferentialDrive(left, right);
	}

	public void tankDrive(OI oi) {
		
		

		double left = 0;
		double right = 0;

		if (oi.forward.get()) {
			left = 1;
			right = 0.98;
		} else if (oi.back.get()) {
			left = -1;
			right = -0.98;
		}
		if (oi.left.get()) {
			left *= 0.5;
		} else if (oi.right.get()) {
			right *= 0.5;
		}
		if (oi.tankLeft.get()) {
			left = -1;
			right = 0.98;
		} else if (oi.tankRight.get()) {
			left = 1;
			right = -0.98;
		}
		if (oi.sneak.get()) {
			if (oi.left.get() || oi.right.get()) {
				left *= 0.8;
				right *= 0.8;
			} else {
				left *= 0.65; 
				right *= 0.65;
			}
		} else if (oi.precision.get()) {
			if (oi.left.get() || oi.right.get()) {
				left *= 0.8;
				right *= 0.8;
			} else {
				left *= 0.6;
				right *= 0.6;
			}
		}
		
		if ((oi.forward.get() || oi.back.get()) && !wasMovementLastFrame) {
			timeMovementPressed = System.currentTimeMillis();
		}
		
		long timeSincePressed = System.currentTimeMillis() - timeMovementPressed; 
		double accelerationCurveMultiplier = Math.sqrt(timeSincePressed / 1000d);
		//commented out to remove the acceleration curve for the treadbot
		//left *= accelerationCurveMultiplier;
		//right *= accelerationCurveMultiplier;
		
		if (Math.abs(left) < Constants.EPSILON &&
		    Math.abs(right) < Constants.EPSILON &&
			oi.joysticksAttached) {
				// No NetworkTables buttons are pressed, but the joystick
				// is present.  Default to the joystick.
				myRobot.tankDrive(- oi.leftJoystick.getY(), - oi.rightJoystick.getY());
		} else {
				myRobot.tankDrive(left, right);
		}
		wasMovementLastFrame = oi.forward.get() || oi.back.get();
		
		
	}
	@Override
	public void tankDrive (double leftSpeed, double rightSpeed) {
		myRobot.tankDrive(-leftSpeed, -rightSpeed);
	}
}
