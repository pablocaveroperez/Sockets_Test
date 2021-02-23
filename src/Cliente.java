import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;

public class Cliente {

    private static final String HOST = "localhost";

    public static void main(String[] args) {

	DataInputStream din = null;
	DataOutputStream dout = null;
	BufferedReader br = null;
	Socket socket = null;
	ControlCliente controlCliente = null;

	try {
	    System.out.println("El cliente se va a conectar");
	    socket = new Socket(HOST, Servidor.PORT);
	    System.out.println("Cliente conectado");
	    Control.cogerAtencion(socket);
	    System.out.println("Cojo la anteicón bien.");
	    InputStream inputStream = socket.getInputStream();
	    System.out.println("input creado");

	    // create a DataInputStream so we can read data from it.
	    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
	    System.out.println("Objet traido.");

	    // read the list of messages from the socket
	    List<ControlCliente> lClientes = (List<ControlCliente>) objectInputStream.readObject();
	    System.out.println("La lista la coge bien. Tiene ahora: "+lClientes.size());
	    controlCliente = new ControlCliente(Control.iContadorClientes++);

	    lClientes.add(controlCliente);
	    Control.soltarAtencion(lClientes);
	    System.out.println("Lo ha soltado bien "+lClientes.size());

	    new Thread(controlCliente).start();
	    // din = new DataInputStream(socket.getInputStream());
	    dout = new DataOutputStream(socket.getOutputStream());
	    br = new BufferedReader(new InputStreamReader(System.in));

	    String sMensajeRecibido = "", sMensajeEnviado = "";
	    while (!sMensajeEnviado.equals("gitano")) {
		sMensajeEnviado = br.readLine();
		dout.writeUTF(sMensajeEnviado);
		dout.flush();

		// sMensajeRecibido = din.readUTF();
		// System.out.println("Server says: " + sMensajeRecibido);
	    }

	} catch (Exception ex) {
	    try {
		Control.lClientes.remove(controlCliente);
		din.close();
		socket.close();
		br.close();
		dout.close();
	    } catch (IOException e) {
		System.out.println("No se ha podido conectar con el servidor");
	    }
	    ex.printStackTrace();
	    System.err.println("Error." + ex.getMessage());
	}

    }

}
