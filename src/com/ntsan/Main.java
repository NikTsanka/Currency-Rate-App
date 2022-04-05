package com.ntsan;

public class Main {

    public static void main(String[] args) throws Exception {

        NBGJsonParser nbgJsonParser = new NBGJsonParser();
        nbgJsonParser.startApp();

        CBRXMLParser cbrxmlParser = new CBRXMLParser();
        cbrxmlParser.startApp();
    }
}
