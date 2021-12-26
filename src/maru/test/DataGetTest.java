package maru.test;
import com.fazecast.jSerialComm.*;
import java.io.*;
public class DataGetTest {
	InputStream in;
	SerialPort port;
	public static void main(String[] args) {
		new DataGetTest().start();
	}
	public DataGetTest() {
		port = SerialPort.getCommPort("COM4");
		System.out.println();
		if(!port.openPort()) {
			System.out.println("error");
			System.exit(1);
		}
		port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		port.setBaudRate(115200);
		in = port.getInputStream();
	}
	void start() {
		while(true) {
			try {
				char input = (char)in.read();
				if(input == 'Q') {
					while(true) {
						input = (char)in.read();
						System.out.print(input);
						if(input == '\n') {
							break;
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
