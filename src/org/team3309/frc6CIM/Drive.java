
package org.team3309.frc6CIM;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;

public class Drive {

    //all of the sensors and motor controllers

    private Victor left1;
    private Victor left2;
    private Victor left3;
    private Victor right1;
    private Victor right2;
    private Victor right3;
    private Encoder leftEncoder;
    private Encoder rightEncoder;
    private ModifiedGyro gyro;
    private Solenoid driveShifter;
    
    //all the booleans for which drive is being used
    private boolean haloDriveEnabled = true;
    private boolean tankDriveEnabled = false;

    private static Drive instance;
    public Drive getInstance() {
        if(instance == null) {
            instance = new Drive();
        }
        return instance;
    }
    //the constructor
    private Drive() {
        //initialize Victors
        left1 = new Victor(RobotMap.DRIVE_LEFT_1);
        left2 = new Victor(RobotMap.DRIVE_LEFT_2);
        left3 = new Victor(RobotMap.DRIVE_LEFT_3);
        right1 = new Victor(RobotMap.DRIVE_RIGHT_1);
        right2 = new Victor(RobotMap.DRIVE_RIGHT_2);
        right3 = new Victor(RobotMap.DRIVE_RIGHT_3);
        
        driveShifter = new Solenoid(RobotMap.DRIVE_DRIVESHIFTER);

        //initialize Encoders
        leftEncoder = new Encoder(RobotMap.DRIVE_ENCODER_LEFT_A, RobotMap.DRIVE_ENCODER_LEFT_B, true, CounterBase.EncodingType.k1X);
        rightEncoder = new Encoder(RobotMap.DRIVE_ENCODER_RIGHT_A, RobotMap.DRIVE_ENCODER_RIGHT_B, false, CounterBase.EncodingType.k1X);
        leftEncoder.start();
        rightEncoder.start();
        //initialize gyro
       gyro = new ModifiedGyro(RobotMap.DRIVE_GYRO);

    }

    //change this to change threshold
    private final double THRESHOLD = .1;
    //tells if gyro is a yay or nay
    private boolean gyroEnabled = false;
    //the max angular velocity (duh)
    private final int MAX_ANGULAR_VELOCITY = 720;
    
    //speed barrier for encoder, when encoder.getRate() exceeds this value, high gear automatically happens
    private final double SPEED_BARRIER = 40;
    
    //max voltage for drive
    private final double MAX_DRIVE_VOLTAGE = 20;
    
    //Now for all the possible kp constants
    private final double KP_NORMAL = .02;
    
    //joystick is driven like a halo warthog, left joystick goes forward and backward, right joystick goes left and right
    private void driveHalo(double throttle, double turn) {
        
        double modifiedTurn;
        double gyroKP = KP_NORMAL;
        if (gyroEnabled) {
            if (Math.abs(throttle) < THRESHOLD && Math.abs(turn) < THRESHOLD) {
                //if ther joystick is not pressed enough, immeaditely stop, don't even do the math
                return;
            }
            

            double currentAngularRateOfChange = gyro.getAngularRateOfChange();
            double desiredAngularRateOfChange = turn * MAX_ANGULAR_VELOCITY;
            modifiedTurn = (currentAngularRateOfChange - desiredAngularRateOfChange) * gyroKP;
        }else {
            modifiedTurn = turn;
        }
        
        double t_left = throttle + modifiedTurn;
        double t_right = throttle - modifiedTurn;

        double left = t_left + skim(t_right);
        double right = t_right + skim(t_left);

        //negative because Vinnie did it
        setLeft(-left);
        setRight(right);
    }
    
    //This method takes when the equation returns above the max or below the min and fixes it so it does not.  Uses some math and gets the job done
    private double skimGain = .25;
    double skim(double v) {
        // gain determines how much to skim off the top
        if (v > 1.0) {
            return -((v - 1.0) * skimGain);
        } else if (v < -1.0) {
            return -((v + 1.0) * skimGain);
        }
        return 0;
    }

    //Tank drive works as so:
    //left joystick controls left side,
    //right joystick controls right side
    //simple and easy
    private void driveTank(double leftY, double rightY) {
        setLeft(leftY);
        setRight(rightY);
    }

    public void drive(double leftX, double leftY, double rightX, double rightY) {
        //stop drive if drive motors voltage exceeds a value
        /*
        if(voltageSensor.getReading() > MAX_DRIVE_VOLTAGE) {
            setLowGearOn();
            return;
        }
        */
        
        //Find if rate excededs certain value, if so, set it to high gear
        if(Math.abs(rightEncoder.getRate()) > SPEED_BARRIER && Math.abs(leftEncoder.getRate()) > SPEED_BARRIER) {
            setHighGearOn();
        }
        
        if(haloDriveEnabled) {
            driveHalo(leftY, rightX);
        }else if(tankDriveEnabled) {
            driveTank(leftY, rightY);
        }else {
            //if not drive is enabled, enable halo as default drive
            haloDriveEnabled = true;
            tankDriveEnabled = false;
        }
    }

    private void setLeft(double val) {
        left1.set(val);
        left2.set(val);
        left3.set(val);
    }

    private void setRight(double val) {
        right1.set(val);
        right2.set(val);
        right3.set(val);
    }
    
    public void setTankDrive() {
        tankDriveEnabled = true;
        haloDriveEnabled = false;
    }
    
    public void setHaloDrive() {
        tankDriveEnabled = false;
        haloDriveEnabled = true;
    }
    
    public void resetGyro() {
        gyro.reset();
    }
    
    //turns the solenoid on and off
    private void setDriveShifter(boolean b) {
        driveShifter.set(b);
    }
    public void setLowGearOn() {
        setDriveShifter(false);
    }
    public void setHighGearOn() {
        setDriveShifter(true);
    }
    
    //enable this is you ever want to go just forward
    private boolean straightPidEnabled = false;

    /*public int getCurrentDriveVoltage() {
        double totalVoltage = left1. + left2.getVoltage() + left3.getVoltage() + right1.getVoltage() + right2.getVoltage() + right3.getVoltage();
        return totalVoltage;
    }*/
    
    //this class takes care of PIDSource and PIDOutput, IN ONE CLASS , and this straightPID fun is for autonomous only, so it will be unused until autonomous
    //ignore until later
    private class StraightPID implements PIDSource, PIDOutput {

        public double pidGet() {
            return (leftEncoder.get() + rightEncoder.get()) / 2;
        }

        public void pidWrite(double d) {
            if (straightPidEnabled) {
                driveHalo(d, 0);
            }
        }
    }
}