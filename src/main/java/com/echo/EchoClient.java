package com.echo;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

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

            String input;

            while (true) {
                Queue<Byte> queue = new LinkedList<>();

                int inputChar = System.in.read();
                while (inputChar != 10) {
                    queue.offer((byte) inputChar);
                    inputChar = System.in.read();
                }

                int count = 0;
                gogoBuffer(queue, out, count);

                for (int i = 0; i < count; i++) {
                    System.out.println("echo>" + (input = in.readLine()));
                    if (input.equals("exit")) {
                        break;
                    }
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

    public static int gogoBuffer(Queue<Byte> queue, PrintWriter out, int count) {
        count++;
        byte[] buf = new byte[1024];
        int queueSize = queue.size();
        if (queueSize <= 1024) {
            for (int i = 0; i < queueSize; i++) {
                byte temp = queue.poll();
                buf[i] = temp;
            }
            String userinput = new String(buf, 0, queueSize);
            out.println(userinput);
            return count;
        } else { /*큐사이즈가 버퍼크기를 넘길때 */
            for (int i = 0; i < 1024; i++) {
                buf[i] = queue.poll();
            }
            String userinput = new String(buf, 0, 1023);
            out.println(userinput);
            gogoBuffer(queue, out, count);
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
