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
            boolean on = true;

            while (on) {

                int count = buffergogo(out);

                for (int i = 0; i < count; i++) {
                    System.out.println("echo>" + (input = in.readLine()));
                    if (count == 1 && input.equals("exit")) {
                        on = false;
                        break;
                    }
                }
            }


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
//
//    public static int buffergogo(PrintWriter out,int count) throws IOException {
//        count += 1;
//        byte[] stdIn = new byte[1024];
//        int inputChar = System.in.read();
//        int idx = 0;
//        while (inputChar != 10) {
//            stdIn[idx] = (byte)inputChar;
//            if (idx == 1023){
//                out.println(new String(stdIn,0,idx));
//                count = buffergogo(out,count);
//                break;
//            }
//            inputChar = System.in.read();
//            idx += 1;
//        }
//        if (idx == 1023){
//            return count;
//        }
//        if ( count <= 1 || idx != 0 ){
//            out.println(new String(stdIn,0,idx));
//        }else{
//            count-=1;
//        }
//
//        return count;
//    }

    public static int buffergogo(PrintWriter out) throws IOException {

        int count = 1;
        byte[] stdIn = new byte[1024];

        int inputChar = System.in.read();
        int idx = 0;
        while (inputChar != 10) {
            stdIn[idx] = (byte)inputChar;
            if (idx == 1023){
                out.println(new String(stdIn,0,idx));
                stdIn = new byte[1024];
                count += 1;
                idx = -1;
            }
            idx +=1;
            inputChar = System.in.read();

        }

        if (count ==1 || idx != 0) {
            out.println(new String(stdIn, 0, idx));
        }
        else{
            count-=1;
        }
        return count;

    }

    public static void tcpClient123(int port, String hostName) {
        System.out.println("TCP Client Test");

        try {

            Socket echoSocket = new Socket(hostName, port);
            System.out.println("port : " + port + "\n");
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            String input;
            String userInput;

            while (true) {
                byte[] stdInBuf = new byte[1024];
                int bytesRead = System.in.read(stdInBuf);

                System.out.println("BytesRead Size : " + bytesRead);
                userInput = new String(stdInBuf, 0, bytesRead-1);
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
