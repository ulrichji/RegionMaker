package application;

import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;

public class MapView extends Pane
{
	private ImageView mapImage;
	
	public MapView()
	{
		mapImage=new ImageView();
		this.getChildren().add(mapImage);
	}
	
	public void setImage(WritableImage fxImage)
	{
		this.mapImage.setImage(fxImage);
		this.setWidth(fxImage.getWidth());
		this.setHeight(fxImage.getHeight());
	}
	
}
