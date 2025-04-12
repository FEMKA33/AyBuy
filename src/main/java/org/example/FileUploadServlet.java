package org.example;

import jakarta.servlet.MultipartConfigElement;
import org.apache.commons.io.IOUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@MultipartConfig(
        fileSizeThreshold = 50 * 1024 * 1024, // 1MB
        maxFileSize = 50 * 1024 * 1024,  // 10MB
        maxRequestSize = 50 * 1024 * 1024 // 50MB
)
public class FileUploadServlet extends HttpServlet {

    private static final MultipartConfigElement MULTI_PART_CONFIG = new MultipartConfigElement("c:/Users/user/uploads");

    private static final String UPLOAD_DIR = System.getProperty("user.home") + File.separator + "uploads";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Начало обработки POST-запроса");

        System.out.println("Установка CORS-заголовков");
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        resp.setHeader("Access-Control-Allow-Credentials", "true");

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            System.out.println("Директория загрузки не существует. Создаём...");
            if (!uploadDir.mkdirs()) {
                System.out.println("Не удалось создать директорию: " + UPLOAD_DIR);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Не удалось создать директорию для загрузки файлов.");
                return;
            }
        } else {
            System.out.println("Директория загрузки существует: " + UPLOAD_DIR);
        }

        if (!req.getContentType().startsWith("multipart/form-data")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Неверный тип контента. Ожидается multipart/form-data.");
            return;
        }

        try {
            System.out.println("Получение файла из запроса");
            Part filePart = req.getPart("image");
            System.out.println("Файл получен");
            if (filePart == null) {
                System.out.println("Файл не передан в запросе!");
                throw new ServletException("Файл не передан в запросе!");
            }

            String contentType = filePart.getContentType();
            if (!contentType.startsWith("image/")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Поддерживаются только изображения.");
                return;
            }

            String fileName = filePart.getSubmittedFileName();
            System.out.println("Имя полученного файла: " + fileName);
            if (fileName == null || fileName.isEmpty()) {
                System.out.println("Имя файла отсутствует или некорректно!");
                throw new ServletException("Имя файла отсутствует или некорректно!");
            }

            File file = new File(uploadDir, fileName);
            System.out.println("Сохранение файла: " + file.getAbsolutePath());
            try (FileOutputStream fos = new FileOutputStream(file)) {
                IOUtils.copy(filePart.getInputStream(), fos);
            }

            System.out.println("Файл сохранён: " + file.getAbsolutePath());
            resp.getWriter().write("Файл успешно загружен: " + file.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Ошибка при обработке файла");
            System.err.println("Ошибка при получении части 'image': " + e.getMessage());
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Ошибка на сервере: " + e.getMessage());
        }

        System.out.println("Завершение обработки POST-запроса");
    }
}