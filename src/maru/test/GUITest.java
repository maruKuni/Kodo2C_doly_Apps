package maru.test;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.scene.layout.*;
/**/
public class GUITest extends Application{
	Button btn;
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage stage) throws Exception {
		Scene scene;
		BorderPane bp = new BorderPane();
		btn = new Button("計測開始");
		bp.setCenter(btn);
		scene = new Scene(bp);
		stage.setScene(scene);
		stage.setTitle("テスト");
		stage.setWidth(640);
		stage.setHeight(480);
		stage.show();
	}
}
