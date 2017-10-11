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
//import org.alvin.api.ui.StringPanel;
//
///**
// *
// * @author tangzhichao
// */
//public class StringViewAction extends AbstractAction {
//
//    private JPanel contentPane;
//    public StringViewAction(JPanel contentPane) {
//        putValue(NAME, "字符串转换");
//        this.contentPane = contentPane;
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        this.contentPane.removeAll();
//        StringPanel pane = new StringPanel();
//        this.contentPane.add(pane,BorderLayout.CENTER);
//        this.contentPane.updateUI();
//    }
//
//}
