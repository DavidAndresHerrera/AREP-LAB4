package edu.escuelaing.arep.demo;
import edu.escuelaing.arep.miniSpring.RequestMapping;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDate;


public class HelloController {

    @RequestMapping("/Hola")
    public static String hola() {

        return "Hola david";
    }
    @RequestMapping("/Hello")
    public static String hello() {

        return "Hi david";
    }
    @RequestMapping("/Carro.jpg")
    public static  byte[] carro(){
        BufferedImage imagen;
        try{
            imagen = ImageIO.read(new File("src/main/resources/imagenes/Carro.jpg"));
            ByteArrayOutputStream ImageBytes = new ByteArrayOutputStream();
            ImageIO.write(imagen, "JPG", ImageBytes);
            return ImageBytes.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @RequestMapping("/index.html")
    public static String index(){

        return "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<meta charset=\"UTF-8\">\n"
                + "<title>Hola david, tu pagina esta en ejecucion sin errores</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "<h1>El dia de hoy  es</h1>\n"
                + "<h1>"+ LocalDate.now()+"</h1>\n"
                + "</body>\n"
                + "</html>\n";
    }

}