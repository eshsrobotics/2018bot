/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1759.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
//import edu.wpi.first.wpilibj.Talon;
import com.ctre.phoenix.motorcontrol.can.*;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends IterativeRobot {
	private DifferentialDrive m_myRobot;
	private Joystick m_leftStick;
	private Joystick m_rightStick;
	private WPI_TalonSRX rightFront;
	private WPI_TalonSRX rightBack;
	private WPI_TalonSRX leftFront;
	private WPI_TalonSRX leftBack;
	private SpeedControllerGroup left;
	private SpeedControllerGroup right;
	private DoubleSolenoid launcher;
	
	@Override
	public void robotInit() {
		//Streams usb camera video feed straight to Dashboard.
		CameraServer.getInstance().startAutomaticCapture();
		
		//Initialize drive.
		rightFront = new WPI_TalonSRX(0);
		rightBack = new WPI_TalonSRX(1);
		leftFront = new WPI_TalonSRX(5);
		leftBack = new WPI_TalonSRX(6);
		left = new SpeedControllerGroup(leftFront, leftBack);
		right = new SpeedControllerGroup(rightFront, rightBack);
		m_myRobot = new DifferentialDrive(left, right);
		m_leftStick = new Joystick(0);
		m_rightStick = new Joystick(1);
		launcher = new DoubleSolenoid(0, 1);
	}

	@Override
	public void teleopPeriodic() {
		m_myRobot.tankDrive(m_leftStick.getY(), m_rightStick.getY());
		// Potentially damaging to robot if the solenoid is not properly controlled (dry firing is bad)
		if(m_rightStick.getTrigger() == true) {
			launcher.set(DoubleSolenoid.Value.kForward);
		} else if (m_leftStick.getTrigger() == true) {
			launcher.set(DoubleSolenoid.Value.kReverse);
		} else {
			launcher.set(DoubleSolenoid.Value.kOff);
		}
	}
}
