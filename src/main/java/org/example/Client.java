package org.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Client {
    public static String myMsg;

    public static void main(String[] args) throws IOException {
        final Socket client = new Socket("localhost", Server.PORT);
        final Scanner input = new Scanner(client.getInputStream());
        final PrintWriter output = new PrintWriter(client.getOutputStream(), true);

        String serverClientMsg = input.nextLine();
        System.out.println(serverClientMsg);


        new Thread(() -> {
            while (true) {
                try {
                    String inputLine = input.nextLine();
                    System.out.println(inputLine);
                } catch (Exception e) {

                }
            }
        }).start();

                new Thread(() -> {
                    Scanner consoleScanner = new Scanner(System.in);
                    while (true) {
                        myMsg = consoleScanner.nextLine();
                        output.println(myMsg);
                        if (Objects.equals("q", myMsg)) {
                            try {
                                client.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        }
                    }
                }).start();
    }

}
