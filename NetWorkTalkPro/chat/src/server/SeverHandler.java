package server;

import entity.ChatStatus;
import entity.FontStyle;
import entity.TransferInfo;
import io.IOStream;
import util.FontSupport;


import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 服务器端开辟一个线程，来处理一直读消息
 */
public class SeverHandler extends Thread {
    Socket socket;
    ServerFrame serverFrame;

    public SeverHandler(Socket socket,ServerFrame serverFrame) {
        this.socket = socket;
        this.serverFrame = serverFrame;
    }

   static List<String> onlineUser= new ArrayList<>();
   static List<Socket> onlineSocket = new ArrayList<>();
    @Override
    public void run() {
          //默认重复拿
        while (true) {
            try {
            Object obj = IOStream.readMessage(socket);
            if(obj instanceof TransferInfo){
                TransferInfo tfi = (TransferInfo) obj;
                if(tfi.getStatusEnum() == ChatStatus.LOGIN){
              //登陆消息
                    loginHandler(tfi);
                }else if(tfi.getStatusEnum() == ChatStatus.CHAT){
                    //聊天消息
                    chatHandler(tfi);
                }else if(tfi.getStatusEnum() == ChatStatus.DD){
                    doudong(tfi);
                }else if(tfi.getStatusEnum()==ChatStatus.QUIT){
                    //退出处理
                    loginOut(tfi);

                    //休眠1秒后
                    Thread.sleep(1000);
                    //关闭当前socket
                    socket.close();
                    //关闭当前线程
                    this.interrupt();
                    //跳出循环
                    break;
                }

            }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 用户退出处理，清理在线人数，刷新用户列表，告诉所有人，你已经离开
     * @param tfi
     */
    private void loginOut(TransferInfo tfi) {
        String userName = tfi.getUserName();
        //将该用户从用户集合移除
        Iterator<String> userIter = onlineUser.iterator();
        while(userIter.hasNext()) {
            if(userIter.next().equals(userName)) {
                userIter.remove();
            }
        }

        //将该用户从socket集合移除
        Iterator<Socket> socketIter = onlineSocket.iterator();
        while(socketIter.hasNext()) {
            Socket next = socketIter.next();
            if(socket == next) {
                socketIter.remove();
            }
        }

        //将user与Socket的关系从Map中移除
        ChatServer.userSocketMap.remove(userName);

        //刷新服务器面板的用户列表
        flushOnlineUserList();

        //给所有在线的用户发送下线消息
        tfi.setStatusEnum(ChatStatus.NOTICE);
        sendAll(tfi);

        //告诉其他人刷新用户列表
        tfi.setUserOnlineArray(onlineUser.toArray(new String [onlineUser.size()]));
        tfi.setStatusEnum(ChatStatus.ULIST);
        sendAll(tfi);
    }

    /**
     * 发送抖动消息给客户端
     * @param tfi
     */
    private void doudong(TransferInfo tfi){
          String reciver = tfi.getReciver();
          if("All".equals(reciver)){
              sendAll(tfi);
              log(tfi.getSender()+"给所有人发抖动消息:");
          }else {
              //私聊
              send(tfi);
              log(tfi.getSender()+"给"+tfi.getReciver()+"发抖动消息");
          }

    }

    /**
     * 刷新用户列表
     */
    public void flushOnlineUserList(){
   JList lstUser = serverFrame.onlineUserPanel.lstUser;
    String[] userArray = onlineUser.toArray(new String[onlineUser.size()]);
    lstUser.setListData(userArray);

    serverFrame.serverInfoPanel.txtNumber.setText(userArray.length+"");
}

    /**
     * 登陆处理，处理客户端请求
     * @param tfi
     */
    private void loginHandler(TransferInfo tfi){
        boolean flag = checkUserLogin(tfi);
        tfi.setLoginSuccessFlag(false);
        if(flag){
            //返回登陆成功
            tfi.setLoginSuccessFlag(true);
            tfi.setStatusEnum(ChatStatus.LOGIN);
            IOStream.writeMessage(socket,tfi);
            String userName = tfi.getUserName();

            //统计在线人数
            onlineUser.add(tfi.getUserName());
            onlineSocket.add(socket);

            //在线用户和管道的对应关系
            ChatServer.userSocketMap.put(userName,socket);

            //系统消息发给客户端，该用户上线
            tfi = new TransferInfo();
            tfi.setStatusEnum(ChatStatus.NOTICE);
            String notice =">>"+userName+"上线了";
            tfi.setNotice(">>"+userName+"上线了");
           sendAll(tfi);
           //发送最新的用户列表给客户端
        tfi = new TransferInfo();
        tfi.setUserOnlineArray(onlineUser.toArray(new String[onlineUser.size()]));
        tfi.setStatusEnum(ChatStatus.ULIST);
            sendAll(tfi);

            //刷新在线用户列表
            flushOnlineUserList();

            //记录日志
        log(notice);
        }else {
            System.out.println("登录失败");
            IOStream.writeMessage(socket,tfi);
            //记录日志
            log(tfi.getUserName()+"登录失败");
        }
    }

    /**
     * 记录日志的方法
     * @param log
     */
    public void log(String log){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        JTextPane txtLog = serverFrame.serverInfoPanel.txtLog;
        String oldlog = txtLog.getText();
        txtLog.setText(oldlog+"\n"+dateStr+""+log);
    }
    /**
     * 聊天消息处理
     * @param tfi
     */
    private void chatHandler(TransferInfo tfi){
        String reciver = tfi.getReciver();
        if("All".equals(reciver)){
            //发送给所有人
            sendAll(tfi);
         List<FontStyle> contents = tfi.getContent();
            FontSupport.fontDecode(serverFrame.serverInfoPanel.txtLog,contents,tfi.getSender(),"所有人");

            //记录日志

        }else {
            //私聊
            send(tfi);
            List<FontStyle> contents = tfi.getContent();
            FontSupport.fontDecode(serverFrame.serverInfoPanel.txtLog,contents,tfi.getSender(),tfi.getReciver());
        }
    }
    /**
     * 发送消息给所有人
     * @param tfi
     */
    public void  sendAll(TransferInfo tfi){
        for (int i=0;i<onlineSocket.size();i++){
            Socket tempSocket = onlineSocket.get(i);
            IOStream.writeMessage(tempSocket,tfi);
        }
    }

    public void  send(TransferInfo tfi){
        String reciver = tfi.getReciver();
        String sender = tfi.getSender();
        //根据reciver拿到Socket管道

        //通过用户名为键，socket为
        Socket socket1 = ChatServer.userSocketMap.get(reciver);
        IOStream.writeMessage(socket1,tfi);

        Socket socket2 = ChatServer.userSocketMap.get(sender);
        IOStream.writeMessage(socket2,tfi);
    }

    /**
     * 登录功能
     * @param tfi
     * @return
     */
    public boolean checkUserLogin(TransferInfo tfi){
        String userName = tfi.getUserName();
        String password = tfi.getPassword();
        try {
            FileInputStream fis = new FileInputStream(new File("D:\\javaproject\\NetWorkTalkPro\\chat\\src\\user.txt"));
          DataInputStream dis = new DataInputStream(fis);
          String row = null;
          while ((row = dis.readLine())!=null){
              //文件中读取的行
              if((userName+"|"+password).equals(row)){
                  return true;
              }
          }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
