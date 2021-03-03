import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Cliente {

	private static final String HOST = "localhost";

	public static void main(String[] args) {

		DataOutputStream dout = null;
		BufferedReader br = null;
		Socket socket = null;
		int idCliente = Servidor.iContador++;

		try {
			System.out.println("El cliente se va a conectar");
			socket = new Socket(HOST, Servidor.PORT);
			System.out.println(socket.getTrafficClass());
			System.out.println(socket.getInetAddress());
			new Thread(new Servidor.ControlCliente(socket)).start();
			dout = new DataOutputStream(socket.getOutputStream());
			br = new BufferedReader(new InputStreamReader(System.in));
			String sMensajeEnviado = "";
			while (!sMensajeEnviado.equals("gitano")) {
				sMensajeEnviado = br.readLine();
				dout.writeUTF("# " + idCliente + " : " + sMensajeEnviado);
			}

		} catch (Exception ex) {
			try {
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
