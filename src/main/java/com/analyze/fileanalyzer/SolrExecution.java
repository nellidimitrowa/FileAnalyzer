package com.analyze.fileanalyzer;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.io.*;

public class SolrExecution {

    public String executeSolr(String fileOpen, String collectionName, boolean isLogFile) {
        String serverURL = "http://localhost:8983/solr";
        SolrClient client = new HttpSolrClient.Builder(serverURL).build();
        String text = "";
        try {
            File file = new File(fileOpen);
            text = "<html>";
            BufferedReader buffreader = new BufferedReader(new FileReader(file));
            String readLine = "";
            while ((readLine = buffreader.readLine()) != null) {
                final SolrQuery query = new SolrQuery();
                query.setQuery(readLine);
                query.setRows(10);

                final QueryResponse response = client.query(collectionName,query);
                final SolrDocumentList docs = response.getResults();
                if (isLogFile) {
                    text = showLogData(docs, text);
                } else {
                    text = showFileData(docs, text);
                }
            }
            text.concat("</html>");
            System.out.println(text);
            if(text == "<html>") {
                text = "<html>There is no data for this option in our database.<br>Please, try another option.</html>";
            }
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (SolrServerException solrServerException) {
            solrServerException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return text;
    }

    public String showFileData(SolrDocumentList docs, String text) {
        for(SolrDocument doc : docs) {
            text = text.concat("File path: " + doc.getFieldValue("id") + "<br>");
            text = text.concat("Creation date: " + doc.getFieldValue("date") + "<br>");
            text = text.concat("Content type: " + doc.getFieldValue("stream_content_type") + "<br>");
            text = text.concat("Application name: " + doc.getFieldValue("application_name") + "<br>");
            text = text.concat("Author: " + doc.getFieldValue("last_author") + "<br>");
            text = text.concat("Language: " + doc.getFieldValue("language") + "<br>");
            text = text.concat("<br>");
        }

        return text;
    }

    public String showLogData(SolrDocumentList docs, String text) {
        for(SolrDocument doc : docs) {
            text = text.concat("Time: " + doc.getFirstValue("Time") + "<br>");
            text = text.concat("Component: " + doc.getFirstValue("Component") + "<br>");
            text = text.concat("Description: " + doc.getFirstValue("Description") + "<br>");
            text = text.concat("Origin: " + doc.getFirstValue("Origin") + "<br>");
            text = text.concat("Event context: " + doc.getFirstValue("Event_context") + "<br>");
            text = text.concat("Event name: " + doc.getFirstValue("Event_name") + "<br>");
            text = text.concat("IP address: " + doc.getFirstValue("IP_address") + "<br>");
            text = text.concat("<br>");
        }

        return text;
    }
}
