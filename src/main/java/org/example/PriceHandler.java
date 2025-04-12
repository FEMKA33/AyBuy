package org.example;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class PriceHandler extends ObjectHandler {
    private String[] data;
    private String id;
    private String price;
    private String user_id;

    JSONObject json;

    public PriceHandler() {
    }

    @Override
    public void PutData(String body) throws IOException {
        json = new JSONObject(body);
        ParseData();
    }

    @Override
    public void ParseData() {
        id = json.getString("id");
        price = json.getString("price");
        user_id = json.getString("user_id");
    }

    public String getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    @Override
    public void DatabaseOperation(DatabaseConnection dbConnection) {
        if (!Objects.equals(price, ""))
        {
            dbConnection.ChangePrice(Integer.parseInt(id), price, Integer.parseInt(user_id));
        }
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