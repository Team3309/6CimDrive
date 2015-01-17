/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3309.frc6CIM.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.team3309.frc6CIM.subsystems.Drive;

/**
 *
 * @author Friarbots
 */
public class PIDCommand {

    private boolean pidLoopFinished = false;
    private double kP;
    private double kD;
    private double pidRequestedValue;
    private double pidLastError;
    private double pidSensorCurrentValue;
    
    private double currentThrottle;

    private double pidError;

    private double pidDerivative;
    private double pidDrive;

    public PIDCommand(double kP, double kD, double pidRequestedValue) {
        this.kP = kP;
        this.kD = kD;
        this.pidRequestedValue = pidRequestedValue;
    }

    public void setCurrent(double current, double throttle) {
        pidSensorCurrentValue = current;
        currentThrottle = throttle;
    }

    public void run() {
        // calculate error
        pidError = pidSensorCurrentValue - pidRequestedValue;
     
        // calculate the derivative
        pidDerivative = pidError - pidLastError;
        pidLastError = pidError;
        //System.out.println("Last Error: " + pidLastError);

        // calculate drive
        pidDrive = ((kP * pidError) + (kD* pidDerivative));
        double leftPower = (currentThrottle - pidDrive);
        double rightPower = (currentThrottle + pidDrive);
        Drive.getInstance().setLeft(leftPower);
        Drive.getInstance().setRight(rightPower);
    }

}
