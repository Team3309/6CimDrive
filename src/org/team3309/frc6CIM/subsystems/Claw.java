/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3309.frc6CIM.subsystems;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.team3309.frc6CIM.RobotMap;

/**
 *
 * @author Friarbots
 */
public class Claw extends Subsystem {

    private Victor rightClaw;
    private Victor leftClaw;

    private static Claw instance;

    public static Claw getInstance() {
        if (instance == null) {
            instance = new Claw();
        }
        return instance;
    }

    private Claw() {
        rightClaw = new Victor(RobotMap.CLAW_RIGHTSIDE);
        leftClaw = new Victor(RobotMap.CLAW_LEFTSIDE);

    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public void runClawInward() {
        rightClaw.set(1);
        leftClaw.set(1);
    }

    public void runClawOutward() {
        rightClaw.set(-1);
        leftClaw.set(-1);
    }

    public void stopClaw() {
        rightClaw.set(0);
        leftClaw.set(0);
    }
}
