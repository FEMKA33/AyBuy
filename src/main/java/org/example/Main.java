package org.example;

import java.io.IOException;
import java.net.InetSocketAddress;

import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;
import jakarta.servlet.MultipartConfigElement;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main {
    public static void main(String[] args) throws Exception {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8082), 0);
        httpServer.createContext("/api/users", new RequestBody(new UserHandler()));
        httpServer.createContext("/api/login", new RequestBody(new LoginHandler()));
        httpServer.createContext("/api/userinfo", new RequestBody(new DataUserHandler()));
        httpServer.createContext("/api/newComponent", new RequestBody(new ComponentHandler()));
        httpServer.createContext("/api/getComponents", new RequestBody(new GetComponentsHandler()));
        httpServer.createContext("/api/getComponentById", new RequestBody(new GetComponentByIdHandler()));
        httpServer.createContext("/api/getComponentsById", new RequestBody(new GetComponentsByIdHandler()));
        httpServer.createContext("/api/Price", new RequestBody(new PriceHandler()));
        httpServer.createContext("/api/History", new RequestBody(new HistoryHandler()));
        httpServer.createContext("/api/getHistory", new RequestBody(new GetHistoryHandler()));
        httpServer.createContext("/api/Balance", new RequestBody(new BalanceHandler()));
        httpServer.createContext("/api/getBalance", new RequestBody(new GetBalanceHandler()));
        httpServer.createContext("/api/deleteComponent", new RequestBody(new DeleteComponentHandler()));
        httpServer.createContext("/api/attendance", new RequestBody(new AttendenceHandler()));
        httpServer.createContext("/api/getTheMostPopularComponents", new RequestBody(new GetMostPopularComponents()));
        httpServer.createContext("/api/getComponentsByIdUser", new RequestBody(new GetComponentsByIdUserHandler()));
        httpServer.createContext("/api/getComponentsByIdAuctionHandler", new RequestBody(new GetComponentsByIdAuctionHandler()));
        httpServer.setExecutor(Executors.newFixedThreadPool(16));
        httpServer.start();
        System.out.println("Сервер запущен на порту 8082");

        Server jettyServer = new Server(8081);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        jettyServer.setHandler(context);

        context.addEventListener(new FileUploadApplication());
        context.addServlet(FileUploadServlet.class, "/api/newImage")
                .getRegistration().setMultipartConfig(
                        new MultipartConfigElement("C:/Users/user/uploads")
                );

        ServletHolder downloadHolder = new ServletHolder(new FileDownloadServlet());
        context.addServlet(downloadHolder, "/api/getImage");

        jettyServer.start();
        System.out.println("Jetty-сервер запущен на порту 8081");

        jettyServer.join();
    }
}