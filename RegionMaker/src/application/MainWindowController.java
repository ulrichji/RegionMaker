package application;

import java.awt.image.BufferedImage;
import java.io.File;

import properties.Properties;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
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
	
	//We want to keep track of the stage this controller is running on so we can modify it from the controller
	private Stage associatedStage;
	
	//Variables associated with tasks have to be declared outside local scope
	MapLoadTask load_task;
	ProgressBarDialog progress_dialog;
	
	public MainWindowController(Stage associatedStage) {
		this.associatedStage = associatedStage;
	}

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
	
	@FXML private void close() {
		associatedStage.close();
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
            //At this point the map is not necessarily loaded, refer to eventhandler in openFile()
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
		double selectionX=(selection.getX()/(int)Main.getProperties().getMapViewWidth())*(map.getRealWidth());
		double selectionY=(selection.getY()/(int)Main.getProperties().getMapViewHeight())*(map.getRealHeight());
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
		map.generateOceans(Main.getProperties().getSeaLevel());//FIXME should be possible for user to specify the height of the ocean.
		//refresh the image.
		setImage(map.getDrawableMap((int)Main.getProperties().getMapViewWidth(),(int)Main.getProperties().getMapViewHeight()));
	}
	
	@FXML public void openPropertiesWindow()
	{
		Properties.showPropertiesWindow();
	}
	
	public void openFile(String path)
	{
		//Initialize an asynchronous map-load task
		load_task = new MapLoadTask(path);
		Thread load_thread = new Thread(load_task);
		
		//Create a progressbar to keep track of load progress and block actions during load
		progress_dialog = new ProgressBarDialog(load_task, associatedStage);
		
		//Handle load success event
		load_task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				//Get the result of the load task
				map = load_task.getValue();
				
				//Update the image in the view
				setImage(map.getDrawableMap((int)Main.getProperties().getMapViewWidth(),(int)Main.getProperties().getMapViewHeight()));
				mapView.setSelectionShape(new SkylinesSelectionShape(map,(int)Main.getProperties().getMapViewWidth(),(int)Main.getProperties().getMapViewHeight()));
				
				//Close the progressbar and enable export
				progress_dialog.close();
	            mi_export.setDisable(false); //TODO Bind a mapLoaded property instead?
			}
		});
		
		
		//Handle failed to load event
		load_task.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				progress_dialog.close();
				System.err.println("Failed to load .dem file");
				//TODO Inform user of failure
			}
		});
		
		//Show progressbar and start task
		progress_dialog.show();
		load_thread.start();
	}
	
	private void setImage(BufferedImage img)
	{
		//create a new image with the dimensions. These dimensions does not represent the dimensions of the map
		WritableImage fxImage=new WritableImage((int)Main.getProperties().getMapViewWidth(),(int)Main.getProperties().getMapViewHeight());
		fxImage=SwingFXUtils.toFXImage(img,fxImage);
		//update the image in the view.
		mapView.setImage(fxImage);
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
