package cilent;

/**
 * 抖动类
 */
public class DouDong extends Thread{
    ChatFrame frame;

    public DouDong(ChatFrame frame) {
        this.frame = frame;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 3; i++) {
                frame.setLocation(frame.getX()-28, frame.getY());
                Thread.sleep(88);
                frame.setLocation(frame.getX(), frame.getY()-28);
                Thread.sleep(88);
                frame.setLocation(frame.getX()+28, frame.getY());
                Thread.sleep(88);
                frame.setLocation(frame.getX(), frame.getY()+28);
                Thread.sleep(88);
            }
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }
}
