import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Control implements Runnable, Serializable {
    private static Socket socket;
    public static Integer iContadorClientes = 0;
    DataInputStream din;
    private static Semaphore semaControl = new Semaphore(1);
    public static List<ControlCliente> lClientes = new ArrayList<ControlCliente>();

    public Control(Socket socket) {
	this.socket = socket;
    }

    public static void cogerAtencion(Socket socket) {
	try {
	    semaControl.acquire();
	    OutputStream outputStream = socket.getOutputStream();
	    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
	    objectOutputStream.writeObject(lClientes);
	} catch (Exception ex) {
	    System.err.println(ex.getMessage());
	}
    }
    
    public static void soltarAtencion(List<ControlCliente> lista) {
	lClientes = lista;
	semaControl.release();
    }

    @Override
    public void run() {
	while (true) {
	    try {
		din = new DataInputStream(this.socket.getInputStream());
		String sMensajeRecibido = din.readUTF();
		System.out.println("El mensaje es = " + sMensajeRecibido);
		System.out.println("El tamaño de la list ade cliente es: " + lClientes.size());
		for (ControlCliente c : lClientes) {
		    c.sumarSemaforo(sMensajeRecibido);
		}
//		
//		OutputStream outputStream = socket.getOutputStream();
//		// create an object output stream from the output stream so we can send an
//		// object through it
//		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
//		objectOutputStream.writeObject(sMensajeRecibido);
		// System.out.println("client says: " + sMensajeRecibido);
	    } catch (IOException e) {
		e.printStackTrace();
	    }

	}
    }

}

/**
 * Clase que recoge los mensajes del servidor y los imprime en cada cliente
 * 
 * @author pablo
 *
 */
class ControlCliente implements Runnable, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public Integer id;
    private String sMensaje;
    private Semaphore semaforo;

    public void sumarSemaforo(String sMensaje) {
	this.sMensaje = sMensaje;
	semaforo.release();
    }

    public void restarSemaforo() {
	try {
	    semaforo.acquire();
	} catch (Exception ex) {
	    System.err.println(ex.getMessage());
	}
    }

    public ControlCliente(Integer id) {
	this.id = id;
	this.semaforo = new Semaphore(0);
    }

    @Override
    public void run() {
	while (true) {
	    restarSemaforo();
	    System.out.println("cliente: " + this.id + " dice: " + this.sMensaje);
	}
    }
}
