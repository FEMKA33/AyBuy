package org.example;

import jakarta.servlet.*;

public class FileUploadApplication implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        // Регистрация сервлета
        ServletRegistration.Dynamic servlet = servletContext.addServlet("FileUploadServlet", new FileUploadServlet());
        servlet.addMapping("/upload"); // URL для обработки
        servlet.setMultipartConfig(new MultipartConfigElement(
                System.getProperty("java.io.tmpdir"),
                50 * 1024 * 1024,
                50 * 1024 * 1024,
                50 * 1024 * 1024
        ));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Очистка ресурсов, если необходимо
    }
}