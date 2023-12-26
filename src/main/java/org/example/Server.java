package org.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Stream;

public class Server {
    private static Map<Long, SocketWrapper> clients = new HashMap<>();
    private static Long nums;
    private static long idCounter = 0;
    public static final int PORT = 8081;


    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                Socket client = server.accept();
                System.out.println(client);
                long clientId = ++idCounter;
                SocketWrapper socketWrapper = new SocketWrapper(clientId, client);
                clients.put(clientId, socketWrapper);

                new Thread(() -> {
                    try (Scanner input = socketWrapper.getInput();
                         PrintWriter output = socketWrapper.getOutput()) {
                            output.println("You joined the server! Clients of the server: " + clients);

                            while (true) {
                                String clientInput = input.nextLine();

                                if (Objects.equals("q", clientInput)) {
                                    clients.remove(clientId);
                                    clients.values().forEach(it -> it.getOutput().println("User left the chat: " + socketWrapper));
                                    break;
                                }



                                if (clientInput.indexOf("@") == 0) {
                                    try {
                                         nums = Long.valueOf(clientInput.substring(1, clientInput.indexOf(' ')));
                                        System.out.println(nums.toString());
                                        if (nums <= clients.size() && nums != null && nums > 0) {
                                            SocketWrapper destination = clients.get(Long.valueOf(nums));
                                            destination.getOutput().println(clientInput);
                                        } else {
                                            System.out.println("nope");
                                        }
                                    } catch (Exception e ) {
                                        System.out.println(e.getMessage());
                                    }

                            } else {
                                    clients.values().stream().forEach(it -> it.getOutput().println(clientInput));
                                }
                            }
                    }

                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
