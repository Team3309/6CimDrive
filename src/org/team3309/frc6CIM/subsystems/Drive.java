package org.team3309.frc6CIM.subsystems;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.team3309.frc6CIM.ModifiedGyro;
import org.team3309.frc6CIM.RobotMap;

public class Drive extends Subsystem {
//sorry Joey, I committed with your account

    private boolean isPrintingDriveInfo = false;

    //all of the sensors and motor controllers
    private Victor[] leftVictors = new Victor[3];
    private Victor[] rightVictors = new Victor[3];
    private Victor strafeVictor;

    private Encoder leftEncoder;
    private Encoder rightEncoder;
    private ModifiedGyro gyro;
    private Solenoid driveShifterRight;
    private Solenoid driveShifterLeft;

    //all the constants for which drive is being used
    private final int MODE_HALO_DRIVE = 0;
    private final int MODE_TANK_DRIVE = 1;
    private int driveMode = 0;

    //enable this is you ever want to go just forward
    private boolean straightPidEnabled = false;

    //This method takes when the equation returns above the max or below the min and fixes it so it does not.  Uses some math and gets the job done
    private double skimGain = .25;

    //change this to change threshold
    private final double THRESHOLD = .3;
    //tells if gyro is a yay or nay
    private boolean gyroEnabled = true;
    //the max angular velocity (duh)
    private int MAX_ANGULAR_VELOCITY = 800;

    //speed barrier for encoder, when encoder.getRate() exceeds this value, high gear automatically happens
    private final double SPEED_BARRIER = 40;

    //max voltage for drive
    private double MAX_DRIVE_VOLTAGE = 20;

    //Now for all the possible kp constants
    private double KP_NORMAL = .002;

    private static Drive instance;

    private PIDController straightPID = null;

    public static Drive getInstance() {
        if (instance == null) {
            instance = new Drive();

        }
        return instance;
    }

    //the constructor
    private Drive() {
        //initialize Victors in their arrays
        leftVictors[0] = new Victor(RobotMap.DRIVE_LEFT_1);
        leftVictors[1] = new Victor(RobotMap.DRIVE_LEFT_2);
        leftVictors[2] = new Victor(RobotMap.DRIVE_LEFT_3);
        rightVictors[0] = new Victor(RobotMap.DRIVE_RIGHT_1);
        rightVictors[1] = new Victor(RobotMap.DRIVE_RIGHT_2);
        rightVictors[2] = new Victor(RobotMap.DRIVE_RIGHT_3);

        strafeVictor = new Victor(RobotMap.DRIVE_STRAFE);

        driveShifterRight = new Solenoid(RobotMap.DRIVE_DRIVESHIFTER_RIGHT);
        driveShifterLeft = new Solenoid(RobotMap.DRIVE_DRIVESHIFTER_LEFT);

        //initialize Encoders
        leftEncoder = new Encoder(RobotMap.DRIVE_ENCODER_LEFT_A, RobotMap.DRIVE_ENCODER_LEFT_B, true, CounterBase.EncodingType.k1X);
        rightEncoder = new Encoder(RobotMap.DRIVE_ENCODER_RIGHT_A, RobotMap.DRIVE_ENCODER_RIGHT_B, false, CounterBase.EncodingType.k1X);
        leftEncoder.start();
        rightEncoder.start();
        //initialize gyro
        gyro = new ModifiedGyro(RobotMap.DRIVE_GYRO);

        StraightPID straight = new StraightPID();
        straightPID = new PIDController(.001, 0, .02, straight, straight);
        straightPID.disable();
    }

