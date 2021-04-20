package server;

import constans.Constants;
import entity.ServerInfoBean;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务器启动入口
 */
public class ChatServer {

    static Map<String,Socket> userSocketMap = new HashMap<>();
    ServerFrame serverFrame;

    public ChatServer(){
        try {
            //建立服务器的Socket监听
            ServerSocket sso = new ServerSocket(Constants.SERVER_PORT);
            serverFrame = new ServerFrame();

            //初始化服务器参数信息
            ServerInfoBean serverInfo = getServerIP();
            loadSeverInfo(serverInfo);

            //循环，解决多客户使用
            while (true){
                //等待连接，阻塞实现，会得到一个客户的连接
                Socket socket = sso.accept();

                SeverHandler severHandler = new SeverHandler(socket,serverFrame);
                severHandler.start();
                System.out.println("服务器接收到客户端的连接"+socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化加载服务器参数
     * @param serverInfo
     */
    public void loadSeverInfo(ServerInfoBean serverInfo){
        serverFrame.serverInfoPanel.txtIP.setText(serverInfo.getIp());
        serverFrame.serverInfoPanel.txtServerName.setText(serverInfo.getHostName());
          serverFrame.serverInfoPanel.txtLog.setText("服务器已启动。。。");
    }

    /**
     * 获取服务器的主机名和IP地址
     * @return 返回服务器IP等信息
     */
    public ServerInfoBean getServerIP() {
        ServerInfoBean sib = null;
        try {
            InetAddress serverAddress = InetAddress.getLocalHost();
            byte[] ipAddress = serverAddress.getAddress();
            sib = new ServerInfoBean();
            sib.setIp(serverAddress.getHostAddress());
            sib.setHostName(serverAddress.getHostName());
            sib.setPort(Constants.SERVER_PORT);

            System.out.println("Server IP is:" + (ipAddress[0] & 0xff) + "."
                    + (ipAddress[1] & 0xff) + "." + (ipAddress[2] & 0xff) + "."
                    + (ipAddress[3] & 0xff));
        } catch (Exception e) {
            System.out.println("###Cound not get Server IP." + e);
        }
        return sib;
    }


    public static void main(String[] args) {
     new ChatServer();
    }
}
