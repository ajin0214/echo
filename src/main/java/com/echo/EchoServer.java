package com.echo;

import java.io.*;
import java.net.*;
import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class EchoServer {

    public static void tcpServer1(int port) {
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
                if (inputLine.equals("exit")) {
                    break;
                }
            }

            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();

        } catch (IOException ie) {
            System.err.println("Exception caught when trying to listen on port " + port + " or listening for a connection");
            System.err.println(ie.getMessage());
        }
    }


    //팔다리가 두세개
    public static void tcpServer_12(int port) {
        System.out.println("TCP Server Test");
        System.out.println("port : " + port + "\n");

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is ready");
            System.out.println("Waiting connection\n");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connect completed\n");

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);


            while (true) {

                int count = 1;
                byte[] inBuf = new byte[1024];

                int inChar = clientSocket.getInputStream().read();
                int idx = 0;

                while (inChar != 10) {
                    inBuf[idx] = (byte) inChar;
                    if (idx == 1023) {
                        System.out.println("Client>" + new String(inBuf, 0, idx));
                        out.println(new String(inBuf, 0, idx));
                        inBuf = new byte[1024];
                        idx = -1;
                        count += 1;
                    }
                    idx += 1;
                    inChar = clientSocket.getInputStream().read();
                }
                if (idx != 0 || count == 1) {
                    System.out.println("Client>" + new String(inBuf, 0, idx));
                    out.println(new String(inBuf, 0, idx));
                }
                if (new String(inBuf, 0, idx).equals("exit") && count == 1) {
                    break;
                }
            }

            out.close();
            clientSocket.close();
            serverSocket.close();

        } catch (IOException ie) {
            System.err.println("Exception caught when trying to listen on port " + port + " or listening for a connection");
            System.err.println(ie.getMessage());
        }
    }

    public static void tcpServerbuffer(int port) {
        System.out.println("TCP Server Test");
        System.out.println("port : " + port + "\n");

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is ready");
            System.out.println("Waiting connection\n");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connect completed\n");
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            int bufferSize = 4;

            while (true) {
                byte[] buf;
                int bufsize;
                String in = "";
                do {
                    buf = new byte[bufferSize];
                    bufsize = clientSocket.getInputStream().read(buf);
                    in += new String(buf, 0, bufsize);
                } while (buf[bufsize - 1] != 10);
                in = in.substring(0, in.length() - 1);
                System.out.println("Client>" + in);
                out.println(in);
                if (in.equals("exit")) {
                    break;
                }
            }

            out.close();
            clientSocket.close();
            serverSocket.close();

        } catch (IOException ie) {
            System.err.println("Exception caught when trying to listen on port " + port + " or listening for a connection");
            System.err.println(ie.getMessage());
        }
    }

    public static void tcpServer(int port) {
        System.out.println("TCP Server Test");
        System.out.println("port : " + port + "\n");

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is ready");
            System.out.println("Waiting connection\n");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connect completed\n");
            OutputStream out = clientSocket.getOutputStream();

            int bufferSize = 15;

            while (true) {
                //데이터받기~~
                String in = "";
                //버퍼로 뜨기
                byte[] buf = new byte[bufferSize];
                int bufLen = clientSocket.getInputStream().read(buf);
                //헤더사이즈 확인하기
                int headerSize = ((buf[0] & 0xFF) << 24) | ((buf[1] & 0xFF) << 16) | ((buf[2] & 0xFF) << 8) | (buf[3] & 0xFF);
                //타입 확인하기
                int bufType = buf[5];
                //페이로드 길이 확인
                int payloadSize = ((buf[6] & 0xFF) << 24) | ((buf[7] & 0xFF) << 16) | ((buf[8] & 0xFF) << 8) | (buf[9] & 0xFF);
                //페이로드 확인하기
                in += new String(buf, headerSize, min(bufLen - headerSize, payloadSize));
                System.out.println(in);
                //첫 버퍼에 다 못담은 페이로드 확인하기
                for (int i = 1; i < (headerSize + payloadSize) / (float) bufferSize; i++) {
                    buf = new byte[bufferSize];
                    bufLen = clientSocket.getInputStream().read(buf);
                    in += new String(buf, 0, bufLen);
                }
                //페이로드 타입 지정하기
                Object iin;
                if (bufType == 0) {
                    iin = Integer.parseInt(in);
                } else {
                    iin = in;
                }

                System.out.println("Client>" + iin);

        //에코보내기
            //헤더붙이기
                //데이터타입 스트링으로 변환
                if (bufType == 0) {
                    iin = String.valueOf(iin);
                }
                //데이터길이 구하기
                payloadSize = iin.toString().length();
                //바이트배열 선언
                byte[] dataToecho = new byte[10 + payloadSize];
                //헤더사이즈 담기
                headerSize = 10;
                dataToecho[0] = (byte) ((headerSize >> 24) & 0xFF);
                dataToecho[1] = (byte) ((headerSize >> 16) & 0xFF);
                dataToecho[2] = (byte) ((headerSize >> 8) & 0xFF);
                dataToecho[3] = (byte) (headerSize & 0xFF);
                //데이터타입 담기
                dataToecho[4] = (byte) 0;
                dataToecho[5] = (byte) bufType;
                //페이로드 길이 담기
                dataToecho[6] = (byte) ((payloadSize >> 24) & 0xFF);
                dataToecho[7] = (byte) ((payloadSize >> 16) & 0xFF);
                dataToecho[8] = (byte) ((payloadSize >> 8) & 0xFF);
                dataToecho[9] = (byte) (payloadSize & 0xFF);
                //페이로드 담기
                System.arraycopy(iin.toString().getBytes(),0,dataToecho, headerSize, payloadSize);

                System.out.println(Arrays.toString(dataToecho));

                //에코보내기
                out.write(dataToecho);

                //종료조건
                if (iin.equals("exit")) {
                    break;
                }
            }

            out.close();
            clientSocket.close();
            serverSocket.close();

        } catch (IOException ie) {
            System.err.println("Exception caught when trying to listen on port " + port + " or listening for a connection");
            System.err.println(ie.getMessage());
        }
    }

    public static void udpServer(int port) {
        System.out.println("UDP Server Test");
        System.out.println("port : " + port + "\n");

        try {
            DatagramSocket socket = new DatagramSocket(port);
            while (true) {
                byte[] buf = new byte[4];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                socket.receive(packet);

                String dataGot = new String(packet.getData(), 0, packet.getLength());
                InetAddress address = packet.getAddress();
                int clientPort = packet.getPort();
                System.out.println("Client(Port:" + clientPort + ")>" + dataGot);
//
//                byte[] test = new byte[buf.length*2];
//                System.arraycopy(buf,0,test,0,buf.length);
//                System.arraycopy(buf,0,test,buf.length,buf.length);

                packet = new DatagramPacket(buf, buf.length, address, clientPort);
//                System.out.println("out>" + new String(buf));
                socket.send(packet);

                if (dataGot.equals("exit")) {
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


    public static void udpServer12(int port) {
        System.out.println("UDP Server Test");
        System.out.println("port : " + port + "\n");

        try {
            DatagramSocket socket;
            socket = new DatagramSocket(port);
            int clientBufferSize = 8;

            while (true) {
                //서버로부터 패킷을 받을 준비
                String input = "";
                DatagramPacket packet;
                byte[] buf;
                InetAddress address;
                int clientPort;
                //패킷을 받아서 문자열로 합쳐서 저장하기
                do {
                    buf = new byte[8];
                    packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    address = packet.getAddress();
                    clientPort = packet.getPort();
                    input += new String(packet.getData(), 0, packet.getLength());
//                    System.out.println(new String(packet.getData(), 0, packet.getLength()));
                } while ((packet.getData()[packet.getLength() - 1]) != 10);
                //받은 문자열 출력
                System.out.println("Client>" + input.substring(0, input.length() - 1));

                //받은 문자열을 다시 보내기 위해 쪼개기
                byte[] byteInput = input.getBytes();
                int start = 0;
                while (start < byteInput.length) {
                    int end = min(byteInput.length, start + clientBufferSize);
                    byte[] chunk = new byte[end - start];
                    System.arraycopy(byteInput, start, chunk, 0, chunk.length);
                    //쪼갠 바이트배열 패킷으로 옮기기
                    packet = new DatagramPacket(chunk, chunk.length, address, clientPort);
                    socket.send(packet);
                    start += clientBufferSize;
//                    System.out.println(Arrays.toString(chunk));
                }
                //종료조건
                if (input.substring(0, input.length() - 1).equals("exit")) {
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

        try {
            DatagramSocket socket = new DatagramSocket(portNumber);
            while (true) {
                byte[] buf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                socket.receive(packet);

                String dataGot = new String(packet.getData(), 0, packet.getLength());
                InetAddress address = packet.getAddress();
                int clientPort = packet.getPort();
                System.out.println("Client(Port:" + clientPort + ")>" + dataGot);
                String[] parts = dataGot.split(" ");

                int sequenceNum = Integer.parseInt(parts[0]);
                //예상되는 번호의 데이터가 들어오지 않았는데 처음보는 데이터일 경우
                if (sequenceNum != expectedSequenceNum && !sequenceList.contains(parts[0])) {
                    //처음보는 데이터가 누락된 데이터였을 경우
                    if (missing.contains(Integer.toString(sequenceNum))) {
                        System.out.println("missed packet(" + sequenceNum + ")recovered");
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
                    for (int i = 1; i <= sequenceNum; i++) {
                        if (!sortedList.contains(Integer.toString(i))) {
                            System.out.println("packet(" + i + ")missed");
                            missing.add(Integer.toString(i));
                        }
                    }

                    sequenceList.add(Integer.toString(sequenceNum));
                    expectedSequenceNum = sequenceNum + 1;
                    //중복된 데이터가 들어온 경우
                } else if (sequenceNum != expectedSequenceNum && sequenceList.contains(parts[0])) {
                    System.out.println("packet(" + sequenceNum + ")repeated");
                    //예상한 데이터가 들어온 경우
                } else if (sequenceNum == expectedSequenceNum) {
                    sequenceList.add(Integer.toString(sequenceNum));
                    expectedSequenceNum = sequenceNum + 1;
                }

                packet = new DatagramPacket(buf, buf.length, address, clientPort);

                socket.send(packet);

                if (parts.length == 3 && parts[2].equals("exit")) {
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
