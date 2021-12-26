package maru.test;
import com.fazecast.jSerialComm.*;
public class SerialTest {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		SerialPort[] ports = SerialPort.getCommPorts();
		System.out.println(ports);
	}

}
