package com.echo;

public class Echo {
    public static void main(String[] args) {
        if (args.length > 2) {
            switch (args[0]) {
                case "server":
                    switch (args[1]){
                        case "tcp":
                            com.echo.EchoServer.tcpServer(args[2]);
                            break;
                        case "udp":
                            com.echo.EchoServer.udpServer(args[2]);
                            break;
                        default:
                            System.out.println("#########################");
                            System.out.println("#########tcp/udp#########");
                            System.out.println("#########################");
                    }
                    break;
                case "client":
                    switch (args[1]){
                        case "tcp":
                            com.echo.EchoClient.tcpClient(args[2]);
                            break;
                        case "udp":
                            com.echo.EchoClient.udpClient(args[2]);
                            break;
                        default:
                            System.out.println("#########################");
                            System.out.println("#########tcp/udp#########");
                            System.out.println("#########################");
                    }
                    break;
                default:
                    System.out.println("###############################");
                    System.out.println("#########server/client#########");
                    System.out.println("###############################");
            }
        } else {
            System.out.println("##################################################");
            System.out.println("#########server/client tcp/udp portNumber#########");
            System.out.println("##################################################");
        }
    }
}
