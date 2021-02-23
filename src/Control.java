import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class Control {

    public Semaphore semaServer;
    public Semaphore semaCliente;
    private Socket socket;
    
    public Control () {
	semaServer = new Semaphore(0);
	semaCliente = new Semaphore(0);
    }

    public Control(Socket socket, Class clase) {
	semaServer = new Semaphore(0);
	semaCliente = new Semaphore(0);
	this.socket = socket;
	if (clase.getName().equals(Servidor.class.getName())) {
	    new Thread(new LectorServer()).start();
	} else {
		new Thread(new LectorCliente()).start();
	}
    }

    public void restarSemaforoServer() {
	try {
	    control.semaServer.acquire();
	} catch (Exception ex) {
	    System.err.println(ex.getMessage());
	}
    }

    public void sumarSemaforoServer() {
	control.semaServer.release();
    }
    
    public void restarSemaforoCliente() {
   	try {
   	    control.semaCliente.acquire();
   	} catch (Exception ex) {
   	    System.err.println(ex.getMessage());
   	}
       }

       public void sumarSemaforoCliente() {
   	control.semaCliente.release();
       }

    public static Control control;

    public class LectorServer implements Runnable {
	DataInputStream din = null;

	@Override
	public void run() {
	    while (true) {

		try {
		    din = new DataInputStream(socket.getInputStream());
		    control.restarSemaforoServer();
		    System.out.println("### Cliente ha dicho: " + din.readUTF());

		} catch (IOException e) {
		    System.err.println(e.getMessage());
		}
	    }

	}

    }
    
    public class LectorCliente implements Runnable {
	DataInputStream din = null;

	@Override
	public void run () {
	    while (true) {

		try {
		    din = new DataInputStream(socket.getInputStream());
		    control.restarSemaforoCliente();
		    System.out.println("Servidor ha dicho: " + din.readUTF());

		} catch (IOException e) {
		    System.err.println(e.getMessage());
		}
	    }
	    
	}
    }

}
