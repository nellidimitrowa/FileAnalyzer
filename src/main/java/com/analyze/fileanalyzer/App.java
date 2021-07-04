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
    private JTextField solrCollectionNameTextField;
    private JTextField solrQueryTextField;
    private JPanel panelSolrData;
    private JButton panelSolrDataGoBackButton;
    private JLabel panelSolrDataLabel;
    private JLabel panelSolrIndexedDataLabel;
    private JButton panelSolrGoBackButton;
    private JComboBox collectionNameComboBox;
    private JButton viewBgDataButton;
    private JButton viewEnDataButton;
    private JFrame frame;
    private File file;

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

    public void setButtonIcons() {
        Icon tikaIcon = new ImageIcon("D:\\WORD\\UNI\\mag\\FinalProject\\FileAnalyzer\\src\\main\\resources\\icons\\Apache-Tika.png");
        tikaButton.setIcon(tikaIcon);
        Icon solrIcon = new ImageIcon("D:\\WORD\\UNI\\mag\\FinalProject\\FileAnalyzer\\src\\main\\resources\\icons\\solrlogo.png");
        solrButton.setIcon(solrIcon);
        Icon backButtonIcon = new ImageIcon("D:\\WORD\\UNI\\mag\\FinalProject\\FileAnalyzer\\src\\main\\resources\\icons\\back.png");
        panelTikaGoBackButton.setIcon(backButtonIcon);
        panelSolrGoBackButton.setIcon(backButtonIcon);
        panelSolrDataGoBackButton.setIcon(backButtonIcon);
        Icon viewAllDataIcon = new ImageIcon("D:\\WORD\\UNI\\mag\\FinalProject\\FileAnalyzer\\src\\main\\resources\\icons\\all-data.png");
        viewAllDataButton.setIcon(viewAllDataIcon);
        Icon enIcon = new ImageIcon("D:\\WORD\\UNI\\mag\\FinalProject\\FileAnalyzer\\src\\main\\resources\\icons\\en.png");
        viewEnDataButton.setIcon(enIcon);
        Icon bgIcon = new ImageIcon("D:\\WORD\\UNI\\mag\\FinalProject\\FileAnalyzer\\src\\main\\resources\\icons\\bg.png");
        viewBgDataButton.setIcon(bgIcon);
    }

    public void viewSolrData(String queryFilename) {
        String collectionName = collectionNameComboBox.getSelectedItem().toString();
        boolean isLogFile = false;
        if(collectionName == "test") isLogFile = true;
        String text = solr.executeSolr(queryFilename, collectionName, isLogFile);

        panelSolrIndexedDataLabel.setText(text);
        navigation.navigateTo(panelSolrData);
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

                        //getting the list of all meta data elements
                        String[] metadataNames = metadata.names();
                        String metadataText = "<html>";
                        for(String name : metadataNames) {
                            metadataText = metadataText.concat(name + ": " + metadata.get(name) + "<br>");
                        }
                        metadataText.concat("</html>");

                        panelTikaLabel.setText("The metadata of file " + file.getName());
                        navigation.navigateTo(panelTika);
                        panelTikaMetadataLabel.setText(metadataText);
//                        databaseQuery.insertQuery("insert into fileanalyzer.tika (filename, metadata) values ('" + file.getName() + "', '" + metadataText + "')", connection);
//                        databaseQuery.getAllResults(connection);
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

        viewAllDataButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String fileOpen = "D:\\WORD\\UNI\\mag\\FinalProject\\FileAnalyzer\\src\\main\\resources\\queryAll.txt";
                viewSolrData(fileOpen);
            }
        });

        viewBgDataButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String fileOpen = "D:\\WORD\\UNI\\mag\\FinalProject\\FileAnalyzer\\src\\main\\resources\\queryBG.txt";
                viewSolrData(fileOpen);
            }
        });

        viewEnDataButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String fileOpen = "D:\\WORD\\UNI\\mag\\FinalProject\\FileAnalyzer\\src\\main\\resources\\queryEN.txt";
                viewSolrData(fileOpen);
            }
        });

        panelTikaGoBackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                navigation.navigateTo(panelMain);
            }
        });

        panelSolrDataGoBackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                navigation.navigateTo(panelSolr);
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
    }
}
