/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends TimedRobot {
	private DifferentialDrive drive;
	private Joystick left, right;
	private NetworkTable table;
	private NetworkTableEntry tv, tx, ty, ta; 

 	@Override
 	public void robotInit() {
		drive = new DifferentialDrive(new WPI_TalonSRX(2), new WPI_TalonSRX(3));
		left = new Joystick(0);
		right = new Joystick(1);

		Limelight.setMoveConstants(.2, .1, .5, 20);
		Limelight.setAlignConstants(.05, .15, .7);
  	}

	@Override
	public void teleopPeriodic() {
		Limelight.update();

		//post to smart dashboard periodically
		SmartDashboard.putNumber("LimelightV", Limelight.getV());
		SmartDashboard.putNumber("LimelightX", Limelight.getX());
		SmartDashboard.putNumber("LimelightY", Limelight.getY());
		SmartDashboard.putNumber("LimelightArea", Limelight.getA());

		double move = 0, align = 0;
		if (left.getRawButton(1)) {
			move = Limelight.move();
		}
		if (left.getRawButton(2)) { 
			align = Limelight.align();
		}
		if (left.getRawButton(3)) {
			move = Limelight.move();
			align = Limelight.align();
		}

		if (left.getRawButton(4)) {
			Limelight.flash();
		}

		double h = 1;
		double d = h/Math.tan(Limelight.getY() * Math.PI/180);

		System.out.println(move);
		drive.arcadeDrive(move - left.getY(), align + left.getX(), false);
	}

	@Override
	public void disabledInit() {
		Limelight.disable();
	}
}
