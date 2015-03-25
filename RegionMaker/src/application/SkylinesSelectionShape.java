package application;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

public class SkylinesSelectionShape extends SelectionShape
{
	private double width=18000;
	private double height=18000;
		
	public SkylinesSelectionShape(Map map,double imageResX,double imageResY)
	{
		//the size of the image to draw relative to the image.
		double imageWidth=(width/map.getRealWidth())*imageResX;
		double imageHeight=(height/map.getRealHeight())*imageResY;
		
		//Image to draw the shape of the city onto
		BufferedImage img=new BufferedImage((int)imageWidth,(int)imageHeight,BufferedImage.TYPE_INT_ARGB);
		//the graphics for the drawing
		Graphics g=img.getGraphics();
		
		//There are 9 squares in the shape of the city so this is the size of each.
		double squareWidth=imageWidth/9;
		double squareHeight=imageHeight/9;
		
		//draw every square in the shape of the city. That is 9x9
		for(int i=0;i<9;i++)
		{
			for(int u=0;u<9;u++)
			{
				//this is the center map. That is the plots you can buy
				if(i>=2 && i<=6 && u>=2 && u<=6)
				{
					//Want the color to be blue and transparent
					g.setColor(new Color(0,0,255,50));
					//draw the region
					g.fillRect((int)(i*squareWidth),(int)(u*squareHeight),(int)(squareWidth),(int)(squareWidth));
				}
				//Else it is just the edge of the region.
				else
				{
					//want the color to be turquise(or whatever) and more transparent round the corners
					g.setColor(new Color(0,255,255,25));
					g.fillRect((int)(i*squareWidth),(int)(u*squareHeight),(int)(squareWidth),(int)(squareWidth));
				}
				//draw black borders around each city section.
				g.setColor(new Color(0,0,0,100));
				g.drawRect((int)(i*squareWidth),(int)(u*squareHeight),(int)(squareWidth),(int)(squareWidth));
			}
		}
		//set this imageview to this image.
		this.setBufferedImage(img);
	}
	//set a bufferedimage this views image.
	public void setBufferedImage(BufferedImage img)
	{
		//Create a new image to add to the view.
		WritableImage fxImage=new WritableImage(img.getWidth(),img.getHeight());
		//Function creates a writableimage out of a bufferedimage
		fxImage=SwingFXUtils.toFXImage(img,fxImage);
		//set the image to this imageview
		this.setImage(fxImage);
	}
	
	public double getWidth()
	{
		return width;
	}
	public double getHeight()
	{
		return height;
	}
}
