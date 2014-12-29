package org.team3309.frc6CIM;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

    XboxController driverController = new XboxController(1);
    XboxController operatorController = new XboxController(2);

    private Scheduler scheduler;
    Drive mDrive;
    //Runs when Robot is turned on
    public void robotInit() {
        scheduler = Scheduler.getInstance();
        //make drive
        mDrive = new Drive();
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
        mDrive.resetGyro();
    }

    //This function is called periodically during operator control
    public void teleopPeriodic() {
        scheduler.run();
        
        //gets all 4 axis from driver remote and depending on what drive the robot is in, the values will be used accordingly
        mDrive.drive(driverController.getLeftX(), driverController.getLeftY(), driverController.getRightX(), driverController.getRightY());
        
        //changes drive
        if(driverController.getLB()) {
            mDrive.setTankDrive();
        }else {
            mDrive.setHaloDrive();
        }
              
        //changes the solenoid on and off for driveshifter
        if(driverController.getRB()) {
            mDrive.setLowGearOn();
        }else {
            mDrive.setHighGearOn();
        }
    }
}
