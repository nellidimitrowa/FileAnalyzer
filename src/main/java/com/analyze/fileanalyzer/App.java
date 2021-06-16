package com.analyze.fileanalyzer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

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
    private JPanel panelFileMenu;
    private JButton buttonChooseFile;
    private JButton buttonSolr;
    private JLabel labelHello;
    private JButton buttonAnotherFile;
    private JButton buttonTika;
    private JLabel labelSelectedFile;
    private JPanel panelTika;
    private JButton panelTikaGoBackButton;
    private JLabel panelTikaLabel;
    private JLabel panelTikaMetadataLabel;
    private JPanel panelSolr;
    private JButton buttonExecuteTika;
    private JTextField textFieldCollectionName;
    private JTextField textFieldQuery;
    private JPanel panelSolrData;
    private JButton panelSolrDataGoBackButton;
    private JLabel panelSolrDataLabel;
    private JLabel panelSolrIndexedDataLabel;
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
                    String metadataText = "<html>";
                    for(String name : metadataNames) {
                        metadataText = metadataText.concat(name + ": " + metadata.get(name) + "<br>");
                    }
                    metadataText.concat("</html>");

                    panelTikaLabel.setText("The metadata of file " + file.getName());
                    panelApp.removeAll();
                    panelApp.repaint();
                    panelApp.revalidate();

                    panelApp.add(panelTika);
                    panelApp.repaint();
                    panelApp.revalidate();

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
        });
        panelTikaGoBackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panelApp.removeAll();
                panelApp.repaint();
                panelApp.revalidate();

                panelApp.add(panelFileMenu);
                panelApp.repaint();
                panelApp.revalidate();
            }
        });
        buttonSolr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panelApp.removeAll();
                panelApp.repaint();
                panelApp.revalidate();

                panelApp.add(panelSolr);
                panelApp.repaint();
                panelApp.revalidate();
            }
        });
        buttonExecuteTika.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String serverURL = "http://localhost:8983/solr";
                SolrClient client = new HttpSolrClient.Builder(serverURL).build();
                String collectionName = textFieldCollectionName.getText();
                String fileOpen = textFieldQuery.getText();
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

                    panelApp.removeAll();
                    panelApp.repaint();
                    panelApp.revalidate();

                    panelApp.add(panelSolrData);
                    panelApp.repaint();
                    panelApp.revalidate();
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                } catch (SolrServerException solrServerException) {
                    solrServerException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
        });
    }
}
