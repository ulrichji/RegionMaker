package application;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainWindowController
{
	private Map map;
	
	@FXML private void openFile()
	{
		Stage stage=new Stage();
		FileChooser fc_openFile=new FileChooser();
		fc_openFile.setTitle("Open .dem file");
		File file = fc_openFile.showOpenDialog(stage);
        if (file != null) {
            openFile(file.getAbsolutePath());
        }

	}
	
	public void openFile(String path)
	{
		map=new Map();
		try
		{
			map.loadMap(path);
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
