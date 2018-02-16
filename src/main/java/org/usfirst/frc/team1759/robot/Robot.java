/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1759.robot;

import org.usfirst.frc.team1759.commands.ExpelCommand;
import org.usfirst.frc.team1759.commands.IntakeCommand;
import org.usfirst.frc.team1759.robot.subsystems.Arm;
import org.usfirst.frc.team1759.robot.subsystems.Climber;
import org.usfirst.frc.team1759.robot.subsystems.Intake;
import org.usfirst.frc.team1759.robot.subsystems.Launcher;
import org.usfirst.frc.team1759.robot.subsystems.TankDrive;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends IterativeRobot {
	private TankDrive tank;
	private Launcher launcher;
	private Intake upperIntake;
	private Intake lowerIntake;
	private Climber climber;
	private Arm arm;
	private IntakeCommand partialIntakeCommand;
	private IntakeCommand fullIntakeCommand;
	private ExpelCommand expelCommand;
	private OI oi;
	
	@Override
	public void robotInit() {
		//Streams usb camera video feed straight to Dashboard.
		CameraServer.getInstance().startAutomaticCapture();
		//Initialize drive.
		oi = new OI();
		tank = new TankDrive();
		upperIntake = new Intake(new WPI_TalonSRX(RobotMap.UPPER_LEFT_INTAKE), new WPI_TalonSRX(RobotMap.UPPER_RIGHT_INTAKE));
		lowerIntake = new Intake(new WPI_TalonSRX(RobotMap.LOWER_LEFT_INTAKE), new WPI_TalonSRX(RobotMap.LOWER_RIGHT_INTAKE));
		arm = new Arm(new DoubleSolenoid(RobotMap.ARM_PORT_IN, RobotMap.ARM_PORT_OUT));
		launcher = new Launcher();
		partialIntakeCommand = new IntakeCommand(lowerIntake, arm);
		fullIntakeCommand = new IntakeCommand(upperIntake, lowerIntake, arm);
		expelCommand = new ExpelCommand(lowerIntake, arm);
		climber = new Climber();
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
		tank.tankDrive(oi);
		launcher.launch(oi);
		climber.climb(oi);
	}
}