package org.team3309.frc6CIM;

import org.team3309.frc6CIM.commands.AutoForwardAndTurn;
import org.team3309.frc6CIM.subsystems.Claw;
import org.team3309.frc6CIM.subsystems.Drive;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import org.team3309.constantchanger.constantChanger;

public class Robot extends IterativeRobot {

    XboxController driverController = new XboxController(1);
    XboxController operatorController = new XboxController(2);

    private Scheduler scheduler;
    private Drive mDrive;
    private Claw mClaw;
    
    //The command that will begin running at the start of autonomous
    private Command autoCommand;
    
    private boolean constantChanger = true;
    //Runs when Robot is turned on
    public void robotInit() {
        if(constantChanger) {
            //constantChanger frame = new constantChanger();
        }
        scheduler = Scheduler.getInstance();
        //make drive
        mDrive = Drive.getInstance();
        mClaw = Claw.getInstance();
        //sets it so all information about the drive will be printed repeatidly during driving
        mDrive.setPrintingDriveInfo(false);
    }

    //When first put into disabled mode
    public void disabledInit() {

    }

    //Called repeatedly in disabled mode
    public void disabledPeriodic() {

    }

    //Init to Auto
    public void autonomousInit() {
        autoCommand = new AutoForwardAndTurn();
        autoCommand.start();
    }

    //This function is called periodically during autonomous
    public void autonomousPeriodic() {
        scheduler.run();
    }

    //Init to Tele
    public void teleopInit() {
        mDrive.resetGyro();
        //autoCommand.cancel();
    }

    //This function is called periodically during operator control
    public void teleopPeriodic() {
        scheduler.run();

        //gets all 4 axis from driver remote and depending on what drive the robot is in, the values will be used accordingly
        mDrive.drive(driverController.getLeftX(), driverController.getLeftY(), driverController.getRightX(), driverController.getRightY());

        //changes drive
        if (driverController.getLB()) {
            mDrive.setTankDrive();
        } else {
            mDrive.setHaloDrive();
        }

        //changes the solenoid on and off for driveshifter
        if (driverController.getA()) {
            mDrive.setLowGearOn();
        } else if (driverController.getB()) {
            mDrive.setHighGearOn();
            if (mDrive.isPrintingDriveInfo()) {
                System.out.println("High Gear Enabled");
            }
        }

        //checks if triggers are pressed in any way shape or form
        if (driverController.getRB()) {
            mClaw.runClawInward();
        } else if (driverController.getLB()) {
            mClaw.runClawOutward();
        } else {
            mClaw.stopClaw();
        }

        if (mDrive.isPrintingDriveInfo()) {
            System.out.println("------------------------\n");
        }
    }
}
