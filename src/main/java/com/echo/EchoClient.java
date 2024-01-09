package com.echo;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EchoClient {
    public static void tcpClient12341(int port, String hostName) {
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

    public static void tcpClient2(int port, String hostName) {
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

    //괴물코드
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
            stdIn[idx] = (byte) inputChar;
            if (idx == 1023) {
                out.println(new String(stdIn, 0, idx));
                stdIn = new byte[1024];
                count += 1;
                idx = -1;
            }
            idx += 1;
            inputChar = System.in.read();

        }

        if (count == 1 || idx != 0) {
            out.println(new String(stdIn, 0, idx));
        } else {
            count -= 1;
        }
        return count;

    }


    public static void tcpClientbuffer(int port, String hostName) {
        System.out.println("TCP Client Test");
        try {
            Socket echoSocket = new Socket(hostName, port);
            System.out.println("port : " + port + "\n");
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);

            int stdBufferSize = 8;
            int bufferSize = 8;

            while (true) {
                byte[] stdBuf;
                int stdSize;
                String userInput = "";
                do {
                    stdBuf = new byte[stdBufferSize];
                    stdSize = System.in.read(stdBuf);
                    userInput += new String(stdBuf, 0, stdSize);
                } while (stdBuf[stdSize - 1] != 10);
                userInput = userInput.substring(0, userInput.length() - 1);
                out.println(userInput);

                byte[] buf;
                int bufsize;
                String echo = "";
                do {
                    buf = new byte[bufferSize];
                    bufsize = echoSocket.getInputStream().read(buf);
                    echo += new String(buf, 0, bufsize);
                } while (buf[bufsize - 1] != 10);
                echo = echo.substring(0, echo.length() - 1);
                System.out.println("echo>" + echo);
                if (echo.equals("exit")) {
                    break;
                }
            }

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
            int headerSize = 2;
            int payloadSize;
            int stdBufferSize = 8;
            int bufferSize = 8;

            while (true) {

                //////유저입력받기//////
                byte[] stdBuf;
                int stdSize;
                String userInput = "";
                do {
                    stdBuf = new byte[stdBufferSize];
                    stdSize = System.in.read(stdBuf);
                    userInput += new String(stdBuf, 0, stdSize);
                } while (stdBuf[stdSize - 1] != 10);
                userInput = userInput.substring(0, userInput.length() - 1);
                payloadSize = userInput.length();
                int payloadSizeLen = String.valueOf(payloadSize).length();
                //////유저입력받기//////
                byte[] dataTosend = new byte[2+2+payloadSizeLen + payloadSize];

                int headerLen = 2 + payloadSizeLen;
                dataTosend[0] = (byte) (headerLen / 10);
                dataTosend[1] = (byte) (headerLen % 10);
                int intValue;
                try {
                    intValue = Integer.parseInt(userInput);
                    dataTosend[0] = (byte)0;
                    dataTosend[1] = (byte)0;
                } catch (NumberFormatException e) {
                    dataTosend[0] = (byte)0;
                    dataTosend[1] = (byte)1;
                }




                out.println(userInput);

                byte[] buf;
                int bufsize;
                String echo = "";
                do {
                    buf = new byte[bufferSize];
                    bufsize = echoSocket.getInputStream().read(buf);
                    echo += new String(buf, 0, bufsize);
                } while (buf[bufsize - 1] != 10);
                echo = echo.substring(0, echo.length() - 1);
                System.out.println("echo>" + echo);
                if (echo.equals("exit")) {
                    break;
                }
            }

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

    //고장난클라이언트
    public static void tcpClient12345(int port, String hostName) {
        System.out.println("TCP Client Test");

        try {

            Socket echoSocket = new Socket(hostName, port);
            System.out.println("port : " + port + "\n");
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            String input;
            String userInput;

            do {
                byte[] stdInBuf = new byte[1024];
                int bytesRead = System.in.read(stdInBuf);

                System.out.println("BytesRead Size : " + bytesRead);
                userInput = new String(stdInBuf, 0, bytesRead - 1);
                System.out.println("userInput : " + userInput);
                out.println(userInput);
                System.out.println("echo>" + (input = in.readLine()));
            } while (!input.equals("exit"));


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
            int stdBufferSize = 8;

            while (true) {
                byte[] stdBuf;
                int stdSize;
                String userInput = "";
                do {
                    stdBuf = new byte[stdBufferSize];
                    stdSize = System.in.read(stdBuf);
                    userInput += new String(stdBuf, 0, stdSize);
                } while (stdBuf[stdSize - 1] != 10);
                userInput = userInput.substring(0, userInput.length() - 1);

                byte[] buf = (userInput).getBytes();
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

            socket.close();

        } catch (IOException ie) {
            System.err.println("Couldn't get I/O");
            System.err.println(ie.getMessage());
            System.exit(1);
        }


    }

    public static void udpClient12(int port, String hostName) {
        System.out.println("UDP Client Test");
        System.out.println("port : " + port + "\n");

        try {
            InetAddress address = InetAddress.getByName(hostName);
            DatagramSocket socket = new DatagramSocket();

            int serverBufferSize = 4;
            byte[] buf;

            while (true) {
                //유저입력을 받아서 바로 패킷으로 전송
                byte[] stdBuf;
                int stdLength;
                DatagramPacket packet;
                do {
                    stdBuf = new byte[serverBufferSize];
                    stdLength = System.in.read(stdBuf);
                    buf = (new String(stdBuf, 0, stdLength)).getBytes();
//                    System.out.println(Arrays.toString(buf));
                    packet = new DatagramPacket(buf, buf.length, address, port);
                    socket.send(packet);
                } while (stdBuf[stdLength - 1] != 10);

                //에코로 돌아온 데이터 합치기
                String input = "";
                do {
                    buf = new byte[8];
                    packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    input += new String(packet.getData(), 0, packet.getLength());
//                    System.out.println(new String(packet.getData(),0,packet.getLength()));
                } while ((packet.getData()[packet.getLength() - 1]) != 10);
                //합쳐진 문자열 출력
                System.out.println("echo>" + input.substring(0, input.length() - 1));
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
}
