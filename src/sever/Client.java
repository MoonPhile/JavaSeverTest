package sever;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame implements ActionListener{

	
    private  JTextField textField;// 문자열 입력창

    
    private JTextArea textArea;// 출력 UI

   
    private Socket socket = null; //Server와 통신하기 위한 Socket

   
    private BufferedReader in = null; //Server로부터 데이터를 읽어들이기 위한 입력스트림

   
    PrintWriter out = null; //서버로 내보내기 위한 출력 스트림

    InetAddress ia = null;

   
    private String serverId = "127.0.0.1"; // 서버 아이피

   
    private int serverPort = 30000; // 서버 포트

    public Client() throws IOException {
        renderUI();
        initClient();
        doProcess();
    }

     //클라이언트 초기화

    private void initClient() throws IOException {
        ia = InetAddress.getByName(serverId);
        socket = new Socket(ia, serverPort);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
    }//클라이언트 초기화

    private void doProcess() throws IOException {
        new Thread(() -> {
            try {
                while (true){
                    if( socket.isClosed()) break;
                    String msg = receiveMessage();
                    if( msg != null) textArea.append ( msg + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }//클라이언트와 통신을 처리하는 함수

    private void renderUI() {

        this.setTitle("클라이언트");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textField = new JTextField(30);
        textField.addActionListener(this);

        textArea = new JTextArea(20, 30);
        textArea.setEditable(false);

        add(textField, BorderLayout.PAGE_END);
        add(textArea, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }//화면의 UI를 렌더링한다.

    private String receiveMessage() throws IOException {
        return in.readLine();
    }//서버로 부터 메세지 수신


    private void sendMessage(String message) throws IOException {
        out.println(message);
        out.flush();
    }//서버로 메세지 전송

    @Override
    public void actionPerformed(ActionEvent evt) {
        try {
            String message = textField.getText();
            sendMessage(message);
            textField.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
    }
}
