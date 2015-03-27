package properties;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import application.Main;

public class OceanGenerationPropertiesController
{
	@FXML private TextField tf_seaLevel;
	@FXML private TextField tf_oceanAngle;
	
	private Properties propertiesToEdit;
	private PropertiesWindowController propWindow;
	
	public OceanGenerationPropertiesController(Properties tempProperties,PropertiesWindowController propWindow)
	{
		this.propertiesToEdit=tempProperties;
		this.propWindow=propWindow;
	}

	public void initialize()
	{
		tf_seaLevel.setText(Double.toString(Main.getProperties().getSeaLevel()));
		tf_oceanAngle.setText(Double.toString(Main.getProperties().getOceanAngle()));
		
		tf_seaLevel.setOnKeyReleased(new SeaLevelListener());
		tf_oceanAngle.setOnKeyReleased(new OceanAngleListener());
	}
	
	private class SeaLevelListener implements EventHandler<KeyEvent>
	{

		@Override
		public void handle(KeyEvent e)
		{
			try
			{
				double value=Double.parseDouble(tf_seaLevel.getText().replace(",","."));
					
				tf_seaLevel.setStyle("-fx_background-color:white");
				propertiesToEdit.setSeaLevel(value);
				propWindow.clearLog();
				
			}catch(NumberFormatException ee)
			{
				propWindow.errorLog("The sealevel is not valid.");
				tf_seaLevel.setStyle("-fx-background-color:orange");
			}
		}
		
	}
	
	private class OceanAngleListener implements EventHandler<KeyEvent>
	{
		@Override
		public void handle(KeyEvent e)
		{
			try
			{
				double value=Double.parseDouble(tf_oceanAngle.getText().replace(",","."));
				
				if(value<=0 || value>=90)
				{
					propWindow.errorLog("Angle must be between 0 and 90 degrees");
					tf_oceanAngle.setStyle("-fx-background-color:orange");
				}
				//The value is ok.
				else
				{
					tf_oceanAngle.setStyle("-fx_background-color:white");
					propertiesToEdit.setOceanAngle(value);
					propWindow.clearLog();
				}
			}catch(NumberFormatException ee)
			{
				propWindow.errorLog("The ocean angle is not valid.");
				tf_oceanAngle.setStyle("-fx-background-color:orange");
			}
		}
	}
}
