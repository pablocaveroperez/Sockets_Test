import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Cliente {

    private static final String HOST = "localhost";

    public static void main(String[] args) {

	// DataInputStream din = null;
	DataOutputStream dout = null;
	BufferedReader br = null;
	Socket socket = null;

	try {
	    System.out.println("El cliente se va a conectar");
	    socket = new Socket(HOST, Servidor.PORT);
	    System.out.println("Cliente conectado");
	    InputStream inputStream = socket.getInputStream();
	    // create a DataInputStream so we can read data from it.
	    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
	    Control control = (Control) objectInputStream.readObject();
	    control.addSocketCliente(socket);
	    new Thread(new Control.LectorCliente(control)).start();
	    // din = new DataInputStream(socket.getInputStream());
	    dout = new DataOutputStream(socket.getOutputStream());
	    br = new BufferedReader(new InputStreamReader(System.in));

	    String sMensajeRecibido = "", sMensajeEnviado = "";
	    while (!sMensajeEnviado.equals("salir")) {
		sMensajeEnviado = br.readLine();
		dout.writeUTF(sMensajeEnviado);
		Servidor.control.sumarSemaforoServer();
		dout.flush();
		// sMensajeRecibido = din.readUTF();
		// System.out.println("Server says: " + sMensajeRecibido);
	    }

	} catch (Exception ex) {
	    try {
		// din.close();
		socket.close();
		br.close();
		dout.close();
	    } catch (IOException e) {
		System.out.println("No se ha podido conectar con el servidor");
	    }

	    System.err.println("Error." + ex.getMessage());
	}

    }

}
