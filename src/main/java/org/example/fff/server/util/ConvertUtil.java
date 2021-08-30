package org.example.fff.server.util;

import org.example.fff.server.Method;
import org.example.fff.server.servlet.Request;
import org.example.fff.server.servlet.Response;

import java.io.*;
import java.net.Socket;

public class ConvertUtil {
    public final static String EOF = "";
    public final static String FORM_DATA_HEADER = "multipart/form-data";

    public static Request toRequest(Socket income) throws IOException {
        Request request = new Request();
        request.setIncome(income);

        try {

            final String boundary = null;

            InputStream inputStream = income.getInputStream();
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            boolean firstLine = true;
            while ((line = bufferReader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    String[] eles = line.split(" ");
                    String method = eles[0];
                    String servletPath = eles[1];
                    String protocol = eles[2];
                    request.setServletPath(servletPath);
                    request.setMethod(method);
                    request.setProtocol(protocol);
                } else {
                    if (EOF.equals(line)) {
                        if (Method.POST.equals(request.getMethod())) {
                            String contentType = request.getHeaders("Content-Type").nextElement();
                            if (contentType.startsWith(FORM_DATA_HEADER)) {
                                String[] split = contentType.split("; ");
                                if (split[1].startsWith("boundary")) {
                                    break;
                                }
                            }
                        }
                        break;
                    } else {
                        String[] hs = line.split(": ");
                        request.setHeader(hs[0], hs[1]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            income.close();
        }
        return request;
    }

    public static Response toResponse(Socket income) {
        Response response = new Response();
        response.setOutcome(income);
        response.setProtocol("HTTP/1.1");
        return response;
    }
}
