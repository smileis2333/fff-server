package org.example.fff.server.servlet;

import org.example.fff.server.util.ResponseHeaderWriter;
import org.example.fff.server.util.ResponseWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class Response implements HttpServletResponse {
    private Socket outcome;
    private String protocol;
    private int status = HttpStatus.OK;
    private String msg = "OK";
    private Map<String, String> headers = new HashMap<>();
    private ResponseWriter writer;
    private ResponseHeaderWriter headerWriter;
    private Map<String, Cookie> cookies = new HashMap<>();

    public ResponseHeaderWriter getHeaderWriter() {
        return headerWriter;
    }

    public void setHeaderWriter(ResponseHeaderWriter headerWriter) {
        this.headerWriter = headerWriter;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Socket getOutcome() {
        return outcome;
    }

    public void setOutcome(Socket outcome) {
        this.outcome = outcome;
        try {
            this.writer = new ResponseWriter(outcome.getOutputStream(), this);
            this.headerWriter = new ResponseHeaderWriter(outcome.getOutputStream(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addCookie(Cookie cookie) {
        cookies.put(cookie.getName(), cookie);
    }

    @Override
    public boolean containsHeader(String name) {
        return false;
    }

    @Override
    public String encodeURL(String url) {
        return null;
    }

    @Override
    public String encodeRedirectURL(String url) {
        return null;
    }

    @Override
    public String encodeUrl(String url) {
        return null;
    }

    @Override
    public String encodeRedirectUrl(String url) {
        return null;
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        this.status = sc;
        this.msg = msg != null ? msg : HttpStatus.getMessage(sc);
        ResponseMeta metainfo = newMetaInfo();
        headerWriter.println(metainfo);
        headerWriter.flush();
    }

    @Override
    public void sendError(int sc) throws IOException {
        sendError(sc, null);
    }

    @Override
    public void sendRedirect(String location) throws IOException {

    }

    @Override
    public void setDateHeader(String name, long date) {

    }

    @Override
    public void addDateHeader(String name, long date) {

    }

    @Override
    public void setHeader(String name, String value) {

    }

    @Override
    public void addHeader(String name, String value) {

    }

    @Override
    public void setIntHeader(String name, int value) {

    }

    @Override
    public void addIntHeader(String name, int value) {

    }

    @Override
    public void setStatus(int sc) {
        // todo check valid
        status = sc;
    }

    @Override
    public void setStatus(int sc, String sm) {

    }

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public String getHeader(String name) {
        return null;
    }

    @Override
    public Collection<String> getHeaders(String name) {
        return null;
    }

    @Override
    public Collection<String> getHeaderNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return writer;
    }

    @Override
    public void setCharacterEncoding(String charset) {

    }

    @Override
    public void setContentLength(int len) {

    }

    @Override
    public void setContentLengthLong(long len) {

    }

    @Override
    public void setContentType(String type) {

    }

    @Override
    public void setBufferSize(int size) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale loc) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    public ResponseMeta newMetaInfo() {
        HashMap<String, String> outputHeaders = new HashMap<>(headers);
        cookies.forEach((k, v) -> {
            //todo cookie属性
            outputHeaders.put("Set-Cookie", String.format("%s=%s", k, v.getValue()));
        });
        return newMetaInfo(protocol, status, msg, outputHeaders);
    }

    private ResponseMeta newMetaInfo(String protocol, int status, String msg, Map<String, String> headers) {
        return new ResponseMeta(protocol, status, msg, headers);
    }

    public static class ResponseMeta {
        private int status;
        private Map<String, String> headers = new HashMap<>();
        private String message;
        private String protocol;

        public ResponseMeta(String protocol, int status, String message, Map<String, String> headers) {
            this.status = status;
            this.headers = headers;
            this.message = message;
            this.protocol = protocol;
        }

        @Override
        public String toString() {
            String firstLine = String.format("%s %s %s\n", protocol, status, message);
            Set<Map.Entry<String, String>> entries = headers.entrySet();

            String headerLines = headers.entrySet().stream().map(entry -> String.format("%s: %s", entry.getKey(), entry.getValue())).collect(Collectors.joining("\n"));

            return firstLine + headerLines + "\n";
        }
    }
}

