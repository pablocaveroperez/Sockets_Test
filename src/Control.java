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
    private DataInputStream din;
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
		Servidor.shareToAll(din.readUTF(), objStream);

	    } catch (IOException e) {
		Servidor.clienteStream.remove(objStream);
		System.out.println("Se ha expulsado a un racista");
		bConectado = false;
	    }

	}
    }

}
