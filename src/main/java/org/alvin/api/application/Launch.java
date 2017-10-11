package org.alvin.api.application;

import org.alvin.api.ui.MainFrame;
import org.alvin.api.utils.ServiceObjectFactory;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Launch {

    public static void main(String[] args) throws Exception {
        ServiceObjectFactory.doScanService("org.alvin.api.service.impl");

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            javax.swing.UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        // Create and display the form
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new MainFrame().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(Launch.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
