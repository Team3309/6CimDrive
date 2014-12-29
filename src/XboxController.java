
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;



 /**
    Summary of methods used:
     double axis = Controller.getRawAxis(axisNumber); 
     boolean button = Controller.getRawButton(buttonNumber);
        Constants define these values in XboxControllerMap.java, so replace "buttonNumber" with "XboxControllerMap.BUTTON_A"
    This Class is the main class for Xbox remotes, it pretty much just applies the Joystick class and makes it work with xbox remotes
    The methods below are called in TeleopPeriodic inside the main robot class to see if buttons are presed or not. (Axes too)
  **/
public class XboxController extends GenericHID{

  ////BUTTONS\\\\
  //Base Buttons
  public static final int BUTTON_A = 1;
  public static final int BUTTON_B = 2;
  public static final int BUTTON_X = 3;
  public static final int BUTTON_Y = 4;
  //DPAD
  public static final int BUTTON_DPAD_UP = 5;
  public static final int BUTTON_DPAD_DOWN = 6;
  public static final int BUTTON_DPAD_LEFT = 7;
  public static final int BUTTON_DPAD_RIGHT = 8;
  //Start and Back
  public static final int BUTTON_START = 8;
  public static final int BUTTON_BACK = 7;
  //Sticks
  public static final int BUTTON_LEFT_STICK = 9;
  public static final int BUTTON_RIGHT_STICK = 10;
  //Bumpers and Home
  public static final int BUTTON_LB = 5;
  public static final int BUTTON_RB = 6;
  public static final int BUTTON_HOME = 15;
    
    
  ////AXES\\\\
  public static final int AXIS_LEFT_X = 1;
  public static final int AXIS_LEFT_Y = 2;
  public static final int AXIS_TRIGGER = 3; //return value of right(RT) - left(LT)
  public static final int AXIS_RIGHT_X = 4;
  public static final int AXIS_RIGHT_Y = 5;
    
  //main instance joystick being called throughout class 
  Joystick Controller;
  
 
  //info for usage of xbox remotes found at - http://www.chiefdelphi.com/forums/showthread.php?t=83597
  //Constructor, takes number and makes xbox remote that number joystick that is set by driver station
  public XboxController(int joystickNum) {
    Controller = new Joystick(joystickNum);
  }
  
  
  //Now, here are all the button methods, they all return a boolean that returns true if button is pressed (obviously)
  public boolean getA() {
    return Controller.getRawButton(XboxControllerMap.BUTTON_A);
  } 
  
  public boolean getB() {
    return Controller.getRawButton(XboxControllerMap.BUTTON_B);
  } 
  
  public boolean getXBut() {
    return Controller.getRawButton(XboxControllerMap.BUTTON_X);
  } 
  
  public boolean getYBut() {
    return Controller.getRawButton(XboxControllerMap.BUTTON_Y);
  } 
  
  public boolean getDpadUp() {
    return Controller.getRawButton(XboxControllerMap.BUTTON_DPAD_UP);
  } 
  
  public boolean getDpadDown() {
    return Controller.getRawButton(XboxControllerMap.BUTTON_DPAD_DOWN);
  } 
  
  public boolean getDpadLeft() {
    return Controller.getRawButton(XboxControllerMap.BUTTON_DPAD_LEFT);
  } 
  
  public boolean getDpadRight() {
    return Controller.getRawButton(XboxControllerMap.BUTTON_DPAD_RIGHT);
  } 
  
  public boolean getBack() {
    return Controller.getRawButton(XboxControllerMap.BUTTON_BACK);
  }
  
  public boolean getStart() {
    return Controller.getRawButton(XboxControllerMap.BUTTON_START);
  } 
  
  public boolean getLeftStickButton() {
    return Controller.getRawButton(XboxControllerMap.BUTTON_LEFT_STICK);
  }
  
  public boolean getRightStickButton() {
    return Controller.getRawButton(XboxControllerMap.BUTTON_RIGHT_STICK);
  } 
  
  public boolean getLB() {
    return Controller.getRawButton(XboxControllerMap.BUTTON_LB);
  } 
  
  public boolean getRB() {
    return Controller.getRawButton(XboxControllerMap.BUTTON_RB);
  } 
  
  public boolean getHome() {
    return Controller.getRawButton(XboxControllerMap.BUTTON_HOME);
  }
  
  
  //Next are the methods for getting the Axes, they return a double
  //  You may notice that each method has a temp value and a scaledVal.  
  //  The temp value is what that current axis is at.
  //  The scaledVal just takes the temp value and scales it.
  //  Chagne the scaleAxis method to add a deadband, or to add a constant multiplier to every axis.
  
  
  //Returns from -1 to 1
  public double getLeftX() {
    double temp = Controller.getRawAxis(XboxControllerMap.AXIS_LEFT_X);
    double scaledVal = scaleAxis(temp);
    return scaledVal;
  }
  //Returns from -1 to 1
  public double getLeftY() {
     double temp = Controller.getRawAxis(XboxControllerMap.AXIS_LEFT_Y);
     double scaledVal = scaleAxis(temp);
     return scaledVal;
  }
  //Not really sure what this return, I read somewhere that it returns rightTrigger - leftTrigger where each trigger returns from 0 to 1
  public double getTriggers() {
    double temp = Controller.getRawAxis(XboxControllerMap.AXIS_TRIGGER);
    double scaledVal = scaleAxis(temp);
    return scaledVal;
  }
  //Returns from -1 to 1
  public double getRightX() {
    double temp = Controller.getRawAxis(XboxControllerMap.AXIS_RIGHT_X);
    double scaledVal = scaleAxis(temp);
    return scaledVal;
  }
  //Returns from -1 to 1
  public double getRightY() {
     double temp = Controller.getRawAxis(XboxControllerMap.AXIS_RIGHT_Y);
     double scaledVal = scaleAxis(temp);
     return scaledVal;
  }  
  
  //btw this is your deadband
  static final double DEADBAND = .1;
  
  //here is the scaling method used in axes
  private double scaleAxis(double value) {
    //if the joystick is not pressed enough, don't move.  We dont like crawling spider robots.
    if(Math.abs(value) < DEADBAND) {
      return 0;
    } 
    //this spot would be where you could scale stuff
    return value;
  }
  
  //required to work
   public double getX(Hand hand) {
        if(hand.equals(Hand.kLeft))
            return getLeftX();
        else
            return getRightX();
    }

    public double getY(Hand hand) {
        if(hand.equals(Hand.kLeft))
            return getLeftY();
        else
            return getRightY();
    }

    public double getZ(Hand hand) {
        if(hand.equals(Hand.kLeft))
            return 0;
        else
            return getTriggers();
    }

    public double getTwist() {
        return 0;
    }

    public double getThrottle() {
        return 0;
    }

    public double getRawAxis(int i) {
        return Controller.getRawAxis(i);
    }

    public boolean getTrigger(Hand hand) {
        if(hand.equals(Hand.kLeft))
            return getLB();
        else
            return getRB();
    }

    public boolean getTop(Hand hand) {
        return false;
    }

    public boolean getBumper(Hand hand) {
        if(hand.equals(Hand.kLeft))
            return getLB();
        else
            return getRB();
    }

    public boolean getRawButton(int i) {
        return Controller.getRawButton(i);
    }
  
}//END OF CLASS
  
