import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class PlayerFrame extends JFrame {
    private int width,heigth;
    private Container contentPane;
    private PlayerSprite me;
    private PlayerSprite you;
    private DrawingComponent dc;
    private Timer animationTimer;
    private boolean up,down,left,rigth;
    private Socket socket;
    private int PlayerId;
    private ReadFromServer rfsRunnable;
    private WriteToServer wtsRunnable;

    public PlayerFrame(int w,int h){
        width = w;
        heigth = h;
        up = false;
        down = false;
        left = false;
        rigth = false;
    }

    public void setUpGUI(){
        contentPane = this.getContentPane();
        this.setTitle("Player #"+PlayerId);
        contentPane.setPreferredSize(new Dimension(width,heigth));

        createSprites();
        dc = new DrawingComponent();
        contentPane.add(dc);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);

        setUpAnimationTimer();
        setUpKeyListerner();
    }

    private void createSprites(){
        if(PlayerId == 1){
            me = new PlayerSprite(100,400,50,Color.BLUE);
            you = new PlayerSprite(490,400,50,Color.RED);

        }
        else{
            me = new PlayerSprite(490,400,50,Color.RED);
            you = new PlayerSprite(100,400,50,Color.BLUE);
        }
    }

    private void setUpAnimationTimer(){
        int interval = 10;
        ActionListener al = new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                double speed = 5;
                if (up){
                    me.moveV(-speed);
                } else if(down){
                    me.moveV(speed);
                } else if (left){
                    me.moveH(-speed);
                } else if(rigth){
                    me.moveH(speed);
                }
                dc.repaint();
            }
        };
        animationTimer = new Timer(interval,al);
        animationTimer.start();
    }

    private void setUpKeyListerner(){
        KeyListener kl = new KeyListener() {
            public void keyTyped(KeyEvent ke) {
            }
            public void keyPressed(KeyEvent ke) {
                int keyCode = ke.getKeyCode();

                switch (keyCode){

                    case KeyEvent.VK_W:
                        up = true;
                        break;
                    case KeyEvent.VK_S:
                        down = true;
                        break;
                    case KeyEvent.VK_A:
                        left = true;
                        break;
                    case KeyEvent.VK_D:
                        rigth = true;
                        break;
                }
            }
            public void keyReleased(KeyEvent ke) {
                int keyCode = ke.getKeyCode();
                switch (keyCode){
                    case KeyEvent.VK_W:
                        up = false;
                        break;
                    case KeyEvent.VK_S:
                        down = false;
                        break;
                    case KeyEvent.VK_A:
                        left = false;
                        break;
                    case KeyEvent.VK_D:
                        rigth = false;
                        break;
                }
            }
        };
        contentPane.addKeyListener(kl);
        contentPane.setFocusable(true);
    }

    private class DrawingComponent extends JComponent{
        protected void paintComponent(Graphics g){
            Graphics2D g2d = (Graphics2D) g;
            me.drawSprite(g2d);
            you.drawSprite(g2d);
        }
    }


    private void connectToServer(){
        try{
            socket = new Socket("localhost",45371);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            PlayerId = in.readInt();
            System.out.println("voce é o player de numero "+ PlayerId);
            if(PlayerId == 1){
                System.out.println("Esperando por outros players #2 concxões");
            }
            rfsRunnable = new ReadFromServer(in);
            wtsRunnable = new WriteToServer(out);
            rfsRunnable.waitForStartMsg();
        }catch (IOException ex){
            System.out.println("IOException from connect to server");
        }
    }

    private class ReadFromServer implements Runnable{
        private DataInputStream dataIn;

        public ReadFromServer(DataInputStream in){
            dataIn = in;
            System.out.println("RFS Runnable created");
        }
        public void run(){
            try{
                while (true){
                    if (you!=null) {
                        you.setX(dataIn.readDouble());
                        you.setY(dataIn.readDouble());
                    }
                }

            }catch (IOException ex){
                System.out.println("IOException RFS run()");
            }

        }

        public void waitForStartMsg(){
            try{
                String startMsg = dataIn.readUTF();
                System.out.println("Msg from server: "+startMsg);
                Thread readThread = new Thread(rfsRunnable);
                Thread writeThread = new Thread(wtsRunnable);
                readThread.start();
                writeThread.start();
            }catch (IOException ex){
                System.out.println("IOException waiforstartmsg");
            }
        }

    }

    private class WriteToServer implements Runnable{
        private DataOutputStream dataOut;

        public WriteToServer(DataOutputStream out){
            dataOut = out;
            System.out.println("WTS Runnable created");
        }
        public void run(){
            try{
                while (true){
                    if(me!=null) {
                        dataOut.writeDouble(me.getX());
                        dataOut.writeDouble(me.getY());
                        dataOut.flush();
                    }
                    try{
                        Thread.sleep(25);
                    }catch (InterruptedException ex){
                        System.out.println("Interrupted from WTS run");
                    }
                }

            }catch (IOException ex){
                System.out.println("IOException WriteToServer run()");
            }
        }
    }

    public static void main(String[] args) {
        PlayerFrame pf = new PlayerFrame(640,640);
        pf.connectToServer();
        pf.setUpGUI();
    }

}
















