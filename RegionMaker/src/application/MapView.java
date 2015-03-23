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
		//the listener that will move the shape.
		this.setOnMouseMoved(new ShapeMover());
		//the listener used to lock the selection.
		this.setOnMouseClicked(new ClickListener());
	}
	
	public void setImage(Map map,WritableImage fxImage)
	{
		//Update the imageview.
		this.mapImage.setImage(fxImage);
		//set the interesting properties.
		this.imageResX=fxImage.getWidth();
		this.imageResY=fxImage.getHeight();
		this.setWidth(fxImage.getWidth());
		this.setHeight(fxImage.getHeight());
		//Create a new selectionShape based on the new image.
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
	
	//the listener used to lock the selectiontool.
	private class ClickListener implements EventHandler<MouseEvent>
	{
		@Override
		public void handle(MouseEvent e)
		{
			//if primary mouse button is pressed, toggle movement of the selectionShape
			if(e.getButton() == MouseButton.PRIMARY)
			{
				if(moveMouse)
					moveMouse=false;
				else
					moveMouse=true;
			}
		}
		
	}
	
	//move the selectionShape with the mouse
	private class ShapeMover implements EventHandler<MouseEvent>
	{
		@Override
		public void handle(MouseEvent e)
		{
			//if this ability is enabled.
			if(moveMouse)
			{
				//set the layout and the position of the selection.
				selectionShape.setLayoutX(e.getX());
				selectionShape.setLayoutY(e.getY());
				squareX=e.getX();
				squareY=e.getY();
			}
		}
	}
	//returns the coordinates of the selectionbox in pixels and the dimensions of the box in meters.
	public SelectionRectangle getSelection()
	{
		//return coordinates of the selection.
		return new SelectionRectangle(squareX,squareY,selectionShape.getWidth(),selectionShape.getHeight());
	}
}
