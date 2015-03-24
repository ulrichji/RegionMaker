package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class MainWindowController
{
	private Map map;
	
	@FXML private CheckMenuItem cmi_skylines;
	@FXML private CheckMenuItem cmi_sc4;
	@FXML private AnchorPane ap_map;
	@FXML private ScrollPane sp_scrollPane;
	@FXML private MenuItem mi_export;
	
	//gamestate is used to select what game to export to.
	private GameState gameState;
	private MapView mapView;
	
	public void initialize()
	{
		selectSkylines();
		mapView=new MapView();
		ap_map.getChildren().add(mapView);
		//want the anchorpane in the scrollview to resize to the map
		mapView.widthProperty().addListener(new WidthListener(ap_map));
		mapView.heightProperty().addListener(new HeightListener(ap_map));
		
		//wait for the map to load in order to enable exporting.
		mi_export.setDisable(true);
	}
	
	@FXML private void selectSkylines()
	{
		cmi_skylines.setSelected(true);
		cmi_sc4.setSelected(false);
		gameState=GameState.SKYLINES;
	}
	
	@FXML private void selectSC4()
	{
		cmi_skylines.setSelected(false);
		cmi_sc4.setSelected(true);
		gameState=GameState.SC4;
	}
	
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
            //map is now loaded. We can now enable exporting.
            mi_export.setDisable(false);
        }
	}
	
	@FXML private void export()
	{
		//get a rectangle of where the figure is in pixel on screen and width in meters.
		SelectionRectangle selection=mapView.getSelection();
		//the width and height of the rectangle in meters.
		double selectionSizeX=selection.getWidth();
		double selectionSizeY=selection.getHeight();
		//the x and y position of the selected area in meters.
		double selectionX=(selection.getX()/1024)*(map.getRealWidth());
		double selectionY=(selection.getY()/1024)*(map.getRealHeight());
		//create a new Exporter object.
		HeightmapExporter exporter=null;
		switch(gameState)
		{
		case SKYLINES:
			exporter=new SkylinesExporter(map,
				new SelectionRectangle(selectionX,selectionY,selectionSizeX,selectionSizeY));
			break;
		
		case SC4:
			//TODO implement sc4 exporter
			break;
		}
		
		//Use the objects filechooser to select the file.
		exporter.selectFile();
		//export the file.
		exporter.export();
	}
	
	//generate the oceans. This should be improved.
	@FXML public void generateOceans()
	{
		map.generateOceans(4);//FIXME should be possible for user to specify the height of the ocean.
		//refresh the image.
		setImage(map.getDrawableMap(1024,1024));
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
		//update the image in the view.
		setImage(map.getDrawableMap(1024,1024));
	}
	
	private void setImage(BufferedImage img)
	{
		//create a new image with the dimensions. These dimensions does not represent the dimensions of the map
		WritableImage fxImage=new WritableImage(1024,1024);
		fxImage=SwingFXUtils.toFXImage(img,fxImage);
		//update the image in the view.
		mapView.setImage(map,fxImage);
	}
	
	private class WidthListener implements ChangeListener<Number>
	{
		//want a resizable component to edit.
		Region resizeable;
		public WidthListener(Region region)
		{
			this.resizeable=region;
		}

		@Override
		public void changed(ObservableValue<? extends Number> arg0,Number oldValue,Number newValue)
		{
			resizeable.setPrefWidth(newValue.doubleValue());
		}
	}
	private class HeightListener implements ChangeListener<Number>
	{
		//want a resizable component to edit.
		Region resizeable;
		public HeightListener(Region region)
		{
			this.resizeable=region;
		}
		@Override
		public void changed(ObservableValue<? extends Number> arg0,Number oldValue,Number newValue)
		{
			resizeable.setPrefHeight(newValue.doubleValue());
		}
	}
}
