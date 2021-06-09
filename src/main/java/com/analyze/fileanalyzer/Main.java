package com.analyze.fileanalyzer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Main {
    private JButton buttonChooseFile;
    private JPanel panelMain;
    private JLabel labelHello;

    public void createMainWindow() {
        JFrame frame = new JFrame("File analyzer");
        frame.setLayout(null);
        frame.setSize(620, 410);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        ImageIcon imageBackground = new ImageIcon("D:/WORD/UNI/Магистър/FinalProject/FileAnalyzer/src/main/resources/app-background.jpg");
//        JLabel background = new JLabel("", imageBackground, JLabel.CENTER);
//        background.setBounds(0,0,620,410);
//        frame.add(background);
        frame.setContentPane(panelMain);
        createUI(frame);
        frame.setVisible(true);
    }

    public void createUI(final JFrame frame) {
        buttonChooseFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int option = fileChooser.showOpenDialog(frame);
                if(option == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();
                    frame.setVisible(false);
                    FileMenu fileMenu = new FileMenu();
                    fileMenu.setSelectedFile(file);
                    fileMenu.createFileMenuWindow();
                } else {
                    labelHello.setText("Open command canceled");
                }
            }
        });
    }
}
