package com.ntsan;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CBRXMLParser {

    public void startApp() throws Exception {
        String[][] rates = getRates();
        for (String[] rate : rates) {
            System.out.println(rate[0] + " " + rate[1]);
        }
    }

    private String[][] getRates() throws Exception {
        HashMap<String, NodeList> result = new HashMap<>();
        String[][] rates;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String url = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=" + dateFormat.format(date);
        Document doc = loadDocument(url);
        System.out.println(doc.getXmlVersion());

        NodeList nl = doc.getElementsByTagName("Valute");
        for (int i = 0; i < nl.getLength(); i++) {
            Node c = nl.item(i);
            NodeList nlChildes = c.getChildNodes();
            for (int j = 0; j < nlChildes.getLength(); j++) {
                if (nlChildes.item(j).getNodeName().equals("CharCode")) {
                    result.put(nlChildes.item(j).getTextContent(), nlChildes);
                }
            }
        }
        int k = 0;
        rates = new String[result.size()][2];

        for (Map.Entry<String, NodeList> entry : result.entrySet()) {
            NodeList temp = entry.getValue();
            double value = 0;
            int nominal = 0;
            for (int i = 0; i < temp.getLength(); i++) {
                if (temp.item(i).getNodeName().equals("Value")) {
                    value = Double.parseDouble(temp.item(i).getTextContent().replace(",", "."));
                } else if (temp.item(i).getNodeName().equals("Nominal")) {
                    nominal = Integer.parseInt(temp.item(i).getTextContent());
                }
            }
            double amount = value / nominal;
            rates[k][0] = entry.getKey();
            rates[k][1] = (((double) Math.round(amount * 1_000_000)) / 1_000_000) + " RUB";
            k++;
        }
        return rates;
    }

    private Document loadDocument(String url) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        return factory.newDocumentBuilder().parse(new URL(url).openStream());
    }
}