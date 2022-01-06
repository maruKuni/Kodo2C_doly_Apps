package maru.apps;
import java.io.*;
import com.fazecast.jSerialComm.*;
public class PNN50 implements Runnable{
	private int[] IBIBuffer;
	private int numBuffer;
	private static final int maxBuf = 30;
	private static final int x = 50;
	private SerialPort port;
	private InputStream in;
	public PNN50() {
		IBIBuffer = new int[maxBuf];
		numBuffer = 0;
		port = SerialPort.getCommPorts()[0];
		port.openPort();
		port.setBaudRate(115200);
		port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		in = port.getInputStream();
	}
	public boolean isPNNavailable() {
		return (numBuffer >= maxBuf);
	}
	public void addIBI(int IBI) {
		if(numBuffer < maxBuf) {
			IBIBuffer[numBuffer++] = IBI;
		}else {
			for(int i = 0; i < IBIBuffer.length - 1; i++) {
				IBIBuffer[i] = IBIBuffer[i + 1];
			}
			IBIBuffer[IBIBuffer.length - 1] = IBI;
		}
	}
	double getPNNx() {
		assert (isPNNavailable());
		int count = 0;
		for(int i = 0; i < IBIBuffer.length - 1; i++) {
			if(Math.abs(IBIBuffer[i] - IBIBuffer[i + 1]) > 10*x) {
				count++;
			}
		}
		return (double)count / (double)maxBuf;
	}
	
	@Override
	public void run() {
		char inData;
		while(true) {
			try {
				// 読み込み
				inData = (char)in.read();
				//頭文字'Q'はIBIを意味する
				if(inData == 'Q') {
					int tmp = 0;
					while((inData = (char)in.read()) != '\n') {
						tmp += 10*tmp + (inData - '0');
					}
					addIBI(tmp);
					
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
