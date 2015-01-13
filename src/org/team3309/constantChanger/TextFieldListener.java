/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3309.constantchanger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import org.team3309.frc6CIM.subsystems.Drive;

/**
 *
 * @author Friarbots
 */
public class TextFieldListener implements ActionListener{
    private String code;
    private JTextField field;
    public TextFieldListener(String str, JTextField field) {
        super();
        this.field = field;
        code = str;
    }
    public void actionPerformed(ActionEvent e) {
        System.out.println("PERFORMED");
        if(code.equals("KP")) {
            Drive.getInstance().setKP(Double.parseDouble(field.getText()));
        }else if(code.equals("MaxAV")) {
            
        }
    }
    
}
