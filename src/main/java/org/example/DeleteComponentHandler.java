package org.example;

import org.json.JSONObject;

import java.io.IOException;

public class DeleteComponentHandler extends ObjectHandler {
    private String[] data;
    private int id;

    JSONObject json;

    public DeleteComponentHandler() {
    }

    @Override
    public void PutData(String body) throws IOException {
        json = new JSONObject(body);
        ParseData();
    }

    @Override
    public void ParseData() {
        id = json.getInt("id");
    }

    public int getId() {
        return id;
    }

    @Override
    public void DatabaseOperation(DatabaseConnection dbConnection) {
        dbConnection.deleteComponent(id);
        json.put("id", id);
    }

    @Override
    public String getPositiveResponse() {
        if (id == 0)
        {
            json.put("error", "Ошибка авторизации");
        }
        return json.toString();
    }

    @Override
    public String getNegativeResponse() {
        json.put("error", "Ошибка авторизации");
        return json.toString();
    }
}