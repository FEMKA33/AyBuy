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

public class LoginHandler extends ObjectHandler {
    private String[] data;
    private String id;
    private String email;
    private String password;

    JSONObject json;

    LoginHandler() {
    }

    @Override
    public void PutData(String body) throws IOException {
        json = new JSONObject(body);
        ParseData();
    }

    @Override
    public void ParseData() {
        email = json.getString("email");
        password = json.getString("password");
    }

    public String getId() {
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
        id = dbConnection.getId(email);
        json.put("id", id);
    }

    @Override
    public boolean DatabaseBoolean(DatabaseConnection dbConnection) {
        return dbConnection.isLogin(email, password);
    }

    @Override
    public String getPositiveResponse() {
        return json.toString();
    }

    @Override
    public String getNegativeResponse() {
        json.put("error", "Ошибка авторизации");
        return json.toString();
    }
}