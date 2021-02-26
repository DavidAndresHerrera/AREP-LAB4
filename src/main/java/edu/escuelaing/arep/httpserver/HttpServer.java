package edu.escuelaing.arep.httpserver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class HttpServer {


    private int port;
    Map<String, Processor> routesToProcessors = new HashMap();


    public void startServer(int httpPort) throws IOException {
        port = httpPort;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(getPort());
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        boolean running = true;
        while(running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;

            boolean isfirstline = true;
            String path= "";
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                if(isfirstline){
                    path = inputLine.split(" ")[1];
                    isfirstline = false;
                }
                if (!in.ready()) {
                    break;
                }
            }
            String resp=null;
            for(String key: routesToProcessors.keySet()){
                if (path.startsWith(key)){
                    String newpath = path.substring(key.length());
                    try {
                        resp = routesToProcessors.get(key).handle(newpath, null, null);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(resp == null){
                outputLine = validOkHttpResponse();
            }else{
                outputLine = resp;
            }
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    private String validOkHttpResponse() {
        String mostrar = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<meta charset=\"UTF-8\">\n"
                + "<title>Mini Spring</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "<h1>Bienvenido a tu Servidor Web</h1>\n"
                + "</body>\n"
                + "</html>\n";

        return  mostrar;
    }


    public void setPort(int port) {
        this.port = port;
    }

    public   int getPort() {
       return this.port;
    }

    public void registerProcessor(String path, Processor proc){
        routesToProcessors.put(path, proc);
    }




}