    double pidRequestedValue;
    boolean aimAngleIsSet = false;
    //joystick is driven like a halo warthog, left joystick goes forward and backward, right joystick goes left and right
    private void driveHalo(double throttle, double turn, double strafe) {

        double modifiedTurn;
        double gyroKP = KP_NORMAL;

        if (Math.abs(throttle) < THRESHOLD) {
            throttle = 0;
        }
        if (Math.abs(strafe) < THRESHOLD) {
            strafe = 0;
        }
        if (Math.abs(turn) < THRESHOLD) {
            turn = 0;
        }

        //if (Math.abs(throttle) < THRESHOLD && Math.abs(turn) < THRESHOLD) {
        //        //if ther joystick is not pressed enough, immeaditely stop, don't even do the math
        //        return;
        //    }
        //KRAGER FIX GYRO, VALUES WENT TO 2, SHOULD NEVER HIT 2
        if (gyroEnabled) {

            double currentAngularRateOfChange = gyro.getAngularRateOfChange();
            double desiredAngularRateOfChange = turn * MAX_ANGULAR_VELOCITY;
            //Change back if it doesnt work
            modifiedTurn = (currentAngularRateOfChange - desiredAngularRateOfChange) * gyroKP;
            if (isPrintingDriveInfo) {
                System.out.println("turn: " + turn + " throttle: " + throttle);
                System.out.println("Current: " + currentAngularRateOfChange + " Desired: " + desiredAngularRateOfChange);
                System.out.println("Error: " + (currentAngularRateOfChange - desiredAngularRateOfChange) + "modified value: " + modifiedTurn);
            }
        } else {
            modifiedTurn = turn;
            if (isPrintingDriveInfo) {
                System.out.println("gyro not enabled");
                System.out.println("Angle: " + gyro.getAngle() + " Angular Velocity: " + gyro.getAngularRateOfChange());
            }
        }
        double t_left = throttle + modifiedTurn;
        double t_right = throttle - modifiedTurn;

        if (isPrintingDriveInfo) {
            System.out.println(t_left + " t_Left");
            System.out.println(t_right + " t_Right");
        }

        double left = t_left + skim(t_right);
        double right = t_right + skim(t_left);

        if (isPrintingDriveInfo) {
            System.out.println(left + " Left");
            System.out.println(right + " Right");
        }

        double pidLastError;
        double pid_Kp = 0.05;
        double pid_Ki = 0.0;
        double pid_Kd = 0.0;
        double pidSensorCurrentValue = gyro.getAngle();

        double pidError;

        double pidIntegral;
        double pidDerivative;
        double pidDrive;
        boolean pidRunning;
        pidLastError = 0;
        if (strafe != 0) {
            if(!aimAngleIsSet) {
                pidRequestedValue = gyro.getAngle();
                System.out.println("pidRequested = " + pidRequestedValue);
                System.out.println(" ");
                aimAngleIsSet = true;
            }
            pidIntegral = 0;

            // Read the sensor value
            pidSensorCurrentValue = gyro.getAngle();

            // calculate error
            pidError = pidSensorCurrentValue - pidRequestedValue;
            System.out.println(pidSensorCurrentValue + " - "  + pidRequestedValue + " = Error: " + pidError);

            // calculate the derivative
            pidDerivative = pidError - pidLastError;
            pidLastError = pidError;
            //System.out.println("Last Error: " + pidLastError);
                   

            // calculate drive
            pidDrive = (pid_Kp * pidError) + (pid_Kd * pidDerivative);
            System.out.println(pid_Kp + " * "  + pidError + " = " + pidDrive);
            
                // limit drive
                /*if (pidDrive > PID_DRIVE_MAX) {
             pidDrive = PID_DRIVE_MAX;
             }
             if (pidDrive < PID_DRIVE_MIN) {
             pidDrive = PID_DRIVE_MIN;
             }*/
            // send to motor
            setLeft(-pidDrive);
            setRight(-pidDrive);
            setStrafe(strafe);
            //System.out.println("pidDrive: " + pidDrive + " requested: " + pidRequestedValue);
        } else {
            //negative because sides are mirror images
            aimAngleIsSet = false;
            setLeft(-left);
            setRight(right);
            setStrafe(strafe);
            //System.out.println(gyro.getAngle());
        }
    }

    private double skim(double v) {
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
        if (Math.abs(rightEncoder.getRate()) > SPEED_BARRIER && Math.abs(leftEncoder.getRate()) > SPEED_BARRIER) {
            setHighGearOn();
        }
        if (driveMode == 0) {
            driveHalo(leftY, rightX, leftX);
        } else if (driveMode == 1) {
            driveTank(leftY, rightY);
        } else {
            //if not drive is enabled, enable halo as default drive
            driveMode = 0;
        }
    }

    private void setLeft(double val) {
        for (int i = 0; i < leftVictors.length; i++) {
            leftVictors[i].set(val);
        }
    }

    private void setRight(double val) {
        for (int i = 0; i < rightVictors.length; i++) {
            rightVictors[i].set(val);
        }
    }

    private void setStrafe(double value) {

        strafeVictor.set(-value);
    }

    public void setTankDrive() {
        driveMode = MODE_TANK_DRIVE;
    }

    public void setHaloDrive() {
        driveMode = MODE_HALO_DRIVE;
    }

    public void resetGyro() {
        gyro.reset();
    }

    //turns the solenoid on and off
    private void setDriveShifter(boolean b) {
        if(b){
             driveShifterLeft.set(false);
             driveShifterRight.set(true);
        }
        else if(!b){
            driveShifterRight.set(false);
            driveShifterLeft.set(true);
        }
    }

    public void setLowGearOn() {
        setDriveShifter(false);
    }

    public void setHighGearOn() {
        setDriveShifter(true);
    }

    public void stop() {
        drive(0, 0, 0, 0);
    }

    public void driveForward(int counts) {
        straightPID.setSetpoint(counts);
    }

    public boolean isPrintingDriveInfo() {
        return isPrintingDriveInfo;
    }

    public void setPrintingDriveInfo(boolean b) {
        isPrintingDriveInfo = b;
    }

    public double getKP() {
        return KP_NORMAL;
    }

    public void setKP(double value) {
        KP_NORMAL = value;
    }

    public double getMaxAV() {
        return MAX_ANGULAR_VELOCITY;
    }

    public void setMaxAV(int value) {
        MAX_ANGULAR_VELOCITY = value;
    }
    /*public int getCurrentDriveVoltage() {
     double totalVoltage = left1. + left2.getVoltage() + left3.getVoltage() + right1.getVoltage() + right2.getVoltage() + right3.getVoltage();
     return totalVoltage;
     }*/

    protected void initDefaultCommand() {
    }

    //this class takes care of PIDSource and PIDOutput, IN ONE CLASS , and this straightPID fun is for autonomous only, so it will be unused until autonomous
    //ignore until later
    private class StraightPID implements PIDSource, PIDOutput {

        public double pidGet() {
            return (leftEncoder.get() + rightEncoder.get()) / 2;
        }

        public void pidWrite(double d) {
            if (straightPidEnabled) {
                driveHalo(d, 0, 0);
            }
        }
    }
}