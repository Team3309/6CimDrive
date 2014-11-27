package org.team3309.Arrow;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends IterativeRobot {
	
	private Scheduler scheduler;

  //Runs when Robot is turned on
	public void robotInit() {
		scheduler = Scheduler.getInstance();
	}

  //When first put into disabled mode
	public void disabledInit() {
	  
	}
	
	//Called repeatedly in disabled mode
	public void disabledPeriodic() {
	
	}
	
  //Init to Auto
	public void autonomousInit() {
	
	}

	
	//This function is called periodically during autonomous
	public void autonomousPeriodic() {
		scheduler.run();
	}

  //Init to Tele
	public void teleopInit() {
		
	}

	
	//This function is called periodically during operator control
	public void teleopPeriodic() {
		scheduler.run();
		
	}
}
