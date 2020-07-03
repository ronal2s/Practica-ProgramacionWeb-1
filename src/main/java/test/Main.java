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

    private static void postURL(Document doc, String url){
        Elements postForms = doc.select("form[method=\"POST\"]");
        String newstr = url;
        if (null != url && url.length() > 0 )
        {
            int endIndex = url.lastIndexOf("/");
            if (endIndex != -1 || endIndex != 6 || endIndex != 7)
            {
                newstr = url.substring(0, endIndex);
            }
        }

        for (Element el:postForms) {
            try {
                Document postReq = Jsoup.connect(newstr + el.attr("action")).header("matricula", "20160138")
                        .data("asignatura","practica1").post();
                System.out.println("Respuesta: ");
                System.out.println(postReq.html());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
