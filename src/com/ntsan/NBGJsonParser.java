package com.ntsan;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

class NBGJsonParser {

    private static final String URL_KA = "https://nbg.gov.ge/gw/api/ct/monetarypolicy/currencies/ka/json/?date=";
    private static final String URL_EN = "https://nbg.gov.ge/gw/api/ct/monetarypolicy/currencies/en/json/?date=";

    public void startApp() {
        getRates();
    }

    private void getRates() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = new Date();
        String sURL = URL_EN + dateFormat.format(date);

        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray;
        String inputLine;

        String code;
        String name;
        double rate;
        double diff;
        long quantity;

        try {
            URL url = new URL(sURL);
            URLConnection connection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while ((inputLine = bufferedReader.readLine()) != null) {
                jsonArray = (JSONArray) jsonParser.parse(inputLine);

                for (Object obj : jsonArray) {
                    JSONObject jsonObj = (JSONObject) obj;
                    JSONArray currencies = (JSONArray) jsonObj.get("currencies");
                    for (Object value : currencies) {
                        JSONObject jObj = (JSONObject) value;
                        code = (String) jObj.get("code");
                        quantity = (long) jObj.get("quantity");
                        name = (String) jObj.get("name");
                        rate = (double) jObj.get("rate");
                        diff = (double) jObj.get("diff");
                        System.out.println(
                                "(" + code + ") " +
                                        quantity + " - " +
                                        name + " " +
                                        rate + " GEL." +
                                        " Different: " + roundTo(diff, 3)
                        );
                    }
                }
            }
            bufferedReader.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private Double roundTo(double value, int digits) {
        double d = Math.pow(10, digits);
        return Math.round(value * d) / d;
    }
}