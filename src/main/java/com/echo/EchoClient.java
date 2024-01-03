package com.echo;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class EchoClient {
    public static void tcpClient1(int port, String hostName) {
        System.out.println("TCP Client Test");

        try {

            Socket echoSocket = new Socket(hostName, port);
            System.out.println("port : " + port + "\n");
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            String input;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                System.out.println("echo>" + (input = in.readLine()));
                if (input.equals("exit")) {
                    break;
                }
            }

            stdIn.close();
            in.close();
            out.close();
            echoSocket.close();

        } catch (UnknownHostException ue) {
            System.err.println("Don't know about host " + hostName);
            System.err.println(ue.getMessage());
            System.exit(1);
        } catch (IOException ie) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.err.println(ie.getMessage());
            System.exit(1);
        }
    }

    public static void tcpClient(int port, String hostName) {
        System.out.println("TCP Client Test");

        try {

            Socket echoSocket = new Socket(hostName, port);
            System.out.println("port : " + port + "\n");
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String input;
            String userInput;

            while ((userInput = stdIn.readLine()) != null) {
                byte[] stdInBuf = new byte[1024];
                stdInBuf = userInput.getBytes(StandardCharsets.UTF_8);
                int bytesRead = stdInBuf.length;

                System.out.println("BytesRead Size : " + bytesRead);
                String userInput = new String(stdInBuf, 0, bytesRead);
                System.out.println("userInput : " + userInput);
                out.println(userInput);
                System.out.println("echo>" + (input = in.readLine()));
                if (input.equals("exit")) {
                    break;
                }
            }


            in.close();
            out.close();
            echoSocket.close();

        } catch (UnknownHostException ue) {
            System.err.println("Don't know about host " + hostName);
            System.err.println(ue.getMessage());
            System.exit(1);
        } catch (IOException ie) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.err.println(ie.getMessage());
            System.exit(1);
        }
    }

    public static void addValueToArray(byte[] array, byte value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 0) {
                array[i] = value;
                break;
            }
        }
    }

    public static void udpClient(int port, String hostName) {
        System.out.println("UDP Client Test");
        System.out.println("port : " + port + "\n");

        try {
            InetAddress address = InetAddress.getByName(hostName);
            DatagramSocket socket = new DatagramSocket();

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            int sequenceNumber = 1;

            while ((userInput = stdIn.readLine()) != null) {
                byte[] buf = (sequenceNumber + ":" + userInput).getBytes();
                sequenceNumber += 1;
                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);

                socket.send(packet);

                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String echo = new String(packet.getData(), 0, packet.getLength());
                System.out.println("echo>" + echo);
                if (userInput.equals("exit")) {
                    break;
                }
            }

            stdIn.close();
            socket.close();

        } catch (IOException ie) {
            System.err.println("Couldn't get I/O");
            System.err.println(ie.getMessage());
            System.exit(1);
        }
    }
}
