package properties;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import application.Main;

public class MapViewPropertiesController
{
	@FXML private TextField tf_imageWidth;
	@FXML private TextField tf_imageHeight;
	
	private Properties propertiesToEdit;
	private PropertiesWindowController propWindow;
	
	public MapViewPropertiesController(Properties tempProperties,PropertiesWindowController propWindow)
	{
		this.propertiesToEdit=tempProperties;
		this.propWindow=propWindow;
	}

	public void initialize()
	{
		tf_imageWidth.setText(Double.toString(Main.getProperties().getMapViewWidth()));
		tf_imageHeight.setText(Double.toString(Main.getProperties().getMapViewHeight()));
		
		tf_imageWidth.setOnKeyReleased(new ImageWidthListener());
		tf_imageHeight.setOnKeyReleased(new ImageHeightListener());
	}
	
	private class ImageWidthListener implements EventHandler<KeyEvent>
	{

		@Override
		public void handle(KeyEvent e)
		{
			try
			{
				double value=Double.parseDouble(tf_imageWidth.getText().replace(",","."));
				
				if(value<=0)
				{
					propWindow.errorLog("The image width cannot be negative");
					tf_imageWidth.setStyle("-fx-background-color:orange");
				}
				//The value is ok.
				else
				{
					if(value < 2048)
					{
						tf_imageWidth.setStyle("-fx-background-color:white");
						propWindow.clearLog();
					}
					else
					{
						tf_imageWidth.setStyle("-fx-background-color:gray");
						propWindow.printLog("Please be careful with large image sizes, it may consume a lot of memory");
					}
					propertiesToEdit.setMapViewWidth(value);
				}
			}catch(NumberFormatException ee)
			{
				propWindow.errorLog("The width of the image is not valid");
				tf_imageWidth.setStyle("-fx-background-color:orange");
			}
		}
		
	}
	
	private class ImageHeightListener implements EventHandler<KeyEvent>
	{
		@Override
		public void handle(KeyEvent e)
		{
			try
			{
				double value=Double.parseDouble(tf_imageHeight.getText().replace(",","."));
				
				if(value<=0)
				{
					propWindow.errorLog("The image height cannot be negative");
					tf_imageHeight.setStyle("-fx-background-color:orange");
				}
				//The value is ok.
				else
				{
					if(value < 2048)
					{
						tf_imageHeight.setStyle("-fx-background-color:white");
						propWindow.clearLog();
					}
					else
					{
						tf_imageHeight.setStyle("-fx-background-color:gray");
						propWindow.printLog("Please be careful with large image sizes, it may consume a lot of memory");
					}
					propertiesToEdit.setMapViewHeight(value);
				}
			}catch(NumberFormatException ee)
			{
				propWindow.errorLog("The image height is not valid.");
				tf_imageWidth.setStyle("-fx-background-color:orange");
			}
		}
	}
}
