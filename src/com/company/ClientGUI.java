//Yazad Jamshed Davur - 1050178
package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class ClientGUI {
    public JTextField textField1;
    public JPanel panel1;
    public JButton searchButton;
    public JButton addButton;
    public JButton deleteButton;
    public JLabel label;
    public JTextArea resultField;
    public static Socket socket;

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[1]);
        String serverAddress = (args[0]);
        //int port = 5555;
        //String serverAddress = "localhost";
        JFrame frame = new JFrame("Client");
        ClientGUI display = new ClientGUI();
        frame.setContentPane(display.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        socket = null;
        try {
            socket = new Socket(serverAddress, port);
        }
        catch (ConnectException e) {
            String currText = display.resultField.getText();
            display.resultField.setText(currText + "Exception \"" + e + "\" generated\nPlease try to reconnect.\n");
            //display.resultField.invalidate();
            display.addButton.setEnabled(false);
            display.deleteButton.setEnabled(false);
            display.searchButton.setEnabled(false);
            display.textField1.setEditable(false);
        }
        if (socket != null) {
            String currText = display.resultField.getText();
            display.resultField.setText(currText + "Connection Established with Server.\n");
        }
    }
    public static void send(String Operation, String inputStr, JTextArea resultField){
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                String ack = null;
                out.write(Operation + "," + inputStr);
                out.newLine();
                try {
                    out.flush();
                    ack = reader.readLine();
                }
                catch (IOException e) {
                    String currText = resultField.getText();
                    resultField.setText(currText + "Server is down. Please reconnect.\n");
                }
                if (ack != null){
                    String currText = resultField.getText();
                    resultField.setText(currText + ack + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    public ClientGUI() throws IOException {
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField1.getText();
                if(text.matches("[A-Za-z]+")) {
                    send("Search", text, resultField);
                }
                else {
                    String currText = resultField.getText();
                    resultField.setText(currText + "Please enter a single valid word between A-Z only for searching.\n");
                }
            }
        });
        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField1.getText();
                System.out.println(text);
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField1.getText();
                if(text.matches("[A-Za-z]+(, ?[A-Za-z]+( ?[A-Za-z]+)*)+")) {
                    send("Add", text, resultField);
                }
                else {
                    String currText = resultField.getText();
                    resultField.setText(currText + "Please enter a valid word between A-Z only followed by commas to add meanings.\n");
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField1.getText();
                if(text.matches("[A-Za-z]+")) {
                    send("Delete", text, resultField);
                }
                else {
                    String currText = resultField.getText();
                    resultField.setText(currText + "Please enter a single valid word between A-Z only for deletion.\n");
                }

            }
        });
    }
}