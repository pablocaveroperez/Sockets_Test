import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Servidor {

    public static final int PORT = 1234;
    public static ArrayList<ObjectOutputStream> clienteStream = new ArrayList<ObjectOutputStream>();

    public static void main(String[] args) {
	DataInputStream din = null;
	DataOutputStream dout = null;
	BufferedReader br = null;
	ServerSocket serverSocket = null;
	Socket socket = null;

	try {

	    serverSocket = new ServerSocket(PORT, 4);
	    System.out.println("Servidor conectado. Esperando conexiones. ");

	    while (true) {
		socket = serverSocket.accept();
		ObjectOutputStream objOutStream = new ObjectOutputStream(socket.getOutputStream());
		clienteStream.add(objOutStream);
		System.out.println("Cliente conectado. Disfruten de la comunicación");
		Thread t = new Thread(new Control(socket, objOutStream));
		t.start();

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

    public static void shareToAll(String sMensaje, ObjectOutputStream obj) {
	for (ObjectOutputStream stream : clienteStream) {
	    if (!stream.equals(obj)) {
		try {
			stream.writeUTF(sMensaje);
			stream.flush();
		    } catch (Exception ex) {
			ex.printStackTrace();
		    }
	    }
	}
    }
    
    public static class ControlCliente implements Runnable {
	    Socket clientSocket;

	    public ControlCliente(Socket clientSocket){
	        this.clientSocket = clientSocket;
	    }
	    @Override
	    public void run() {
	        try {
	            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
	            while(true){
	                try {
	                    
	                    System.out.println(ois.readUTF());
	                } catch ( IOException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	            }
	        }catch(SocketException e){
	            System.out.println(clientSocket.getInetAddress().getHostAddress()+" disconnected from the Server");
	            clienteStream.remove(clientSocket);
	        }catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	}

}
