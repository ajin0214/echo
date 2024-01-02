package com.echo;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Echo {
    public static void main(String[] args) {

        if (args.length < 3 || args.length >4) {
            System.out.println("tcp/udp server/client portNumber address(optional)");
            return;
        }

        String mode = "client";
        String protocol = "tcp";
        int port = 8080;
        String address = "localhost";

        for (String arg : args) {
            if (arg.equals("server") || arg.equals("client")) {
                mode = arg;
            } else if (arg.equals("tcp") || arg.equals("udp")) {
                protocol = arg;
            } else if (arg.matches("\\d+")) {
                port = Integer.parseInt(arg);
            } else {
                address = arg;
            }
        }

        System.out.println("mode : " + mode);
        System.out.println("protocol : " + protocol);
        System.out.println("port : " + port);
        System.out.println("address : " + address);
        System.out.println("***************************************");
        System.out.println("***************************************");
        System.out.println("***************************************");

        switch (mode) {
            case "server":
                switch (protocol) {
                    case "tcp":
                        com.echo.EchoServer.tcpServer(port);
                        break;
                    case "udp":
                        com.echo.EchoServer.udpServer(port);
                        break;
                }
                break;
            case "client":
                switch (protocol) {
                    case "tcp":
                        com.echo.EchoClient.tcpClient(port, resolve_IP_address(address));
                        break;
                    case "udp":
                        com.echo.EchoClient.udpClient(port, resolve_IP_address(address));
                        break;
                }
                break;
        }
    }

    public static String resolve_IP_address(String host) {
        try {
            InetAddress inetAddress = InetAddress.getByName(host);
            String ipAddress = inetAddress.getHostAddress();
            System.out.println("Resolved IP Address: " + ipAddress);
            return ipAddress;
        } catch (UnknownHostException e) {
            System.out.println("Error: Unable to resolve host '" + host + "'.");
            return null;
        }
    }
}
