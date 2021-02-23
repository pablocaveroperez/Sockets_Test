import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class Control {

    public Semaphore semaServer;
    // public static Semaphore semaCliente;
    private Socket socket;
    private Class clase;

    public Control(Socket socket, Class clase) {
	semaServer = new Semaphore(1);
	this.socket = socket;
	this.clase = clase;
	new Thread(new Lector()).start();
    }

    public void restarSemaforo() {
	try {
	    semaServer.acquire();
	} catch (Exception ex) {
	    System.err.println(ex.getMessage());
	}
    }

    public void sumarSemaforo() {
	semaServer.release();
    }

    public void controlSemaforo() {
	System.out.println("Available: " + semaServer.availablePermits());
    }

    public class Lector implements Runnable {
	DataInputStream din = null;

	@Override
	public void run() {
	    while (true) {

		try {
		    din = new DataInputStream(socket.getInputStream());
		    System.out.println(clase.getName() + " dice: " + din.readUTF());
		} catch (IOException e) {
		    System.err.println(e.getMessage());
		}
	    }

	}

    }

}
