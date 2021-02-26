package edu.escuelaing.arep.miniSpring;

import edu.escuelaing.arep.httpserver.HttpServer;
import edu.escuelaing.arep.httpserver.Processor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class MiniSpringBoot implements Processor {

    private static MiniSpringBoot _instance = new MiniSpringBoot();
    private Map<String, Method > requestProcessors = new HashMap();
    private MiniSpringBoot(){}
    public static MiniSpringBoot getInstance(){
        return _instance;
    }
    public void loadComponents(String[] componentsList) throws ClassNotFoundException {
        for(String s: componentsList){
            loadComponent(s);
        }
    }

    private void loadComponent(String comp) throws ClassNotFoundException {

        Class component = Class.forName(comp);
        Method[] componentMethods = component.getDeclaredMethods();
        for(Method m: componentMethods){
            if(m.isAnnotationPresent(RequestMapping.class)){
                requestProcessors.put(m.getAnnotation(RequestMapping.class).value(),m);
            }
        }
    }


    @Override
    public String handle(String path, HttpRequest req, HttpResponse res)  {
        String resp = "";
        if(requestProcessors.containsKey(path)){
            try {
               resp = requestProcessors.get(path).invoke(null,null).toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"+ resp;
    }

    public void startServer(){
        HttpServer hserver = new HttpServer();
        hserver.registerProcessor("/springapp",this);
        try {
            hserver.startServer(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {
        try {
            MiniSpringBoot.getInstance().loadComponents(args);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        MiniSpringBoot.getInstance().startServer();
    }
    public String validOkHeader(){
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n";
    }
}
