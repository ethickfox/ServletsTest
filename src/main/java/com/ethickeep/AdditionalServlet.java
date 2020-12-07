package com.ethickeep;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/additional"})
@MultipartConfig(location = "/tmp")
public class AdditionalServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, String[]> parameterMap = req.getParameterMap();
        resp.getWriter()
                .write("AdditionalServlet\n" + extracted(req));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        for (Part part : req.getParts()) {
            if (part.getName().equals("file-name")) {
                BufferedReader br = new BufferedReader(new InputStreamReader(part.getInputStream()));
                String filename = br.lines().collect(Collectors.joining("\n"));
                log(filename);
            }else {
                part.write(UUID.randomUUID().toString() + part.getSubmittedFileName());
            }
        }
    }

    private String extracted(HttpServletRequest req) {
        return req.getParameterMap()
                .entrySet()
                .stream()
                .map((entrySet)->{
                    String params = String.join(", ", entrySet.getValue());
                    return entrySet.getKey() + " => " + params;
                }).collect(Collectors.joining("\n"));
    }
}
