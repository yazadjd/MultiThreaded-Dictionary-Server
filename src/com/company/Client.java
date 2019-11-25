package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Client {
    public static void main(String[] args) throws IOException {

        Socket client = new Socket("localhost", 5555);
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        dos.writeUTF("Hello!!\n");
        DataInputStream dis = new DataInputStream(client.getInputStream());
        System.out.println(dis.readLine());
        client.close();
    }
}
