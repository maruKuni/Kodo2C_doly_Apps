package maru.apps;

import java.util.*;
import java.awt.desktop.ScreenSleepEvent;
import java.io.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.*;
import javafx.util.Duration;
import javafx.event.*;
import javafx.animation.*;
/** 実行用クラス
 * ウィンドウ表示とメインフロー
 * @author KunimaruYuta
 * 
 * */
public class MainApps extends Application {
	private ImageView view;
	private ArrayList<Figure> figure;
	private Button startButton;
	private BorderPane bp;
	private PNN50 pnn;
	private Scene scene;
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		bp = new BorderPane();
		view = new ImageView();
		figure = new ArrayList<Figure>();
		initList();
		pnn = new PNN50();
		(new Thread(pnn)).start();
		startButton = new Button("計測開始");
		startButton.setOnAction(e -> processing(e));
		startButton.setDisable(true);
		bp.setCenter(startButton);
		scene = new Scene(bp);
		stage.setScene(scene);
		stage.setWidth(1280);
		stage.setHeight(720);
		stage.show();
		stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, e -> System.exit(0));
		Timer timer = new Timer(false);
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				if(pnn.isPNNavailable()) {
					startButton.setDisable(false);
					timer.cancel();
				}
			}
		};
		timer.schedule(task, 0, 100);
	}

	private void initList() {
		for (File file : new File("img/").listFiles()) {
			try {
				figure.add(new Figure(new FileInputStream(file)));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	private void processing(ActionEvent e) {
		bp.setCenter(view);
		new Thread(() ->{
			for(Figure f:figure) {
				Platform.runLater(() -> view.setImage(f.getImage()));
				for(int i = 0;i < 10; i++) {
					f.addPNN(pnn.getPNNx());
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
			
		}).start();;
		
	}
	
}
