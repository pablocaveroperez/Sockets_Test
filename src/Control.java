import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Control implements Runnable, Serializable {
    private static Socket socket;
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
	while (true) {
	    try {
		din = new DataInputStream(this.socket.getInputStream());
		String sMensajeRecibido = din.readUTF();
		Servidor.shareToAll(sMensajeRecibido, objStream);
	    } catch (IOException e) {
		e.printStackTrace();
	    }

	}
    }

}

