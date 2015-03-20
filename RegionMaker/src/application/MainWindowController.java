package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class MainWindowController
{
	private Map map;
	
	@FXML private ImageView iv_map;
	
	@FXML private void openFile()
	{
		//Create a new stage
		Stage stage=new Stage();
		//create the file chooser
		FileChooser fc_openFile=new FileChooser();
		fc_openFile.setTitle("Open .dem file");
		fc_openFile.getExtensionFilters().add(new ExtensionFilter("DEM","*.dem"));
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
		BufferedImage img=map.getDrawableMap(1024,1024);
		WritableImage fxImage=new WritableImage(1024,1024);
		fxImage=SwingFXUtils.toFXImage(img,fxImage);
		iv_map.setImage(fxImage);
	}
}
