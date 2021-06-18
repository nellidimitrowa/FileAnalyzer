package com.analyze.fileanalyzer;

import javax.swing.*;

public class Navigation {
    private JPanel mainPanel;

    public Navigation(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public void navigateTo(JPanel nextPanel) {
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();

        mainPanel.add(nextPanel);
        mainPanel.repaint();
        mainPanel.revalidate();
    }
}
