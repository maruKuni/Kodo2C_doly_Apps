package maru.test;
import java.io.*;
import com.fazecast.jSerialComm.*;
public class BPMTest {

	public static void main(String[] args) throws IOException {
		SerialPort port = SerialPort.getCommPorts()[0];
		InputStream in;
		long currentTime = System.currentTimeMillis();
		port.openPort();
		port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		port.setBaudRate(115200);
		in = port.getInputStream();
		while(true) {
			char inData = (char)in.read();
			if(inData == 'B' && System.currentTimeMillis() - currentTime > 10000) {
				while(true) {
					inData = (char)in.read();
					System.out.print(inData);
					if(inData == '\n') {
						currentTime = System.currentTimeMillis();
						break;
					}
				}
			}
		}
	}

}
