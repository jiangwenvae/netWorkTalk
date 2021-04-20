package entity;

import java.awt.*;
import java.io.Serializable;

public class FontStyle implements Serializable {
    private static final long serialVersionUID = -1156702467078049893L;
    /**
     * 字的内容
     */
    private String content;
    /**
     * 字的字体
     */
    private String fontFamily;
    /**
     * 字的大小
     */
    private int size;
    /**
     * 字的颜色
     */
    private Color color;
    /**
     * 字的样式
     */
    private int fontStyle;
    /**
     * 图片路径
     */
    private String path;

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getFontFamily() {
        return fontFamily;
    }
    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public int getFontStyle() {
        return fontStyle;
    }
    public void setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
    }
}
