package org.example;

import java.io.IOException;

import java.util.ArrayList;

import org.json.JSONObject;

public class DataUserHandler extends ObjectHandler {
    private String[] data;
    private int id;
    private String email;
    private String password;

    JSONObject json;

    DataUserHandler() {
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

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public void DatabaseOperation(DatabaseConnection dbConnection) {
        ArrayList<String> result = new ArrayList<String>();
        result = DatabaseConnection.getUserInfo(id);
        json.put("name", result.get(0));
        json.put("email", result.get(1));
    }

    @Override
    public boolean DatabaseBoolean(DatabaseConnection dbConnection) {
        return true;
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
