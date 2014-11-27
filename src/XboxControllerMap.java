/** 
This class is the map used for XboxController.java.  
Call "Controller.getRawButton(XboxControllerMap.BUTTON_A);" to use button A (boolean)

Just stole the map from 2013 robot, changed some names of the constants, and now its mine
**/
//
public class XboxControllerMap {
  
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
    private static final int AXIS_LEFT_X = 1;
    private static final int AXIS_LEFT_Y = 2;
    private static final int AXIS_TRIGGER = 3; //return value of right(RT) - left(LT)
    private static final int AXIS_RIGHT_X = 4;
    private static final int AXIS_RIGHT_Y = 5;
}
