package cilent;

/**
 * 客户端开辟线程  取消息
 */

import entity.ChatStatus;
import entity.FontStyle;
import entity.TransferInfo;
import io.IOStream;
import util.FontSupport;

import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread {
    Socket socket;
//登陆窗体
    LoginFrame loginFrame;
//聊天窗体
    ChatFrame chatFrame;

    public ClientHandler(Socket socket,LoginFrame loginFrame) {
        this.socket = socket;
        this.loginFrame = loginFrame;
    }

    @Override
    public void run() {
            //默认重复拿
            while (true) {
                try {
                    //模拟一直拿消息，产生阻塞
                    Object obj = IOStream.readMessage(socket);
                    if(obj instanceof TransferInfo){
                        TransferInfo tfi = (TransferInfo) obj;
                        if(tfi.getStatusEnum() == ChatStatus.LOGIN){
                            //登陆消息
                            loginResult(tfi);
                        }else if(tfi.getStatusEnum() == ChatStatus.CHAT){
                            //聊天消息
                            chatReslut(tfi);
                        }else if(tfi.getStatusEnum() == ChatStatus.NOTICE){
                            noticeResult(tfi);
                        }else if(tfi.getStatusEnum()== ChatStatus.ULIST){
                              //刷新当前在线用户列表
                            onlineUserResult(tfi);
                        }else if(tfi.getStatusEnum() == ChatStatus.DD){
                            DDResult(tfi);
                        }
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

    }

    /**
     * 接收抖动信息
     * @param tfi
     */
    private void DDResult(TransferInfo tfi){
        DouDong dd = new DouDong(chatFrame);
        dd.start();
    }

    /**
     * 登录结果的处理
     * @param tfi
     */
    public void loginResult(TransferInfo tfi) {
        if(tfi.getLoginSuccessFlag()) {
            //根据实体类取出用户名
            String userName = tfi.getUserName();

            //登录成功，打开主界面
            chatFrame = new ChatFrame(userName,socket);
            loginFrame.dispose();//关闭窗体
        }else {
            //登录失败
            System.out.println("客户端接收到登录失败");
        }
    }

    /**
     * 聊天消息处理
     * @param tfi
     */
    public void chatReslut(TransferInfo tfi){
         String sender = tfi.getSender();
         String receiver = tfi.getReciver();
        List<FontStyle> cotents = tfi.getContent();
        FontSupport.fontDecode(chatFrame.acceptPane,cotents,sender,receiver);
    }

    /**
     * 系统消息提示
     * @param tfi
     */
    private void noticeResult(TransferInfo tfi){
        //公屏投射系统消息
        FontSupport.contentAppend( chatFrame.acceptPane,"\n"+tfi.getNotice());
    }

    /**
     * 刷新当前用户列表
     * @param tfi
     */
   public void onlineUserResult(TransferInfo tfi){
       String[] userOnlineArray = tfi.getUserOnlineArray();
       chatFrame.lstUser.setListData(userOnlineArray);
   }
}
