package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class GetHistoryHandler extends ObjectHandler {
    private String[] data;
    private int id;

    JSONArray jsonArray;
    JSONObject json;

    public GetHistoryHandler() {
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
        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
        result = DatabaseConnection.getHistory(id);
        var mas1 = result.get(0);
        var mas2 = result.get(1);
        for (int i = 0; i < mas1.size(); i=i+1) {
            JSONObject json = new JSONObject();
            json.put("id", mas1.get(i));
            json.put("price", mas2.get(i));
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