package cilent;

import entity.ChatStatus;
import entity.FontStyle;
import entity.TransferInfo;
import io.IOStream;
import util.FontSupport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;

/**
 * 聊天主界面
 */

public class ChatFrame extends JFrame {
    private static final long serialVersionUID = -8945833334986986964L;
    /**
     * 服务器窗体宽度
     */
    public static final Integer FRAME_WIDTH = 750;

    /**
     * 服务器窗体高度
     */
    public static final Integer FRAME_HEIGHT = 600;

    public JTextPane acceptPane;
    public JList lstUser;
     String userName;
     Socket socket;
    ChatFrame chatFrame;
    JComboBox fontFamilyCmb;
    JComboBox reciverBox;
    public ChatFrame(String userName, Socket socket){
        chatFrame = this;
        this.socket = socket;
        this.userName = userName;
        this.setTitle("聊天室主界面");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        //窗体不可扩大
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //获取屏幕
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        //屏幕居中处理
        setLocation((width-FRAME_WIDTH)/2, (height-FRAME_HEIGHT)/2);

        //加载窗体的背景图片
        ImageIcon imageIcon = new ImageIcon("D:\\javaproject\\NetWorkTalkPro\\background\\beijing (2).jpg");
        //创建一个标签并将图片添加进去
        JLabel frameBg = new JLabel(imageIcon);
        //设置图片的位置和大小
        frameBg.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        this.add(frameBg);

        // 接收框
        acceptPane = new JTextPane();
        acceptPane.setOpaque(false);//设置透明
        acceptPane.setFont(new Font("宋体", 0, 16));

        // 设置接收框滚动条
        JScrollPane scoPaneOne = new JScrollPane(acceptPane);
        scoPaneOne.setBounds(15, 20, 500, 332);
        //设置背景透明
        scoPaneOne.setOpaque(false);
        scoPaneOne.getViewport().setOpaque(false);
        frameBg.add(scoPaneOne);

        //当前在线用户列表
        lstUser = new JList();
        lstUser.setFont(new Font("宋体", 0, 14));
        lstUser.setVisibleRowCount(17);
        lstUser.setFixedCellWidth(180);
        lstUser.setFixedCellHeight(18);

        //声明菜单
        JPopupMenu popupMenu = new JPopupMenu();

        //私聊按钮（菜单项）
        JMenuItem privateChat = new JMenuItem("私聊");
        privateChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Object reciverObj = lstUser.getSelectedValue();
                if(reciverObj!=null){
                    String reciver = String.valueOf(reciverObj);
                    reciverBox.removeAllItems();
                    reciverBox.addItem("All");
                    reciverBox.addItem(reciver);
                    reciverBox.setSelectedItem(reciver);
                }
                reciverBox.addItem("");
            }
        });
        popupMenu.add(privateChat);

        //添加点击事件，需要确认右键
        lstUser.addMouseListener(new MouseAdapter() { //适配器
            @Override
            public void mouseClicked(MouseEvent e) {
                //监听左键还是右键
                if(e.isMetaDown()){
                    //弹出菜单
                    popupMenu.show(lstUser,e.getX(),e.getY());
                }
            }
        });

        JScrollPane spUser = new JScrollPane(lstUser);
        spUser.setFont(new Font("宋体", 0, 14));
        spUser.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        spUser.setBounds(530, 17, 200, 507);
        frameBg.add(spUser);

        // 输入框
        JTextPane sendPane = new JTextPane();
        sendPane.setOpaque(false);
        sendPane.setFont(new Font("宋体", 0, 16));

        JScrollPane scoPane = new JScrollPane(sendPane);// 设置滚动条
        scoPane.setBounds(15, 400, 500, 122);
        scoPane.setOpaque(false);
        scoPane.getViewport().setOpaque(false);
        frameBg.add(scoPane);

        // 添加表情选择
        JLabel lblface = new JLabel(new ImageIcon("D:\\javaproject\\NetWorkTalkPro\\background\\face.png"));
        lblface.setBounds(14, 363, 25, 25);
        lblface.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                FaceFrame face = new FaceFrame(sendPane);
            }
        });
        frameBg.add(lblface);

        // 添加抖动效果
        JLabel lbldoudong = new JLabel(new ImageIcon("D:\\javaproject\\NetWorkTalkPro\\background\\doudong.png"));
        lbldoudong.setBounds(43, 363, 25, 25);
        lbldoudong.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                //抖动功能实现
                TransferInfo tfi = new TransferInfo();
                tfi.setStatusEnum(ChatStatus.DD);
                tfi.setSender(userName);
                String recvier = "All";
                //要发给谁
                Object reciverObj = reciverBox.getSelectedItem();
                if(reciverObj!=null){
                    recvier = String.valueOf(reciverObj);
                }
                tfi.setReciver(recvier);
                IOStream.writeMessage(socket, tfi);
            }

        });
        frameBg.add(lbldoudong);

        // 设置字体选择
        JLabel lblfontChoose = new JLabel(new ImageIcon("D:\\javaproject\\NetWorkTalkPro\\background\\ziti.png"));
        lblfontChoose.setBounds(44, 363, 80, 25);
        lblfontChoose.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                JColorChooser colorChooser = new JColorChooser();
                Color color = colorChooser.showDialog(ChatFrame.this, "字体颜色", Color.BLACK);
                //字体改变
                FontSupport.setFont(sendPane, color, fontFamilyCmb.getSelectedItem().toString(), Font.BOLD, 60);
            }

        });
        frameBg.add(lblfontChoose);

        //字体下拉选项
         fontFamilyCmb = new JComboBox();
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        String[] str = graphicsEnvironment.getAvailableFontFamilyNames();
        for (String string : str) {
            fontFamilyCmb.addItem(string);
        }
        fontFamilyCmb.setSelectedItem("楷体");
        fontFamilyCmb.setBounds(104, 363, 150, 25);
        frameBg.add(fontFamilyCmb);


        //label标签
        JLabel reciverLabel = new JLabel("聊天对象");
        reciverLabel.setBounds(304, 363, 80, 25);
        frameBg.add(reciverLabel);

        //下拉选择框
        reciverBox = new JComboBox();
        reciverBox.setSelectedItem("All");
        reciverBox.addItem("All");
        reciverBox.setBounds(374, 363, 150, 25);
        frameBg.add(reciverBox);

        /**
         * 发送按钮
         */
        JButton send = new JButton("发 送");
        send.setBounds(15, 533, 125, 25);
        send.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String content = sendPane.getText();
                TransferInfo tfi = new TransferInfo();

              //包装了所有文字对应属性
                java.util.List<FontStyle> fontStyles = FontSupport.fontEncode(sendPane);
                tfi.setContent(fontStyles);

                //发送人
                tfi.setSender(userName);
                String reciver = "All";

                Object reciverObj = reciverBox.getSelectedItem();
                if(reciverObj !=null){
                    reciver = String.valueOf(reciverObj);
                }
                System.out.println("111"+reciver);
                //接收人ALL
                tfi.setReciver(reciver);
                //本次处理消息类型为
                tfi.setStatusEnum(ChatStatus.CHAT);
                IOStream.writeMessage(socket,tfi);
                sendPane.setText("");
            }
        });
        frameBg.add(send);

        //客户端关闭窗体退出
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    System.out.println(userName + "窗口关闭");
                    TransferInfo tfi = new TransferInfo();
                    tfi.setStatusEnum(ChatStatus.QUIT);
                    tfi.setUserName(userName);
                    tfi.setNotice(userName + "已离开聊天室.....");
                    IOStream.writeMessage(socket, tfi);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        setVisible(true);
    }
}
