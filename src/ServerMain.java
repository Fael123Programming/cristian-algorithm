import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ServerMain {
    public static final int SOCKET_PORT = 9999;
    public static final String LOCK = "lock";

    public static void main(String[] args) {
        Chronometer c = new Chronometer();
        c.start();
        System.out.println(Thread.currentThread().getName());
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName());
            try (
                DatagramSocket socket = new DatagramSocket(SOCKET_PORT);
            ) {
                while (true) {
                    synchronized (LOCK) {
                        System.out.println("Waiting for sync requests...");
                        DatagramPacket inputPacket = new DatagramPacket(new byte[1], 1);
                        socket.receive(inputPacket);
                        System.out.printf("Sync packet received from %s:%d\n",
                                inputPacket.getAddress().getHostAddress(),
                                inputPacket.getPort()
                        );
                        long curr = c.getCurrent();
                        byte[] dataBuffer = ByteUtils.longToBytes(curr);
                        System.out.printf(
                                "Sending packet (%d bytes) to %s:%d -> current is %d\n",
                                dataBuffer.length,
                                inputPacket.getAddress().getHostAddress(),
                                inputPacket.getPort(),
                                curr
                        );
                        DatagramPacket outputPacket = new DatagramPacket(
                                dataBuffer,
                                dataBuffer.length,
                                inputPacket.getAddress(),
                                inputPacket.getPort()
                        );
                        socket.send(outputPacket);
                    }
                }
            } catch (SocketException e) {
                System.out.println("SocketException caught with message: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("IOException caught with message: " + e.getMessage());
            }
        }).start();
        String text;
        while (true) {
            text = JOptionPane.showInputDialog("To reset the chronometer type a number (ms) below and hit OK");
            try {
                c.setCurrent(Long.parseLong(text));
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
