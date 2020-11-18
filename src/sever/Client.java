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

	
    private  JTextField textField;// ���ڿ� �Է�â

    
    private JTextArea textArea;// ��� UI

   
    private Socket socket = null; //Server�� ����ϱ� ���� Socket

   
    private BufferedReader in = null; //Server�κ��� �����͸� �о���̱� ���� �Է½�Ʈ��

   
    PrintWriter out = null; //������ �������� ���� ��� ��Ʈ��

    InetAddress ia = null;

   
    private String serverId = "127.0.0.1"; // ���� ������

   
    private int serverPort = 30000; // ���� ��Ʈ

    public Client() throws IOException {
        renderUI();
        initClient();
        doProcess();
    }

     //Ŭ���̾�Ʈ �ʱ�ȭ

    private void initClient() throws IOException {
        ia = InetAddress.getByName(serverId);
        socket = new Socket(ia, serverPort);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
    }//Ŭ���̾�Ʈ �ʱ�ȭ

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
    }//Ŭ���̾�Ʈ�� ����� ó���ϴ� �Լ�

    private void renderUI() {

        this.setTitle("Ŭ���̾�Ʈ");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textField = new JTextField(30);
        textField.addActionListener(this);

        textArea = new JTextArea(20, 30);
        textArea.setEditable(false);

        add(textField, BorderLayout.PAGE_END);
        add(textArea, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }//ȭ���� UI�� �������Ѵ�.

    private String receiveMessage() throws IOException {
        return in.readLine();
    }//������ ���� �޼��� ����


    private void sendMessage(String message) throws IOException {
        out.println(message);
        out.flush();
    }//������ �޼��� ����

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
