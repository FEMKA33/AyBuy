package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class BalanceHandler extends ObjectHandler {
    private String[] data;
    private int id;
    private int balance;

    JSONArray jsonArray;
    JSONObject json;
    
    public BalanceHandler() {
    }

    @Override
    public void PutData(String body) throws IOException {
        json = new JSONObject(body);
        ParseData();
    }

    public void ParseData() {
        id = json.getInt("id");
        balance = json.getInt("balance");
    }

    @Override
    public void DatabaseOperation(DatabaseConnection dbConnection) {
        int result = DatabaseConnection.getUserBalance(id) + balance;
        dbConnection.UpdateBalance(id, result);
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