package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class GetComponentByIdHandler extends ObjectHandler {
    private String[] data;
    private int id;

    JSONArray jsonArray;
    JSONObject json;

    public GetComponentByIdHandler() {
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
        jsonArray = new JSONArray();
        ArrayList<String> result = new ArrayList<String>();
        result = DatabaseConnection.getComponentById(id);
        json.put("name", result.get(0));
        json.put("price", result.get(1));
        json.put("image", result.get(2));
        json.put("description", result.get(3));
        json.put("date", result.get(4));
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