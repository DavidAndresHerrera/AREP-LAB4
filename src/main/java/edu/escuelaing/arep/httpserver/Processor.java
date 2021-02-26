package edu.escuelaing.arep.httpserver;

import java.lang.reflect.InvocationTargetException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public interface Processor {
    public String handle(String path, HttpRequest req, HttpResponse res) throws InvocationTargetException, IllegalAccessException;
}
