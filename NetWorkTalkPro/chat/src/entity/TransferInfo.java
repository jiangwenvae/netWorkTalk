package entity;

import java.io.Serializable;
import java.util.List;

/**
 * 数据封装
 */
public class TransferInfo implements Serializable {
    private static final long serialVersionUID = 6543722756249559791L;

    private String userName;
    private  String password;
    //聊天消息内容
    private List<FontStyle> content;

    public List<FontStyle> getContent() {
        return content;
    }

    public void setContent(List<FontStyle> content) {
        this.content = content;
    }

    //系统消息
    private String notice;
    private Boolean loginSuccessFlag = false;
    //消息类型枚举
    private ChatStatus statusEnum;

    //在线的用户列表
    private String[] userOnlineArray;

    //发送人
    private String sender;
    //接收人
    private String reciver;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String[] getUserOnlineArray() {
        return userOnlineArray;
    }

    public void setUserOnlineArray(String[] userOnlineArray) {
        this.userOnlineArray = userOnlineArray;
    }

    public ChatStatus getStatusEnum() {
        return statusEnum;
    }

    public void setStatusEnum(ChatStatus statusEnum) {
        this.statusEnum = statusEnum;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Boolean getLoginSuccessFlag() {
        return loginSuccessFlag;
    }

    public void setLoginSuccessFlag(Boolean loginSuccessFlag) {
        this.loginSuccessFlag = loginSuccessFlag;
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
