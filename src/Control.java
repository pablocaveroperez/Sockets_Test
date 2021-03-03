import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class Control implements Runnable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Socket socket;
	public static Integer iContadorClientes = 0;
	DataInputStream din;
	private static Semaphore semaControl = new Semaphore(1);
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
				if (!sMensajeRecibido.equals("gitano")) {
					Servidor.shareToAll(sMensajeRecibido, objStream);
				} else {
				    Servidor.clienteStream.remove(objStream);
				    System.out.println("Se pira paco");
				}
			} catch (IOException e) {
				System.err.println("Error en IOException de run de Control");
				e.printStackTrace();
				bConectado = false;
			}

		}
	}

}
