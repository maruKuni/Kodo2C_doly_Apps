package maru.apps;
import java.io.InputStream;
import java.util.*;
import javafx.scene.image.*;
public class Figure implements Comparable<Figure>{
	private Image image;
	ArrayList<Double> pNNs;
	public Figure(InputStream input) {
		image = new Image(input);
		pNNs = new ArrayList<Double>();
	}
	public void addPNN(double pNN) {
		pNNs.add(pNN);
	}
	public double getPNN() {
		return pNNs.stream()
			.mapToDouble(pNN -> pNN.doubleValue())
			.average()
			.getAsDouble();
	}
	public Image getImage() {
		return image;
	}
	
	@Override
	public int compareTo(Figure o) {
		return (int)((this.getPNN() - o.getPNN())*100.0);
	}
}
