package org.example;

import java.net.InetSocketAddress;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.json.JSONObject;

public class UserHandler extends ObjectHandler {
    private String[] data;
    private String id;
    private String name;
    private String email;
    private String password;

    JSONObject json;

    public UserHandler() {
    }

    @Override
    public void PutData(String body) throws IOException {
        json = new JSONObject(body);
        ParseData();
    }

    @Override
    public void ParseData() {
        name = json.getString("name");
        email = json.getString("email");
        password = json.getString("password");
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public void DatabaseOperation(DatabaseConnection dbConnection) {
        dbConnection.addUser(name, email, password);
        id = dbConnection.getId(email);
        json.put("id", id);
    }

    @Override
    public  boolean DatabaseBoolean(DatabaseConnection dbConnection) {
        return !dbConnection.isUser(email);
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