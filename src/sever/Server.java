package sever;

import java.awt.BorderLayout;
import java.io.*;
import java.net.*;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Server extends JFrame{
	
	private JTextArea textArea; //��� UI
	private Socket socket = null; //Client�� ����ϱ� ���� Socket
	private ServerSocket serverSocket = null; //���� ������ ���� ServerSocket
	private BufferedReader in = null; //Client�κ��� �����͸� �о���̱� ���� �Է½�Ʈ��
	private PrintWriter out = null; //Client�� �����͸� ������ ���� ��� ��Ʈ��
	private int port = 30000; //���� ��Ʈ
	private int randomNumber = 0; //��������
	private int remainCnt = 5; //��������
	
	public Server() throws IOException{
		renderUI();
		initServer();
		doProcess();
	}
	
	//���� �ʱ�ȭ
	private void initServer() throws IOException{
		serverSocket = new ServerSocket(port); //���� ����
		randomNumber = (int)((Math.random()*50)+1); //�������� ����
	}
	

	private void doProcess() throws IOException{
		try {
			//Ŭ���̾�Ʈ ���Ӵ��
			socket = serverSocket.accept();
			in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //�Է½�Ʈ�� ����
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))); //��½�Ʈ�� ����
			
			sendMessage(" ������ ���ӵǾ����ϴ�.  ");
            sendMessage(" ������ �����մϴ� ");
            textArea.append(String.format("���� %s \n", randomNumber));
            while (true) {

                String receiveMsg = receiveMessage();
                remainCnt--;

                textArea.append(String.format("������ ���� ���� ���� %s \n", receiveMsg));

                if(  Integer.parseInt(receiveMsg ) > randomNumber){ // �������ڰ�  ������ ���� ū���
                    sendMessage("Down.");
                } else if ( Integer.parseInt(receiveMsg ) < randomNumber){ // �������ڰ� ���������� �������
                    sendMessage("Up.");
                } else{
                    sendMessage("Congratulations you Win.");
                    break;
                }

                if( remainCnt == 0){
                    sendMessage("���� ��Ÿ������. ���� �¸��Դϴ�.");
                    break;
                }else{
                    sendMessage(String.format("���� Ƚ���� %dȸ ���ҽ��ϴ�.", remainCnt));
                }}}
            catch(IOException e) {
            e.printStackTrace();
            } finally {
                socket.close();
            } 
		}//Ŭ���̾�Ʈ�� ����� ó���ϴ� �Լ�
	
	 private String receiveMessage() throws IOException {
	        return in.readLine();
	    }//Ŭ���̾�Ʈ���� �޼��� ����

	 private void sendMessage(String message) throws IOException {
	        out.println(message);
	        out.flush();
	    }//Ŭ���̾�Ʈ�� �޼��� ����
	 
	 private void renderUI() {

	        this.setTitle("����");

	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        textArea = new JTextArea(20, 30);
	        textArea.setEditable(false);

	        add(textArea, BorderLayout.CENTER);
	        pack();
	        setVisible(true);
	    }//ȭ�� UI ������
	
	public static void main(String[] args) throws IOException {
		Server server = new Server();
	}

}
