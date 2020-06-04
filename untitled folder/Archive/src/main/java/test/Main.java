package test;
import java.io.IOException;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        Document doc = null;
//        String url = "https://campusvirtual.pucmm.edu.do/";
        System.out.println("Escribir URL:");
        String url = teclado.nextLine();
        try {
            doc = Jsoup.connect(url).get();
            String title = doc.title();
            System.out.println("Título de la página: " + title);
            System.out.println("Cantidad de líneas: " + countLines(doc));
            System.out.println("Cantidad de párrafos: " + countP(doc));
            System.out.println("Cantidad de imagenes dentro de P: " + countImgsInsideOfP(doc));
            System.out.println("Cantidad de GETS y POSTS sumados: " + countPostsGets(doc));
            showInputsForm(doc);
            postURL(doc, url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int countLines(Document doc) {
        return doc.html().split(System.getProperty("line.separator")).length;
    }

    private static int countP(Document doc) {
        return doc.select("p").size();
    }

    private static int countImgsInsideOfP(Document doc) {
        return doc.select("p img").size();
    }

    private static int countPostsGets(Document doc) {
        int gets = doc.select("form[method=GET]").size();
        int posts = doc.select("form[method=POST]").size();
        return posts;
    }

    private static void showInputsForm(Document doc) {
        Elements inputs = doc.select("form input");
        for(Element element : inputs) {
            System.out.println("Input placeholder: " + element.attr("placeholder") + " y Tipo: " + element.attr("type"));
        }
    }

    private static void postURL(Document doc, String url) {
        String urlFixed = "https://"+url.split("/")[2]+'/';
        Elements posts = doc.select("form[method=POST]");
        System.out.println("Haciendo POST a esta URL: " + urlFixed);
        for(Element post: posts) {
            try {
                //Usando la URL exactamente como se introdujo
//                Document post_request = Jsoup.connect(post.attr("action")).header("matricula", "20160207").data("asignatura", "practica1").post();
                //Usando la URL limpia
                Document post_request = Jsoup.connect(urlFixed).header("matricula", "20160207").data("asignatura", "practica1").post();
                System.out.println("Respuesta: " + post_request.html());
            } catch(IOException e) {
                System.out.println("Error ejecutando el POST: " + e.toString());
            }
        }
    }
}
