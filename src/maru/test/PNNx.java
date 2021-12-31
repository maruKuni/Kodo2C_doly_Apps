package maru.test;

public class PNNx {
	private int[] IBIBuffer;
	private int numBuffer;
	private int maxBuf;
	private int x;
	
	public PNNx(int x, int maxBuf) {
		this.x = x;
		this.maxBuf = maxBuf;
		IBIBuffer = new int[this.maxBuf];
		numBuffer = 0;
	}
	public boolean isPNNavailable() {
		return (numBuffer < maxBuf);
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
		assert (numBuffer == maxBuf);
		int count = 0;
		for(int i = 0; i < IBIBuffer.length - 1; i++) {
			if(Math.abs(IBIBuffer[i] - IBIBuffer[i + 1]) > 10*x) {
				count++;
			}
		}
		return (double)count / (double)maxBuf;
	}
}
