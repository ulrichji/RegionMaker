package application;
	
import properties.Properties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {
	
	private static Properties properties;
	
	public static Properties getProperties()
	{
		return properties;
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader=new FXMLLoader();
			loader.setController(new MainWindowController(primaryStage));
			AnchorPane root=(AnchorPane)loader.load(this.getClass().getResourceAsStream("window.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			
			properties=new Properties();
			properties.loadProperties();
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public static void setProperties(Properties tempProperties)
	{
		properties=tempProperties;
	}
}
