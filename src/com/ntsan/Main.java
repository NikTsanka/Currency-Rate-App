package com.ntsan;

public class Main {

    public static void main(String[] args) throws Exception {

        NBGJsonParser nbgJsonParser = new NBGJsonParser();
        nbgJsonParser.startApp();

        XMLParser xmlParser = new XMLParser();
        xmlParser.startApp();
    }
}
