package com.analyze.fileanalyzer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
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
    private JButton solrButton;
    private JButton tikaButton;
    private JLabel helloLabel;
    private JPanel panelTika;
    private JButton panelTikaGoBackButton;
    private JLabel panelTikaLabel;
    private JLabel panelTikaMetadataLabel;
    private JPanel panelSolr;
    private JButton viewAllDataButton;
    private JPanel panelSolrData;
    private JButton panelSolrDataGoBackButton;
    private JLabel panelSolrDataLabel;
    private JLabel panelSolrIndexedDataLabel;
    private JButton panelSolrGoBackButton;
    private JComboBox collectionNameComboBox;
    private JButton viewBgDataButton;
    private JButton viewEnDataButton;
    private JButton viewLogFileButton;
    private JButton viewLogFileByIPButton;
    private JPanel panelLogIP;
    private JLabel ipLabel;
    private JButton ipButton;
    private JTextField ipTextField;
    private JButton ipGoBackButton;
    private JFrame frame;
    private File file;
    private String previousScreen = "";

    Navigation navigation = new Navigation(panelApp);
    SolrExecution solr = new SolrExecution();

    public static void main(String[] args) {
        App app = new App();
        app.createApp();
    }

    public void createApp() {
        frame = new JFrame("File analyzer");
        frame.setSize(620, 410);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        ImageIcon imageBackground = new ImageIcon("D:/WORD/UNI/Магистър/FinalProject/FileAnalyzer/src/main/resources/app-background.jpg");
//        JLabel background = new JLabel("", imageBackground, JLabel.CENTER);
//        background.setBounds(0,0,620,410);
//        frame.add(background);
        frame.setContentPane(panelApp);
        panelApp.setBackground(Color.BLUE);
        frame.getContentPane().setBackground(new Color(255, 235, 205));
        setButtonIcons();
        helloLabel.setFont(helloLabel.getFont().deriveFont(28.0f));
        frame.setVisible(true);
    }

    public App() {
        navigation.navigateTo(panelMain);

        tikaButton.addActionListener(new ActionListener()  {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int option = fileChooser.showOpenDialog(frame);
                if(option == JFileChooser.APPROVE_OPTION){
                    file = fileChooser.getSelectedFile();
                    navigation.navigateTo(panelTika);
                    try {
                        Parser parser = new AutoDetectParser();
                        BodyContentHandler handler = new BodyContentHandler();
                        Metadata metadata = new Metadata();
                        FileInputStream inputstream = new FileInputStream(file);
                        ParseContext context = new ParseContext();
                        parser.parse(inputstream, handler, metadata, context);

                        String[] metadataNames = metadata.names();
                        String metadataText = "<html>";
                        for(String name : metadataNames) {
                            metadataText = metadataText.concat(name + ": " + metadata.get(name) + "<br>");
                        }
                        metadataText.concat("</html>");

                        panelTikaLabel.setText("The metadata of file " + file.getName());
                        navigation.navigateTo(panelTika);
                        panelTikaMetadataLabel.setText(metadataText);
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
            }
        });

        ipButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String ipAddress = ipTextField.getText();
                if (ipAddress.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter IP address");
                }
                try {
                    FileWriter myWriter = new FileWriter("D:\\WORD\\UNI\\mag\\FinalProject\\FileAnalyzer\\src\\main\\resources\\queryLogByIP.txt");
                    myWriter.write("IP_address:" + ipAddress);
                    myWriter.close();
                    ipTextField.setText("");
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                String collectionName = "test";
                String fileOpen = "D:\\WORD\\UNI\\mag\\FinalProject\\FileAnalyzer\\src\\main\\resources\\queryLogByIP.txt";
                viewSolrData(fileOpen, collectionName, true);
                previousScreen = "panelLogIP";
            }
        });

        viewAllDataButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String collectionName = collectionNameComboBox.getSelectedItem().toString();
                String fileOpen = "D:\\WORD\\UNI\\mag\\FinalProject\\FileAnalyzer\\src\\main\\resources\\queryAll.txt";
                viewSolrData(fileOpen, collectionName, false);
            }
        });

        viewBgDataButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String collectionName = collectionNameComboBox.getSelectedItem().toString();
                String fileOpen = "D:\\WORD\\UNI\\mag\\FinalProject\\FileAnalyzer\\src\\main\\resources\\queryBG.txt";
                viewSolrData(fileOpen, collectionName, false);
            }
        });

        viewEnDataButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String collectionName = collectionNameComboBox.getSelectedItem().toString();
                String fileOpen = "D:\\WORD\\UNI\\mag\\FinalProject\\FileAnalyzer\\src\\main\\resources\\queryEN.txt";
                viewSolrData(fileOpen, collectionName, false);
            }
        });

        viewLogFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String collectionName = "test";
                String fileOpen = "D:\\WORD\\UNI\\mag\\FinalProject\\FileAnalyzer\\src\\main\\resources\\queryAll.txt";
                viewSolrData(fileOpen, collectionName, true);
                previousScreen = "panelMain";
            }
        });

        panelSolrDataGoBackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (previousScreen.equals("panelMain")) {
                    navigation.navigateTo(panelMain);
                }else if (previousScreen.equals("panelLogIP")) {
                    navigation.navigateTo(panelLogIP);
                } else {
                    navigation.navigateTo(panelSolr);
                }
                previousScreen = "";
            }
        });

        panelTikaGoBackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                navigation.navigateTo(panelMain);
            }
        });

        panelSolrGoBackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                navigation.navigateTo(panelMain);
            }
        });

        solrButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                navigation.navigateTo(panelSolr);
            }
        });

        viewLogFileByIPButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                navigation.navigateTo(panelLogIP);
            }
        });

        ipGoBackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                navigation.navigateTo(panelMain);
            }
        });
    }

    public void viewSolrData(String queryFilename, String collectionName, boolean isLogFile) {
        String text = solr.executeSolr(queryFilename, collectionName, isLogFile);

        panelSolrIndexedDataLabel.setText(text);
        navigation.navigateTo(panelSolrData);
    }

    public void setButtonIcons() {
        Icon tikaIcon = new ImageIcon("D:\\WORD\\UNI\\mag\\FinalProject\\FileAnalyzer\\src\\main\\resources\\icons\\Apache-Tika.png");
        tikaButton.setIcon(tikaIcon);
        Icon solrIcon = new ImageIcon("D:\\WORD\\UNI\\mag\\FinalProject\\FileAnalyzer\\src\\main\\resources\\icons\\solrlogo.png");
        solrButton.setIcon(solrIcon);
        Icon backButtonIcon = new ImageIcon("D:\\WORD\\UNI\\mag\\FinalProject\\FileAnalyzer\\src\\main\\resources\\icons\\back.png");
        panelTikaGoBackButton.setIcon(backButtonIcon);
        panelSolrGoBackButton.setIcon(backButtonIcon);
        panelSolrDataGoBackButton.setIcon(backButtonIcon);
        ipGoBackButton.setIcon(backButtonIcon);
        Icon viewAllDataIcon = new ImageIcon("D:\\WORD\\UNI\\mag\\FinalProject\\FileAnalyzer\\src\\main\\resources\\icons\\all-data.png");
        viewAllDataButton.setIcon(viewAllDataIcon);
        Icon enIcon = new ImageIcon("D:\\WORD\\UNI\\mag\\FinalProject\\FileAnalyzer\\src\\main\\resources\\icons\\en.png");
        viewEnDataButton.setIcon(enIcon);
        Icon bgIcon = new ImageIcon("D:\\WORD\\UNI\\mag\\FinalProject\\FileAnalyzer\\src\\main\\resources\\icons\\bg.png");
        viewBgDataButton.setIcon(bgIcon);
        Icon logIcon = new ImageIcon("D:\\WORD\\UNI\\mag\\FinalProject\\FileAnalyzer\\src\\main\\resources\\icons\\log.png");
        viewLogFileButton.setIcon(logIcon);
        ipButton.setIcon(logIcon);
        Icon logIPIcon = new ImageIcon("D:\\WORD\\UNI\\mag\\FinalProject\\FileAnalyzer\\src\\main\\resources\\icons\\ip.png");
        viewLogFileByIPButton.setIcon(logIPIcon);
    }
}
