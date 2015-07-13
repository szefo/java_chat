package com.szefo.client;

import com.szefo.Message;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;


public class History {

    private String filePath;

    public History(String filePath) {
        this.filePath = filePath;
    }

    public static String getTagValue(String sTag, Element element) {
        NodeList nodeList = element.getElementsByTagName(sTag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    public void addMessage(Message msg, String msgTime) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filePath);

            Node node = document.getFirstChild();

            Element message = document.createElement("message");
            Element sender = document.createElement("sender");
            sender.setTextContent(msg.getSender());
            Element content = document.createElement("content");
            content.setTextContent(msg.getContent());
            Element recipient = document.createElement("recipient");
            recipient.setTextContent(msg.getRecipient());
            Element time = document.createElement("time");
            time.setTextContent(msgTime);

            message.appendChild(sender);
            message.appendChild(content);
            message.appendChild(recipient);
            message.appendChild(time);
            node.appendChild(message);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public void fillTable(HistoryFrame historyFrame) {
        DefaultTableModel model = (DefaultTableModel) historyFrame.getjTable().getModel();

        try {
            File fileXml = new File(filePath);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(fileXml);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("message");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    model.addRow(new Object[]{
                            getTagValue("sender", element),
                            getTagValue("content", element),
                            getTagValue("recipient", element),
                            getTagValue("time", element)});
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}































