package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ComponentHandler extends ObjectHandler {
    private static final String UPLOAD_DIRECTORY = "X:/somethings/Java/femochka/image";

    private String[] data;
    private String name;
    private String price;
    private String image;
    private String description;
    private String date;
    private int user_id;

    JSONObject json;

    public ComponentHandler() {
    }

    @Override
    public void PutData(String body) throws IOException {
        json = new JSONObject(body);
        ParseData();
    }

    @Override
    public void ParseData() {
        System.out.println(json.toString());
        name = json.getString("name");
        price = json.getString("price");
        image = json.getString("image");
        description = json.getString("description");
        date = json.getString("selectedDate");
        user_id = json.getInt("id");
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public int getUser_id() {
        return user_id;
    }

    @Override
    public void DatabaseOperation(DatabaseConnection dbConnection) {
        dbConnection.addComponent(name, price, image, description, date, user_id);
    }

    @Override
    public String getPositiveResponse() {
        return "Пользователь успешно добавлен";
    }

    @Override
    public String getNegativeResponse() {
        return "Ошибка при добавлении пользователя в БД";
    }
}