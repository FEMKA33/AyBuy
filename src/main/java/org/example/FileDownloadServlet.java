package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/api/getImage")
public class FileDownloadServlet extends HttpServlet {
    private static final String IMAGE_DIR = "C:/Users/user/uploads";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleCORS(req, resp);

        // Проверка предзапроса OPTIONS
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // Получаем имя файла из параметров запроса
        String fileName = req.getParameter("fileName");
        if (fileName == null || fileName.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Имя файла не указано.");
            return;
        }

        // Путь к файлу
        File file = new File(IMAGE_DIR, fileName);

        // Проверяем существование файла
        if (!file.exists() || file.isDirectory()) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("Файл не найден: " + fileName);
            return;
        }

        // Устанавливаем заголовки ответа
        resp.setContentType(getServletContext().getMimeType(file.getName()));
        resp.setContentLengthLong(file.length());
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

        // Отправка файла в ответ
        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = resp.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleCORS(req, resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void handleCORS(HttpServletRequest req, HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000"); // Разрешённый источник
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE"); // Разрешённые методы
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization"); // Разрешённые заголовки
        resp.setHeader("Access-Control-Allow-Credentials", "true"); // Разрешить отправку сессий/куков
    }
}