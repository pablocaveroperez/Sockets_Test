import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static final int PORT = 1234;

    public static void main(String[] args) {
	DataInputStream din = null;
	DataOutputStream dout = null;
	BufferedReader br = null;
	ServerSocket serverSocket = null;
	Socket socket = null;

	try {

	    System.out.println("Servidor conectado. Esperando conexiones. ");
	    serverSocket = new ServerSocket(PORT, 4);
	    while (true) {
		socket = serverSocket.accept();
		System.out.println("Cliente conectado. Disfruten de la comunicación");
		new Thread(new Control(socket)).start();
		//din = new DataInputStream(socket.getInputStream());
		//dout = new DataOutputStream(socket.getOutputStream());
		//br = new BufferedReader(new InputStreamReader(System.in));

		//String sMensajeRecibido = "", sMensajeEnviado = "";

//		sMensajeRecibido = din.readUTF();
//		System.out.println("client says: " + sMensajeRecibido);
//		sMensajeEnviado = br.readLine();
//		dout.writeUTF(sMensajeEnviado);
//		dout.flush();
	    }

	} catch (Exception ex) {
	    try {
		din.close();
		socket.close();
		serverSocket.close();
		br.close();
		dout.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }

	    System.err.println("Error." + ex.getMessage());
	}

    }

}
