/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1759.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends IterativeRobot {
	private Encoder encoder;
	
	@Override
	public void robotInit() {
		//Streams usb camera video feed straight to Dashboard.
		CameraServer.getInstance().startAutomaticCapture();

		encoder = new Encoder(0, 1);
	}
	
	public void disabledInit() {
		
	}
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}
	
	public void autonomousInit() {
		
	}
	
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}
	
	public void teleopInit() {
		
	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		
		System.out.printf("%d,%f,%f,%d\n", encoder.get(), encoder.getDistance(), encoder.getRate(), encoder.getRaw());
	}
}