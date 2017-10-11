/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alvin.api.ui.action;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *
 * @author tangzhichao
 */
public class ToolBarAction extends AbstractAction {

    private JPanel contentPane;
    private JComponent compnent;

    public ToolBarAction(JPanel contentPane ,JComponent compnent ,String title) {
        putValue(NAME, title);
        this.contentPane = contentPane;
        this.compnent = compnent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.contentPane.removeAll();
        this.contentPane.add(compnent, BorderLayout.CENTER);
        this.contentPane.updateUI();
    }

}
