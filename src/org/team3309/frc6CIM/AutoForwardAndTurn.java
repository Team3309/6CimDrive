/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3309.frc6CIM;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 * @author Friarbots
 */
public class AutoForwardAndTurn extends Command{

    Drive mDrive = Drive.getInstance();

    public AutoForwardAndTurn() {
        
    }
    protected void initialize() {
        
    }

    protected void execute() {
       Timer timer = new Timer();
      
       timer.start();
       mDrive.drive(0,-.5,0,0);
      
       mDrive.stop();
    }

    protected boolean isFinished() {
        //throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return false;
    }

    protected void end() {
        //throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected void interrupted() {
        //throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
