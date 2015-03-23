package application;

import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class MapView extends Pane
{
	//the image of the map
	private ImageView mapImage;
	
	private SkylinesSelectionShape selectionShape;
	private boolean moveMouse=true;
	
	private double imageResX=0,imageResY=0;
	private double squareX=0,squareY=0;
	public MapView()
	{
		mapImage=new ImageView();
		this.getChildren().add(mapImage);
		this.setOnMouseMoved(new ShapeMover()); //TODO rename
		this.setOnMouseClicked(new ClickListener());
	}
	
	public void setImage(Map map,WritableImage fxImage)
	{
		this.mapImage.setImage(fxImage);
		this.imageResX=fxImage.getWidth();
		this.imageResY=fxImage.getHeight();
		this.setWidth(fxImage.getWidth());
		this.setHeight(fxImage.getHeight());
		selectionShape=new SkylinesSelectionShape(map,fxImage.getWidth(),fxImage.getHeight());	
		
		this.getChildren().clear();
		this.getChildren().addAll(mapImage,selectionShape);
	}
	
	public double getImageResolutionX()
	{
		return imageResX;
	}
	
	public double getImageResolutionY()
	{
		return imageResY;
	}
	
	private class ClickListener implements EventHandler<MouseEvent>
	{
		@Override
		public void handle(MouseEvent e)
		{
			if(e.getButton() == MouseButton.PRIMARY)
			{
				if(moveMouse)
					moveMouse=false;
				else
					moveMouse=true;
			}
		}
		
	}
	
	private class ShapeMover implements EventHandler<MouseEvent>
	{
		@Override
		public void handle(MouseEvent e)
		{
			if(moveMouse)
			{
				selectionShape.setLayoutX(e.getX());
				selectionShape.setLayoutY(e.getY());
				squareX=e.getX();
				squareY=e.getY();
			}
		}
	}

	public SelectionRectangle getSelection()
	{
		return new SelectionRectangle(squareX,squareY,selectionShape.getWidth(),selectionShape.getHeight());
	}
}
