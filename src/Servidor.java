import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static final int PORT = 1234;
    public static Control control = new Control();

    public static void main(String[] args) {
	// DataInputStream din = null;
	DataOutputStream dout = null;
	BufferedReader br = null;
	ServerSocket serverSocket = null;
	Socket socket = null;
	try {

	    System.out.println("Servidor conectado. Esperando conexiones. ");
	    serverSocket = new ServerSocket(PORT);
	    control.addSocketServer(socket);
	    socket = serverSocket.accept();
	    OutputStream outputStream = socket.getOutputStream();
	    // create an object output stream from the output stream so we can send an
	    // object through it
	    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
	    objectOutputStream.writeObject(control);
	    new Thread(new Control.LectorServer(control)).start();
	    System.out.println("Cliente conectado. Disfruten de la comunicación");
	    // din = new DataInputStream(socket.getInputStream());
	    dout = new DataOutputStream(socket.getOutputStream());
	    br = new BufferedReader(new InputStreamReader(System.in));

	    String sMensajeEnviado = "";
	    while (!sMensajeEnviado.equals("salir")) {
		// sMensajeRecibido = din.readUTF();
		// System.out.println("client says: " + sMensajeRecibido);
		sMensajeEnviado = br.readLine();
		dout.writeUTF(sMensajeEnviado);
		control.sumarSemaforoCliente();
		dout.flush();
	    }

	} catch (Exception ex) {
	    try {
		// din.close();
		socket.close();
		serverSocket.close();
		// br.close();
		// dout.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }

	    System.err.println("Error." + ex.getMessage());
	}

    }

}
