import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Control implements Runnable {
    private Socket socket;
    DataInputStream din;

    public Control(Socket socket) {
	this.socket = socket;
    }

    @Override
    public void run() {
	while (true) {
	    try {
		    din = new DataInputStream(this.socket.getInputStream());
		    String sMensajeRecibido = din.readUTF();
		    System.out.println("client says: " + sMensajeRecibido);
		} catch (IOException e) {
		    e.printStackTrace();
		}

	}
    }

}
