package org.example;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class AttendenceHandler extends ObjectHandler {
    private String[] data;
    private String id;

    JSONObject json;

    public AttendenceHandler() {
    }

    @Override
    public void PutData(String body) throws IOException {
        json = new JSONObject(body);
        ParseData();
    }

    @Override
    public void ParseData() {
        id = json.getString("id");
    }

    public String getId() {
        return id;
    }

    @Override
    public void DatabaseOperation(DatabaseConnection dbConnection) {
        int result = dbConnection.GetAttendance(Integer.parseInt(id));
        dbConnection.ChangeAttendance(Integer.parseInt(id), result+1);
    }

    @Override
    public String getPositiveResponse() {
        if (id == null)
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