package com.analyze.fileanalyzer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class App {
    private JPanel panelApp;
    private JPanel panelMain;
    private JPanel panelFileMenu;
    private JButton buttonChooseFile;
    private JButton buttonSolr;
    private JLabel labelHello;
    private JButton buttonAnotherFile;
    private JButton buttonTika;
    private JLabel labelSelectedFile;
    private JFrame frame;

    public static void main(String[] args) {
        App app = new App();
        app.createApp();
    }

    public void createApp() {
        frame = new JFrame("File analyzer");
        frame.setLayout(null);
        frame.setSize(620, 410);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        ImageIcon imageBackground = new ImageIcon("D:/WORD/UNI/Магистър/FinalProject/FileAnalyzer/src/main/resources/app-background.jpg");
//        JLabel background = new JLabel("", imageBackground, JLabel.CENTER);
//        background.setBounds(0,0,620,410);
//        frame.add(background);
        frame.setContentPane(panelApp);
        frame.setVisible(true);
    }

    public App() {
        panelApp.removeAll();
        panelApp.add(panelMain);
        panelApp.repaint();
        panelApp.revalidate();

        buttonChooseFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int option = fileChooser.showOpenDialog(frame);
                if(option == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();
                    labelSelectedFile.setText("Selected: " + file.getName());
                    panelApp.removeAll();
                    panelApp.repaint();
                    panelApp.revalidate();

                    panelApp.add(panelFileMenu);
                    panelApp.repaint();
                    panelApp.revalidate();
                } else {
                    labelHello.setText("Open command canceled");
                }
            }
        });
        buttonAnotherFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panelApp.removeAll();
                panelApp.repaint();
                panelApp.revalidate();

                panelApp.add(panelMain);
                panelApp.repaint();
                panelApp.revalidate();
            }
        });
    }
}
