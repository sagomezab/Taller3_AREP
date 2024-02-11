package edu.escuelaing.arem.ASE.app;

import java.io.IOException;
import java.net.URISyntaxException;

public class MyWebServices {

    /**
     * http://localhost:4567/arep
     * @param args
     * @throws URISyntaxException 
     * @throws IOException 
     */
    public static void main( String[] args ) throws IOException, URISyntaxException{
        HttpServer server = HttpServer.getInstance();

        server.staticFiles.location("/public");

        server.get("/hello", (req, res) -> "Hello World");

        server.get("/get-json", (req, res) -> {
            res.type("application/json");
            return "{\"name\": \"Santiago\"}";
        });

        server.get("/get-css", (req, res) -> {
            res.type("text/css");
            return "* {\n" +
                    "    font-family: sans-serif;\n" +
                    "    background-color: #f5f6fa;\n" +
                    "}";
        });

        server.post("/json-post", (req, res) -> {
            res.type("application/json");
            return "{\"name\": \"Santiago\"}";
        });



        server.run(args);

    }
}

