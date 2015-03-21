package application;

import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class MapView extends Pane
{
	//the image of the map
	private ImageView mapImage;
	
	private SkylinesSelectionShape selectionShape;
	
	public MapView()
	{
		mapImage=new ImageView();
		this.getChildren().add(mapImage);
		this.setOnMouseMoved(new ShapeMover()); //TODO rename
	}
	
	public void setImage(Map map,WritableImage fxImage)
	{
		this.mapImage.setImage(fxImage);
		this.setWidth(fxImage.getWidth());
		this.setHeight(fxImage.getHeight());
		selectionShape=new SkylinesSelectionShape(map,fxImage.getWidth(),fxImage.getHeight());	
		
		this.getChildren().clear();
		this.getChildren().addAll(mapImage,selectionShape);
	}
	
	private class ShapeMover implements EventHandler<MouseEvent>
	{
		@Override
		public void handle(MouseEvent e)
		{
			selectionShape.setLayoutX(e.getX());
			selectionShape.setLayoutY(e.getY());
		}
	}
}
