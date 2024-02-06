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
        HttpServer.get("/arep", () -> {
            String resp = "HTTP/1.1 200 Ok\r\n"
            + "Content-Type:text/html\r\n"
            + "\r\n"
            + "Hello AREP!";
            return resp;
        });

        HttpServer.getInstance().runServer(args);
    }
}
