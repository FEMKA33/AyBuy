package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class GetBalanceHandler extends ObjectHandler {
    private String[] data;
    private int id;

    JSONArray jsonArray;
    JSONObject json;

    public GetBalanceHandler() {
    }

    @Override
    public void PutData(String body) throws IOException {
        json = new JSONObject(body);
        ParseData();
    }

    public void ParseData() {
        id = json.getInt("id");
    }

    @Override
    public void DatabaseOperation(DatabaseConnection dbConnection) {
        int result = DatabaseConnection.getUserBalance(id);
        json.put("balance", result);
    }

    @Override
    public String getPositiveResponse() {
        return json.toString();
    }

    @Override
    public String getNegativeResponse() {
        return "{\"message\": \"Invalid email or password\"}";
    }
}