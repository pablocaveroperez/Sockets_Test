import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.net.ssl.SSLServerSocketFactory;

public class Servidor {

    public static final int PORT = 1234;
    public static ArrayList<ObjectOutputStream> clienteStream = new ArrayList<ObjectOutputStream>();
    public static int iContador = 0;

    /**
     * Creacion de certificados: keytool -genkey -keyalg RSA -alias serverKey
     * -keystore serverKey.jks -storepass servpass
     * 
     * Creacion de certificado de servidor keytool -export -keystore serverkey.jks
     * -alias serverKey -file ServerPublicKey.cer (Contraseña es servpass)
     * 
     * Creacion de certificado para cliente keytool -import -alias serverKey -file
     * ServerPublicKey.cer -keystore clienteTrustedCerts.jks -keypass clientpass
     * -storepass servpass
     * 
     * @param args
     */

    public static void main(String[] args) {
	System.setProperty("javax.net.ssl.keyStore", "certs/serverKey.jks");
	System.setProperty("javax.net.ssl.keyStorePassword", "servpass");
	System.setProperty("javax.net.ssl.trustStore", "certs/clientTrustedCerts.jks");
	System.setProperty("javax.net.ssl.trustStorePassword", "servpass");
	ServerSocket serverSocket = null;
	Socket socket = null;

	try {

	    SSLServerSocketFactory serverFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
	    //serverSocket = serverFactory.createServerSocket(PORT, 4);

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

	    ex.printStackTrace();

	    System.err.println("Error." + ex.getMessage());
	} finally {
	    if (socket != null) {
		try {
		    socket.close();
		    serverSocket.close();
		} catch (IOException ex) {
		    System.err.println(ex.getMessage());
		}
	    }
	}

    }

    public static int getNewID() {
	iContador++;
	return iContador;
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

	public ControlCliente(Socket clientSocket) {
	    this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
	    try {
		ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
		boolean bConexion = true;
		while (bConexion) {
		    try {

			System.out.println(ois.readUTF());
		    } catch (IOException e) {
			System.err.println("Error en Controlcliente: " + e.getMessage());
			bConexion = false;
		    }
		}
	    } catch (IOException e) {
		System.out.println(clientSocket.getInetAddress().getHostAddress() + " disconnected from the Server");
		clienteStream.remove(clientSocket);
	    }
	}
    }

}
