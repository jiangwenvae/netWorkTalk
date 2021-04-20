package io;

import java.io.*;
import java.net.Socket;

public class IOStream {


    /**
     * 从Socket管道中读取对象
     * @param socket
     * @return
     */
    public static Object readMessage(Socket socket){
        Object obj = null;

        try {
            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            obj = ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
         return obj;
    }

    public static void writeMessage(Socket socket,Object message){

        try {
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
