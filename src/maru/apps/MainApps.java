package maru.apps;

import java.util.*;
import java.io.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.text.*;

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
	private Label title, description;
	private Stage stage;
	private final String descriptionText  = 
			"※FF値\npNN50の平均値により算出. \n1に近いほどその顔が好きであることを表している．";
	private static final Comparator<Figure> orderFigures = (f1, f2) -> {
		if (f1.getPNN() < f2.getPNN()) {
			return -1;
		} else if (f1.getPNN() > f2.getPNN()) {
			return 1;
		} else {
			return 0;
		}
	};

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		this.stage = stage;
		title = new Label("Favorite Face");
		title.setFont(Font.font("Times New Roman", FontWeight.NORMAL, FontPosture.ITALIC,50));
		description = new Label(descriptionText);
		description.setWrapText(true);
		BorderPane.setAlignment(description, Pos.BOTTOM_RIGHT);
		VBox vertical = new VBox(10);
		bp = new BorderPane();
		view = new ImageView();
		figure = new ArrayList<Figure>();
		initList();
		pnn = new PNN50();
		(new Thread(pnn)).start();
		startButton = new Button("計測開始");
		startButton.setOnAction(e -> processing(e));
		startButton.setDisable(true);
		vertical.getChildren().addAll(title, startButton);
		vertical.setAlignment(Pos.CENTER);
		bp.setCenter(vertical);
		bp.setBottom(description);
		scene = new Scene(bp);
		stage.setScene(scene);
		stage.setWidth(1280);
		stage.setHeight(720);
		stage.setTitle("Favorite Face");
		stage.show();
		stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, e -> {

			pnn.close();
			System.exit(0);
		});
		Timer timer = new Timer(false);
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				if (pnn.isPNNavailable()) {
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
		Collections.shuffle(figure, new Random(System.currentTimeMillis()));
	}

	private void processing(ActionEvent e) {
		bp.getChildren().clear();
		stage.setTitle("Favorite Face - 計測中...");
		bp.setCenter(view);
		new Thread(() -> {
			for (Figure f : figure) {
				Platform.runLater(() -> view.setImage(f.getImage()));
				for (int i = 0; i < 50; i++) {
					f.addPNN(pnn.getPNNx());
					try {
						Thread.sleep(200);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
			BorderPane result = new BorderPane();
			FlowPane images = new FlowPane();
			images.setAlignment(javafx.geometry.Pos.CENTER);
			images.setVgap(10);
			int i = 1;
			for (Object fig : figure.stream()
					.sorted(orderFigures.reversed())
					.limit(5).toArray()) {
				ImageView tmp = new ImageView(((Figure) fig).getImage());
				BorderPane tmpBP = new BorderPane();
				tmp.setFitWidth(320);
				tmp.setFitHeight(300);
				tmpBP.setTop(new Label(i++ +"位 FF値:" + ((Figure)fig).getPNN()));
				tmpBP.setCenter(tmp);
				images.getChildren().add(tmpBP);
			}
			Platform.runLater(() -> stage.setTitle("Favorite Face - 計測終了"));
			Label top5 = new Label("あなたの好きな顔 Top 5");
			BorderPane.setAlignment(top5, Pos.TOP_CENTER);
			top5.setFont(new Font("Yu Gothic UI Bold",20));
			result.setTop(top5);
			result.setCenter(images);
			scene.setRoot(result);
		}).start();
		;

	}

}
