//Yazad Jamshed Davur - 1050178
package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {
    public static int i = 0;
    //public JSONObject Dict;
   // protected Socket socket;
    private JPanel Panel;
    public JTextArea textField;

    public static void execute(Socket socket, JSONObject Dict, JTextArea textField) throws IOException {
        int count = i;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String line = null;
            while((line = reader.readLine() )!= null) {
                String currTrxt = textField.getText();
                textField.setText(currTrxt + "Message from Client " + count + " : " + line +"\n");
                String[] values = line.split(",");
                String key = values[1].toLowerCase().strip();
                synchronized (Dict) {
                    switch (values[0]) {
                        case "Search":
                            if(Dict.containsKey(key)) {
                                JSONArray meanings = (JSONArray) Dict.get(key);
                                int len = meanings.size();
                                String msg = "";
                                for(int k = 0; k < len ; k++) {
                                    int l = k + 1;
                                    msg = msg + ("Word Meaning " + l + " of " + key + ": " + (meanings.toArray())[k]+" ; ");
                                }
                                if ((msg != null) && (msg.length() > 0)) {
                                    msg = msg.substring(0, msg.length() - 2);
                                }
                                writer.write(msg);
                                writer.newLine();
                                writer.flush();
                                break;
                            }
                            else {
                                //System.out.println("Not exist");
                                 writer.write("Word does not exist in Dictionary");
                                 writer.newLine();
                                 writer.flush();
                                 break;
                            }
                        case "Add":
                            if(Dict.containsKey(key)) {
                                writer.write("This Dictionary Word already exists in Dictionary.\n");
                                writer.newLine();
                                writer.flush();
                                break;
                            }
                            else {
                                int length = values.length;
                                int meanlen = length - 2;
                                JSONArray newmeanings  = new JSONArray();
                                for (int k = 0; k < meanlen; k++) {
                                    newmeanings.add(values[k + 2].toLowerCase().strip());
                                }
                                Dict.put(key,newmeanings);
                                writer.write("Word and Meaning(s) Added.\n");
                                writer.newLine();
                                writer.flush();
                                break;
                            }
                        case "Delete":
                            if(Dict.containsKey(key)){
                                Dict.remove(key);
                                writer.write("Word deleted from Dictionary.\n");
                                writer.newLine();
                                writer.flush();
                                break;
                            }
                            else {
                                writer.write("Word does not exist in Dicitonary.\n");
                                writer.newLine();
                                writer.flush();
                                break;
                            }
                        default:
                            break;
                    }
                }
            }
        } catch (SocketException e) {
            String currTrxt = textField.getText();
            textField.setText(currTrxt + "Client " + count + " closed.\n");
        }
    }
    public static void main(String [] args) throws Exception {
        int port = Integer.parseInt(args[0]);
        String fileName = args[1];
        //int port = 5555;
        //String fileName = "dictionaryFile.json";
        JFrame frame = new JFrame("Server");
        Server display = new Server();
        frame.setContentPane(display.Panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        JSONParser jsonParser = new JSONParser();
        JSONObject Dict = null;
        ServerSocket serverSocket = null;
        try (FileReader reader = new FileReader(fileName))
        {
            Object obj = jsonParser.parse(reader);
            Dict = (JSONObject) obj;
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
            display.textField.setText(String.valueOf(e)+"\n");
        }
        try {
            serverSocket = new ServerSocket(port);
        }
        catch(IllegalArgumentException e) {
            String currText = display.textField.getText();
            display.textField.setText(currText + "Port value out of range\n");
        }
        String currText = display.textField.getText();
        if(serverSocket != null) {

        display.textField.setText(currText + "Server listening on port " + port + " for a connection\n");
        }
       while (serverSocket != null) {
           Socket socket = serverSocket.accept();
           i += 1;
           currText = display.textField.getText();
           display.textField.setText(currText + "Client connection number " + i + " accepted:\nRemote Hostname: " + socket.getInetAddress().getHostName() +"\nLocal Port: " + socket.getLocalPort()+"\n");
           JSONObject finalDict = Dict;
           Thread t = new Thread(() -> {
               try {
                   execute(socket, finalDict, display.textField);

               } catch (IOException e) {
                   e.printStackTrace();
               }
           });
           t.start();
        }
    }
}