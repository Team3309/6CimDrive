
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Victor;


public class Drive {
    //all of the sensors and motor controllers
  private Victor left1;
  private Victor left2;
  private Victor right1;
  private Victor right2;
  private Encoder leftEncoder;
  private Encoder rightEncoder;
  private modifiedGyro gyro;
  
  //the constructor
  public Drive() {
    //initialize Victors
    left1 = new Victor(RobotMap.DRIVE_LEFT_1);
    left2 = new Victor(RobotMap.DRIVE_LEFT_2);
    right1 = new Victor(RobotMap.DRIVE_RIGHT_1);
    right2 = new Victor(RobotMap.DRIVE_RIGHT_2);
    
    //initialize Encoders
    leftEncoder = new Encoder(RobotMap.DRIVE_ENCODER_LEFT_A, RobotMap.DRIVE_ENCODER_LEFT_B, true, CounterBase.EncodingType.k1X);
    rightEncoder= new Encoder(RobotMap.DRIVE_ENCODER_RIGHT_A, RobotMap.DRIVE_ENCODER_RIGHT_B, false,  CounterBase.EncodingType.k1X);
    
    //initialize gyro
    modifiedGyro gyro = new modifiedGyro(RobotMap.DRIVE_GYRO);
    
  }
  
  //change this to change threshold
  private final double THRESHOLD = .1;
  boolean gyroEnabled = false;
  
  public void driveHalo(double throttle, double turn) {
    
    
  }
  
  public void driveTank(double throttle, double turn) {
      
  }
  
  public void drive(double throttle, double turn) {
      if(gyroEnabled) {
          if (Math.abs(throttle) < THRESHOLD && Math.abs(turn) < THRESHOLD) {
              
          }
      }
  }
  private void setLeft(double val) {
    left1.set(val);
    left2.set(val);
  }
  
  private void setRight(double val) {
    right1.set(val);
    right2.set(val);
  }
  
  boolean straightPidEnabled = false;
  //this class takes care of PIDSource and PIDOutput, IN ONE CLASS OMG, and this straightPID fun is for autonomous only, so it will be unused until autonomous
  private class StraightPID implements PIDSource, PIDOutput {
    public double pidGet() {
     return (leftEncoder.get() + rightEncoder.get()) / 2;
    }

    public void pidWrite(double d) {
     if(straightPidEnabled){
        driveHalo(d, 0);
      }
    }
  }
}
