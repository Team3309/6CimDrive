
import edu.wpi.first.wpilibj.Victor;


public class Drive {
  private Victor left1;
  private Victor left2;
  private Victor right1;
  private Victor right2;
  private Encoder leftEncoder;
  private Encoder rightEncoder;
  
  public Drive() {
    //initialize Victors
    left1 = new Victor(RobotMap.DRIVE_LEFT_1);
    left2 = new Victor(RobotMap.DRIVE_LEFT_2);
    right1 = new Victor(RobotMap.DRIVE_RIGHT_1);
    right2 = new Victor(RobotMap.DRIVE_RIGHT_2);
    
    //initialize Encoders
    leftEncoder = new Encoder(RobotMap.DRIVE_ENCODER_LEFT_A, RobotMap.DRIVE_ENCODER_LEFT_B, true, CounterBase.EncodingType.k1X);
    rightEncoder= new Encoder(RobotMap.DRIVE_ENCODER_RIGHT_A, RobotMap.DRIVE_ENCODER_RIGHT_B, false,  CounterBase.EncodingType.k1X);
    
  }
  
  public void drive(double throttle, double turn) {
    
    
  }
  
  private void setLeft(double val) {
    left1.set(val);
    left2.set(val);
  }
  
  private void setRight(double val) {
    right1.set(val);
    right2.set(val);
  }
}
