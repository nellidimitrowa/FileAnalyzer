package com.analyze.fileanalyzer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
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
    private JButton executeSolrButton;
    private JTextField solrCollectionNameTextField;
    private JTextField solrQueryTextField;
    private JPanel panelSolrData;
    private JButton panelSolrDataGoBackButton;
    private JLabel panelSolrDataLabel;
    private JLabel panelSolrIndexedDataLabel;
    private JButton panelSolrGoBackButton;
    private JComboBox collectionNameComboBox;
    private JFrame frame;
    private File file;

    Navigation navigation = new Navigation(panelApp);
    static DatabaseQuery databaseQuery = new DatabaseQuery();
    static Connection connection = null;

    public static void main(String[] args) {
        App app = new App();
        app.createApp();
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fileanalyzer?allowPublicKeyRetrieval=true&useSSL=false", "root", "111697");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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

        executeSolrButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String serverURL = "http://localhost:8983/solr";
                SolrClient client = new HttpSolrClient.Builder(serverURL).build();
                String collectionName = collectionNameComboBox.getSelectedItem().toString();
                String fileOpen = "D:\\WORD\\UNI\\mag\\FinalProject\\FileAnalyzer\\src\\main\\resources\\queryAll.txt";
                try {
                    File file = new File(fileOpen);
                    String text = "<html>";
                    BufferedReader buffreader = new BufferedReader(new FileReader(file));
                    String readLine = "";
                    while ((readLine = buffreader.readLine()) != null) {
                        final SolrQuery query = new SolrQuery();
                        query.setQuery(readLine);
                        query.setRows(10);

                        final QueryResponse response = client.query(collectionName,query);
                        final SolrDocumentList docs = response.getResults();
                        text = text.concat(docs.toString() + "<br>");
                    }
                    text.concat("</html>");
                    panelSolrIndexedDataLabel.setText(text);

                    navigation.navigateTo(panelSolrData);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                } catch (SolrServerException solrServerException) {
                    solrServerException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

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
