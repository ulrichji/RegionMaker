package application;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainWindowController
{
	@FXML private void openFile()
	{
		Stage stage=new Stage();
		FileChooser fc_openFile=new FileChooser();
		fc_openFile.setTitle("Open .dem file");
		fc_openFile.showOpenDialog(stage);
		System.out.println("done");
		//stage.show();
	}
}
