package maru.test;
import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.*;
import javafx.scene.layout.*;
import java.util.*;
import java.io.*;

/**/
public class GUITest extends Application{
	Button btn;
	ArrayList<Image> figureList;
	Image[] figures;
	ImageView view;
	Iterator<Image> iterator;
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage stage) throws Exception {
		try {
			Scene scene;
			BorderPane bp = new BorderPane();
			view = new ImageView();
			view.setFitHeight(360);
			view.setFitWidth(500);
			figureList = new ArrayList<Image>();
			Arrays.asList(new File("img/").listFiles()).stream()
			.map(file -> {
				try {
					return new FileInputStream(file);
				} catch (FileNotFoundException e1) {
					return null;
				}
			})
			.map(stream -> new Image(stream))
			.forEach(image -> figureList.add(image));
			iterator = figureList.iterator();		
			btn = new Button("切り替え");
			btn.setOnAction(e -> nextImage(e));
			bp.setCenter(btn);
			bp.setBottom(view);
			scene = new Scene(bp);
			stage.setScene(scene);
			stage.setTitle("テスト");
			stage.setWidth(640);
			stage.setHeight(480);
			stage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void nextImage(ActionEvent e) {
		if(iterator.hasNext()) {
			view.setImage(iterator.next());
		}
	}
}
