package com.analyze.fileanalyzer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

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
    private JPanel panelTika;
    private JTextArea panelTikaTextArea;
    private JButton panelTikaGoBackButton;
    private JLabel panelTikaLabel;
    private JTextArea panelTikaMetadataTextArea;
    private JLabel panelTikaMetadataLabel;
    private JFrame frame;
    private File file;

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
                    file = fileChooser.getSelectedFile();
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

        buttonTika.addActionListener(new ActionListener()  {
            public void actionPerformed(ActionEvent e) {
                try {
                    Parser parser = new AutoDetectParser();
                    BodyContentHandler handler = new BodyContentHandler();
                    Metadata metadata = new Metadata();
                    FileInputStream inputstream = new FileInputStream(file);
                    ParseContext context = new ParseContext();

                    parser.parse(inputstream, handler, metadata, context);

                    //getting the list of all meta data elements
                    String[] metadataNames = metadata.names();
                    String metadataText = "";
                    for(String name : metadataNames) {
                        metadataText = metadataText.concat(name + ": " + metadata.get(name) + "\n");
                    }

                    panelTikaLabel.setText("The metadata of file " + file.getName());
                    panelApp.removeAll();
                    panelApp.repaint();
                    panelApp.revalidate();

                    panelApp.add(panelTika);
                    panelApp.repaint();
                    panelApp.revalidate();

                    panelTikaMetadataTextArea.setText(metadataText);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                } catch (SAXException saxException) {
                    saxException.printStackTrace();
                } catch (TikaException tikaException) {
                    tikaException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
    }
}
