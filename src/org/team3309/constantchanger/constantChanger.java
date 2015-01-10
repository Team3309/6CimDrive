/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team3309.constantchanger;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.team3309.frc6CIM.subsystems.Drive;
/**
 *
 * @author Friarbots
 */
public class constantChanger extends JFrame {
    public constantChanger() {
        super("Constant Changer");
        setSize(300, 300);
        setLocation(0, 0);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
    }
    public void init() {
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.TRAILING);
        
        setLayout(new FlowLayout());
        JPanel northPanel = new JPanel(layout);
        JPanel southPanel = new JPanel(layout);
        
        JLabel label1 = new JLabel("KP: ");
        JTextField kpConstantField = new JTextField(String.valueOf(Drive.getInstance().getKP()), 10);
        northPanel.add(label1);
        northPanel.add(kpConstantField);
        
        JLabel label2 = new JLabel("MAX AV: ");
        JTextField maxAngularVelocityField = new JTextField(String.valueOf(Drive.getInstance().getMaxAV()),10);
        southPanel.add(label2);
        southPanel.add(maxAngularVelocityField);
        
        kpConstantField.setVisible(true);
        kpConstantField.addActionListener(new TextFieldListener());
        getContentPane().add(northPanel, BorderLayout.NORTH);
        getContentPane().add(southPanel, BorderLayout.SOUTH);

        pack();
    }
}
