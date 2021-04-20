package cilent;

import entity.ChatStatus;
import entity.TransferInfo;
import io.IOStream;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;


/**
 * 登陆界面
 */

public class LoginFrame extends JFrame {

    /**
     * 登录窗体宽度
     */
    private static final Integer FRAME_WIDTH = 400;

    /**
     * 登录窗体高度
     */
    private static final Integer FRAME_HEIGHT = 300;

    public LoginFrame() {
        this.setTitle("登录窗体");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        //获取屏幕
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        setLocation((width-FRAME_WIDTH)/2, (height-FRAME_HEIGHT)/2);

        //加载窗体的背景图片
        ImageIcon imageIcon = new ImageIcon("D:\\javaproject\\netWorkTalk\\chat\\src\\image\\beijing2.jpg");
        //创建一个标签并将图片添加进去
        JLabel lblBackground=new JLabel(imageIcon);
        //设置图片的位置和大小
        lblBackground.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        //设置布局为空布局
        lblBackground.setLayout(null);
        //添加到当前窗体中
        this.add(lblBackground);

        //创建一个标签
        JLabel lblUid=new JLabel("账 号: ");
        //设置位置、大小
        lblUid.setBounds(80, 40, 120, 30);
        lblUid.setFont(new Font("宋体" , 0 , 16));
        //设置标签文本的颜色为白色
        lblUid.setForeground(Color.WHITE);
        //将标签添加到背景图片上
        lblBackground.add(lblUid);

        //账号文本框
        JTextField textUid = new JTextField();
        //设置文本框的位置、大小
        textUid.setBounds(150, 40, 160, 30);
        lblBackground.add(textUid);

        //创建一个的标签
        JLabel lblPsw=new JLabel("密 码: ");
        //设置标签的位置、大小
        lblPsw.setBounds(80, 80, 120, 30);
        lblPsw.setFont(new Font("宋体" , 0 , 16));
        //设置字体颜色为白色
        lblPsw.setForeground(Color.WHITE);
        //添加到背景图片上
        lblBackground.add(lblPsw);

        //创建一个密码框，用于输入用户密码
        JPasswordField textPsw = new JPasswordField();
        //设置密码框的位置、大小
        textPsw.setBounds(150, 80, 160, 30);
        lblBackground.add(textPsw);


        //创建一个文字按钮
        JButton enter = new JButton("登 录");
        //设置位置、大小
        enter.setBounds(110, 170, 80, 25);
        enter.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = textUid.getText();
                String password = textPsw.getText();
                TransferInfo tif = new TransferInfo();
                tif.setUserName(userName);
                tif.setPassword(password);
                //这是登录消息
                tif.setStatusEnum(ChatStatus.LOGIN);
                connectionServer(tif);
            }
        });

        lblBackground.add(enter);


        //创建一个取消按钮
        JButton cancel=new JButton("取 消");
        //设置按钮的位置、大小
        cancel.setBounds(215, 170, 80, 25);
        //添加到背景图片上
        lblBackground.add(cancel);

        //设置布局为空布局
        setLayout(null);

        setVisible(true);
    }

    /**
     * 连接服务器对象
     * @param tif
     */
    public void  connectionServer(TransferInfo tif){
    try {
        System.out.println("连接之前");
        Socket socket = new Socket("127.0.0.1", 8888);

        IOStream.writeMessage(socket,tif);

        ClientHandler clientHandler = new ClientHandler(socket,this);
        clientHandler.start();
        System.out.println("客户端启动之后");

    } catch (IOException e1) {
        e1.printStackTrace();
    }
}
    public static void main(String[] args) {
         new LoginFrame();
    }
}
