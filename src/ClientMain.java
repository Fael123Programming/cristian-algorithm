import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ClientMain {
    public static void main(String[] args) {
        Chronometer c = new Chronometer();
        c.start();
        long ts0, ts1;
        try ( DatagramSocket socket = new DatagramSocket() ) {
            while (true) {
                JOptionPane.showMessageDialog(null, "Press OK to synchronize");
                System.out.println("Requesting time from server...");
                ts0 = System.currentTimeMillis();
                DatagramPacket outputPacket = new DatagramPacket(
                        new byte[1],
                        1,
                        InetAddress.getByName("localhost"),
                        ServerMain.SOCKET_PORT
                );
                socket.send(outputPacket);
                byte[] data = new byte[Long.BYTES];
                DatagramPacket inputPacket = new DatagramPacket(
                        data,
                        data.length
                );
                socket.receive(inputPacket);
                long serverTime = ByteUtils.bytesToLong(inputPacket.getData());
                ts1 = System.currentTimeMillis();
                c.setCurrent(serverTime + ((long) ((ts1 - ts0) / 2.0)));
            }
        } catch (SocketException e) {
            System.out.println("SocketException caught with message: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException caught with message: " + e.getMessage());
        }
    }
}
