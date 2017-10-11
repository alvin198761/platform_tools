///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.alvin.api.ui.action;
//
//import java.awt.BorderLayout;
//import java.awt.event.ActionEvent;
//import javax.swing.AbstractAction;
//import javax.swing.JPanel;
//import javax.swing.SwingUtilities;
//import org.alvin.api.ui.PlatPanel;
//
///**
// *
// * @author tangzhichao
// */
//public class PlatViewAction extends AbstractAction {
//
//    private JPanel contentPane;
//
//    public PlatViewAction(JPanel contentPane) {
//        putValue(NAME, "平台工具");
//        this.contentPane = contentPane;
//        actionPerformed(null);
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        this.contentPane.removeAll();
//        PlatPanel pane = new PlatPanel();
//        this.contentPane.add(pane, BorderLayout.CENTER);
//        this.contentPane.updateUI();
//    }
//
//}
