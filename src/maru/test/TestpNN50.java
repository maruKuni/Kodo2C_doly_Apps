package maru.test;
import java.io.*;
import com.fazecast.jSerialComm.*;
public class TestpNN50 {
	private InputStream in;/* 入力用のストリーム */
	private SerialPort port;/* 入力用のポート */
	private int[] IBIBuffer;/* 心拍変動を記録する配列 */
	private int numBuffer;/* 記録した心拍変動の数 */
	public static void main(String[] args) {
		new TestpNN50().start();
	}
	/*コンストラクタ 初期化はここで*/
	TestpNN50(){
		port = SerialPort.getCommPorts()[0];
		if(!port.openPort()) {
			System.err.println("port cannot open");
			System.exit(1);
		}
		port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		port.setBaudRate(115200);
		in = port.getInputStream();
		numBuffer = 0;
		IBIBuffer = new int[31];
	}
	/*メインフロー*/
	void start() {
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
					if(numBuffer < 31) {
						IBIBuffer[numBuffer++] = tmp;
					}else {
						int count = 0;
						for(int i = 0; i < IBIBuffer.length - 1; i++) {
							IBIBuffer[i] = IBIBuffer[i + 1];
						}
						IBIBuffer[30] = tmp;
						for(int i = 0; i < IBIBuffer.length - 1; i++) {
							if(Math.abs(IBIBuffer[i] - IBIBuffer[i + 1]) > 500) {
								count++;
							}
						}
						System.out.println("pNN50:" + (double)count / 30);
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
