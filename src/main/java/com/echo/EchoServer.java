package com.echo;

import java.io.*;
import java.net.*;
import java.util.*;

public class EchoServer {

    public static void tcpServer(int port){
        System.out.println("TCP Server Test");
        System.out.println("port : " + port + "\n");

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is ready");
            System.out.println("Waiting connection\n");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connect completed\n");

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Client>" + inputLine);
                out.println(inputLine);
                if (inputLine.equals("exit")){
                    break;
                }
            }

            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();

        } catch (IOException ie){
            System.err.println("Exception caught when trying to listen on port " + port + " or listening for a connection");
            System.err.println(ie.getMessage());
        }
    }

    public static void udpServer(int port) {
        System.out.println("UDP Server Test");
        System.out.println("port : " + port + "\n");

        try{
            DatagramSocket socket = new DatagramSocket(port);
            while(true){
                byte[] buf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                socket.receive(packet);

                String dataGot = new String(packet.getData(),0, packet.getLength());
                InetAddress address = packet.getAddress();
                int clientPort = packet.getPort();
                System.out.println("Client(Port:" + clientPort + ")>" + dataGot);
                String[] parts = dataGot.split(":");

                packet = new DatagramPacket(buf, buf.length, address, clientPort);

                socket.send(packet);

                if (parts.length == 2 && parts[1].equals("exit")){
                    break;
                }
            }

            socket.close();

        } catch (IOException ie) {
            System.err.println("Couldn't get I/O");
            System.err.println(ie.getMessage());
            System.exit(1);
        }
    }

    public static void udpServer2(String arg) {
        System.out.println("UDP Server Test");
        int portNumber = Integer.parseInt(arg);
        System.out.println("port : " + portNumber);

        int expectedSequenceNum = 1;
        List<String> sequenceList = new ArrayList<>();
        Set<String> missing = new HashSet<>();

        try{
            DatagramSocket socket = new DatagramSocket(portNumber);
            while(true){
                byte[] buf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                socket.receive(packet);

                String dataGot = new String(packet.getData(),0, packet.getLength());
                InetAddress address = packet.getAddress();
                int clientPort = packet.getPort();
                System.out.println("Client(Port:" + clientPort + ")>" + dataGot);
                String[] parts = dataGot.split(" ");

                int sequenceNum = Integer.parseInt(parts[0]);
                //예상되는 번호의 데이터가 들어오지 않았는데 처음보는 데이터일 경우
                if (sequenceNum != expectedSequenceNum && !sequenceList.contains(parts[0])){
                    //처음보는 데이터가 누락된 데이터였을 경우
                    if (missing.contains(Integer.toString(sequenceNum))){
                        System.out.println("missed packet(" +sequenceNum+ ")recovered");
                        missing.remove(Integer.toString(sequenceNum));
                        sequenceList.add(Integer.toString(sequenceNum));
                        continue;
                    }
                    //누락되지 않은 처음보는 데이터일 경우
                    Set<String> sequenceSet = new HashSet<>(sequenceList);
                    sequenceSet.addAll(missing);
                    List<String> sortedList = new ArrayList<>(sequenceSet);
                    Collections.sort(sortedList);
                    //새롭게 알게된 누락된 데이터를 기록
                    for (int i=1;i<=sequenceNum;i++){
                        if (!sortedList.contains(Integer.toString(i))){
                            System.out.println("packet("+i+")missed");
                            missing.add(Integer.toString(i));
                        }
                    }

                    sequenceList.add(Integer.toString(sequenceNum));
                    expectedSequenceNum = sequenceNum+1;
                    //중복된 데이터가 들어온 경우
                } else if (sequenceNum != expectedSequenceNum && sequenceList.contains(parts[0])) {
                    System.out.println("packet(" + sequenceNum + ")repeated");
                    //예상한 데이터가 들어온 경우
                } else if (sequenceNum == expectedSequenceNum){
                    sequenceList.add(Integer.toString(sequenceNum));
                    expectedSequenceNum = sequenceNum+1;
                }

                packet = new DatagramPacket(buf, buf.length, address, clientPort);

                socket.send(packet);

                if (parts.length == 3 && parts[2].equals("exit")){
                    break;
                }
            }

            socket.close();

        } catch (IOException ie) {
            System.err.println("Couldn't get I/O");
            System.err.println(ie.getMessage());
            System.exit(1);
        }
    }
}
