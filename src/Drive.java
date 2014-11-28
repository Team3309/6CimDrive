
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
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
    private modifiedGyro gyro;
    
    //all the booleans for which drive is being used
    boolean haloDriveEnabled = true;
    boolean tankDriveEnabled = false;

    //the constructor
    public Drive() {
        //initialize Victors
        left1 = new Victor(RobotMap.DRIVE_LEFT_1);
        left2 = new Victor(RobotMap.DRIVE_LEFT_2);
        left3 = new Victor(RobotMap.DRIVE_LEFT_3);
        right1 = new Victor(RobotMap.DRIVE_RIGHT_1);
        right2 = new Victor(RobotMap.DRIVE_RIGHT_2);
        right3 = new Victor(RobotMap.DRIVE_RIGHT_3);

        //initialize Encoders
        leftEncoder = new Encoder(RobotMap.DRIVE_ENCODER_LEFT_A, RobotMap.DRIVE_ENCODER_LEFT_B, true, CounterBase.EncodingType.k1X);
        rightEncoder = new Encoder(RobotMap.DRIVE_ENCODER_RIGHT_A, RobotMap.DRIVE_ENCODER_RIGHT_B, false, CounterBase.EncodingType.k1X);

        //initialize gyro
        modifiedGyro gyro = new modifiedGyro(RobotMap.DRIVE_GYRO);

    }

    //change this to change threshold
    private final double THRESHOLD = .1;
    //tells if gyro is a yay or nay
    boolean gyroEnabled = false;
    //the max angular velocity (duh)
    private final int MAX_ANGULAR_VELOCITY = 720;
    
    //Now for all the possible kp constants
    private final double KP_NORMAL = .02;
    
    //joystick is driven like a halo warthog, left joystick goes forward and backward, right joystick goes left and right
    public void driveHalo(double throttle, double turn) {
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

        setLeft(left);
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
    public void driveTank(double leftY, double rightY) {
        setLeft(leftY);
        setRight(rightY);
    }

    public void drive(double leftX, double leftY, double rightX, double rightY) {
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
    
    //enable this is you ever want to go just forward
    boolean straightPidEnabled = false;

    //this class takes care of PIDSource and PIDOutput, IN ONE CLASS OMG, and this straightPID fun is for autonomous only, so it will be unused until autonomous
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
