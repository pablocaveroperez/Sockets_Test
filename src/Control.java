import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class Control implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public Semaphore semaServer;
    public Semaphore semaCliente;
    private static Socket socketCliente;
    public static Socket socketServer;

    public Control() {
	semaServer = new Semaphore(0);
	semaCliente = new Semaphore(0);
    }

    public void addSocketCliente(Socket socket) {
	this.socketCliente = socket;
    }

    public void addSocketServer(Socket socket) {
	this.socketServer = socket;
    }


//    public Control(Socket socket, Class clase) {
//	semaServer = new Semaphore(0);
//	semaCliente = new Semaphore(0);
//	// this.socket = socket;
//	if (clase.getName().equals(Servidor.class.getName())) {
//	    new Thread(new LectorServer()).start();
//	} else {
//	    new Thread(new LectorCliente()).start();
//	}
//    }

    public void restarSemaforoServer() {
	try {
	    LectorServer.control.semaServer.acquire();
	} catch (Exception ex) {
	    System.err.println(ex.getMessage());
	}
    }

    public void sumarSemaforoServer() {
	semaServer.release();
    }

    public void restarSemaforoCliente() {
	try {
	    semaCliente.acquire();
	} catch (Exception ex) {
	    System.err.println(ex.getMessage());
	}
    }

    public void sumarSemaforoCliente() {
	semaCliente.release();
    }


    public static class LectorServer implements Runnable {
	DataInputStream din = null;
	public static Control control;
	
	public LectorServer(Control control) {
	    this.control = control;
	}

	@Override
	public void run() {
	    while (true) {

		try {
		    din = new DataInputStream(socketServer.getInputStream());
		    control.restarSemaforoServer();
		    System.out.println("### Cliente ha dicho: " + din.readUTF());

		} catch (IOException e) {
		    System.err.println(e.getMessage());
		}
	    }

	}

    }

    public static class LectorCliente implements Runnable {
	DataInputStream din = null;
	public static Control control;
	
	public LectorCliente(Control control) {
		this.control = control;
	}

	@Override
	public void run() {
	    while (true) {

		try {
		    din = new DataInputStream(socketCliente.getInputStream());
		    control.restarSemaforoCliente();
		    System.out.println("Servidor ha dicho: " + din.readUTF());

		} catch (IOException e) {
		    System.err.println(e.getMessage());
		}
	    }

	}
    }

}
