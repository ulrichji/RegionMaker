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
		//Create a new stage
		Stage stage=new Stage();
		//create the file chooser
		FileChooser fc_openFile=new FileChooser();
		fc_openFile.setTitle("Open .dem file");
		//Wait for the filechooser to find the file
		File file = fc_openFile.showOpenDialog(stage);
		//if there is a file, try to open it.
        if (file != null) {
            openFile(file.getAbsolutePath());
        }

	}
	
	public void openFile(String path)
	{
		//The map to load in
		map=new Map();
		try
		{
			//load the map from the given path
			map.loadMap(path);
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
