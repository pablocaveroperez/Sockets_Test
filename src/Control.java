import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Control implements Runnable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Socket socket;
	public static Integer iContadorClientes = 0;
	DataInputStream din;
	private ObjectOutputStream objStream;

	public Control(Socket socket, ObjectOutputStream objStream) {
		this.socket = socket;
		this.objStream = objStream;
	}

	@Override
	public void run() {
		boolean bConectado = true;
		while (bConectado) {
			try {
				din = new DataInputStream(this.socket.getInputStream());
				String sMensajeRecibido = din.readUTF();
				Servidor.shareToAll(sMensajeRecibido, objStream);
			} catch (IOException e) {
			    	Servidor.clienteStream.remove(objStream);
				System.err.println("Usuario expulsado por gitano");
				bConectado = false;
			}

		}
	}

}
