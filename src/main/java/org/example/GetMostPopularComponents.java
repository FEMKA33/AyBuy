package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class GetMostPopularComponents extends ObjectHandler {
    private String[] data;
    private int id;

    JSONArray jsonArray;
    JSONObject json;
    public GetMostPopularComponents() {
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
        result = DatabaseConnection.getTheMostPopularComponent(id);
        for (int i = 0; i < result.size(); i=i+6) {
            JSONObject json = new JSONObject();
            json.put("id", result.get(i));
            json.put("name", result.get(i+1));
            json.put("price", result.get(i+2));
            json.put("image", result.get(i+3));
            json.put("description", result.get(i+4));
            json.put("attendance", result.get(i+5));
            jsonArray.put(json);
        }
    }

    @Override
    public String getPositiveResponse() {
        return jsonArray.toString();
    }

    @Override
    public String getNegativeResponse() {
        return "{\"message\": \"Invalid email or password\"}";
    }
}