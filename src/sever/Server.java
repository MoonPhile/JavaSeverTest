package sever;

import java.awt.BorderLayout;
import java.io.*;
import java.net.*;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Server extends JFrame{
	
	private JTextArea textArea; //출력 UI
	private Socket socket = null; //Client와 통신하기 위한 Socket
	private ServerSocket serverSocket = null; //서버 생성을 위한 ServerSocket
	private BufferedReader in = null; //Client로부터 데이터를 읽어들이기 위한 입력스트림
	private PrintWriter out = null; //Client로 데이터를 보내기 위한 출력 스트림
	private int port = 30000; //서버 포트
	private int randomNumber = 0; //랜덤숫자
	private int remainCnt = 5; //남은숫자
	
	public Server() throws IOException{
		renderUI();
		initServer();
		doProcess();
	}
	
	//서버 초기화
	private void initServer() throws IOException{
		serverSocket = new ServerSocket(port); //서버 생성
		randomNumber = (int)((Math.random()*50)+1); //랜덤숫자 생성
	}
	

	private void doProcess() throws IOException{
		try {
			//클라이언트 접속대기
			socket = serverSocket.accept();
			in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //입력스트림 생성
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))); //출력스트림 생성
			
			sendMessage(" 서버에 접속되었습니다.  ");
            sendMessage(" 게임을 시작합니다 ");
            textArea.append(String.format("정답 %s \n", randomNumber));
            while (true) {

                String receiveMsg = receiveMessage();
                remainCnt--;

                textArea.append(String.format("서버로 부터 받은 숫자 %s \n", receiveMsg));

                if(  Integer.parseInt(receiveMsg ) > randomNumber){ // 추측숫자가  랜덤수 보다 큰경우
                    sendMessage("Down.");
                } else if ( Integer.parseInt(receiveMsg ) < randomNumber){ // 추측숫자가 랜덤수보다 작은경우
                    sendMessage("Up.");
                } else{
                    sendMessage("Congratulations you Win.");
                    break;
                }

                if( remainCnt == 0){
                    sendMessage("정말 안타깝군요. 나의 승리입니다.");
                    break;
                }else{
                    sendMessage(String.format("추측 횟수가 %d회 남았습니다.", remainCnt));
                }}}
            catch(IOException e) {
            e.printStackTrace();
            } finally {
                socket.close();
            } 
		}//클라이언트와 통신을 처리하는 함수
	
	 private String receiveMessage() throws IOException {
	        return in.readLine();
	    }//클라이언트에서 메세지 수신

	 private void sendMessage(String message) throws IOException {
	        out.println(message);
	        out.flush();
	    }//클라이언트로 메세지 전송
	 
	 private void renderUI() {

	        this.setTitle("서버");

	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        textArea = new JTextArea(20, 30);
	        textArea.setEditable(false);

	        add(textArea, BorderLayout.CENTER);
	        pack();
	        setVisible(true);
	    }//화면 UI 렌더링
	
	public static void main(String[] args) throws IOException {
		Server server = new Server();
	}

}
