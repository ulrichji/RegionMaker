package properties;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class OceanGenerationPropertiesController
{
	@FXML TextField tf_seaLevel;
	@FXML TextField tf_oceanAngle;
	
	public void initialize()
	{
		tf_seaLevel.setText(Double.toString(Properties.getSeaLevel()));
		tf_oceanAngle.setText(Double.toString(Properties.getOceanAngle()));
	}
}
