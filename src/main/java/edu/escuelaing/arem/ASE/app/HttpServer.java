package edu.escuelaing.arem.ASE.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class HttpServer {

    private static HttpServer _instance = new HttpServer();
    private static String route = "";
    private static WebService servvice = null;

    private HttpServer(){}

    public static HttpServer getInstance(){
        return _instance;
    }

    public void runServer(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
 
        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
 
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;
            
            boolean firstLine = true;
            String uriStr = "";
 
            while ((inputLine = in.readLine()) != null) {
                if(firstLine){
                    uriStr = inputLine.split(" ")[1];
                    firstLine = false;
                }
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }

            URI requestUri = new URI(uriStr);
            try {
                String path = requestUri.getPath();
                if(path.startsWith("/action")){
                    String webUri = path.replace("/action", "");
                    if (webUri.equals(route)){
                        outputLine = servvice.handle();
                    }
                } else {
                    outputLine = htttpClientHtml();
                }
            }catch (Exception e){
                e.printStackTrace();
                outputLine = httpError();
            }
            
            
            if(uriStr.startsWith("/cliente")){
                outputLine = htttpClientHtml();
            }else{
                 outputLine = httpError();
            }
            out.println(outputLine);
 
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }
    
     private static String httpError() {
        String outputLine = "HTTP/1.1 400 Not Found\r\n"
                    + "Content-Type:text/html\r\n"
                    + "\r\n"
                    + "<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "    <head>\n"
                    + "        <title>Error Not found</title>\n"
                    + "        <meta charset=\"UTF-8\">\n"
                    + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                    + "    </head>\n"
                    + "    <body>\n"
                    + "        <h1>Error</h1>\n"
                    + "    </body>\n";
        return outputLine;
                
     }
    
    public static String htttpClientHtml(){
        String outputLine = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type:text/html\r\n"
                    + "\r\n";

        Path file = Paths.get("target/classes/public/cliente.html");
        Charset charset = Charset.forName("UTF-8");
        try(BufferedReader reader = Files.newBufferedReader(file, charset)){
            String line = null;
            while((line = reader.readLine()) != null){
                System.out.println(line);
                outputLine += line;
            } 
        } catch (IOException x){
               System.err.format("IOeXCEPTION; %s%n", x); 
        }
        return outputLine;
    }

    public static void get(String r, WebService s){
        route = r;
        servvice = s;
    }
}
