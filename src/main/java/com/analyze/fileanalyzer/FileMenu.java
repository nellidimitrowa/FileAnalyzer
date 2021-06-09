package com.analyze.fileanalyzer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FileMenu {
    private JPanel panelFileMenu;
    private JButton buttonTika;
    private JButton buttonSolr;
    private JLabel labelSelectedFile;
    private JButton buttonAnotherFile;
    private File selectedFile;

    public JPanel getPanelFileMenu() {
        return panelFileMenu;
    }

    public void setPanelFileMenu(JPanel panelFileMenu) {
        this.panelFileMenu = panelFileMenu;
    }

    public JButton getButtonTika() {
        return buttonTika;
    }

    public void setButtonTika(JButton buttonTika) {
        this.buttonTika = buttonTika;
    }

    public JButton getButtonSolr() {
        return buttonSolr;
    }

    public void setButtonSolr(JButton buttonSolr) {
        this.buttonSolr = buttonSolr;
    }

    public JLabel getLabelSelectedFile() {
        return labelSelectedFile;
    }

    public void setLabelSelectedFile(JLabel labelSelectedFile) {
        this.labelSelectedFile = labelSelectedFile;
    }

    public JButton getButtonAnotherFile() {
        return buttonAnotherFile;
    }

    public void setButtonAnotherFile(JButton buttonAnotherFile) {
        this.buttonAnotherFile = buttonAnotherFile;
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    public void createFileMenuWindow() {
        JFrame frame = new JFrame("File analyzer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panelFileMenu);
        createUI(frame);
        frame.setSize(560, 350);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void createUI(final JFrame frame) {
        labelSelectedFile.setText("Selected: " + selectedFile.getName());
        buttonTika.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
