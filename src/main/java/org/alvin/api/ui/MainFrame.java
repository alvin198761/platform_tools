/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alvin.api.ui;

import org.alvin.api.ui.action.ToolBarAction;

import javax.swing.*;

/**
 *
 * @author tangzhichao
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    public MainFrame() throws Exception {
        initComponents();
        setResizable(false);

//        this.jPanel1.removeAll();
//        PlatPanel pane = new PlatPanel();
//        this.jPanel1.add(pane, BorderLayout.CENTER);
//        this.jPanel1.updateUI();
//        Action action = new ToolBarAction(jPanel1, new PlatPanel(), "云平台");
//        jToolBar1.add(action);

        ToolBarAction action =   new ToolBarAction(jPanel1, new StringPanel(), "常规字符");
        jToolBar1.add(action);
        jToolBar1.add(new ToolBarAction(jPanel1, new StringTabVarPane(), "制表字符"));
        action.actionPerformed(null);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("参数抓取工具 - 唐植超");

        jPanel1.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jToolBar1.setRollover(true);
        jToolBar1.setPreferredSize(new java.awt.Dimension(100, 29));
        getContentPane().add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

}
