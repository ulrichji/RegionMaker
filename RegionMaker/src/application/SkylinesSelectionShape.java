package application;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

public class SkylinesSelectionShape extends ImageView
{
	private double width=18000;
	private double height=18000;
	
	private Map map;
	
	public SkylinesSelectionShape(Map map,double imageResX,double imageResY)
	{
		//the size of the image to draw
		double imageWidth=(width/map.getRealWidth())*imageResX;
		double imageHeight=(height/map.getRealHeight())*imageResY;
		
		BufferedImage img=new BufferedImage((int)imageWidth,(int)imageHeight,BufferedImage.TYPE_INT_ARGB);
		Graphics g=img.getGraphics();
		
		double squareWidth=imageWidth/9;
		double squareHeight=imageHeight/9;
		
		for(int i=0;i<9;i++)
		{
			for(int u=0;u<9;u++)
			{	
				if(i>=2 && i<=6 && u>=2 && u<=6)
				{
					g.setColor(new Color(0,0,255,50));
					g.fillRect((int)(i*squareWidth),(int)(u*squareHeight),(int)(squareWidth),(int)(squareWidth));
				}
				else
				{
					g.setColor(new Color(0,255,255,25));
					g.fillRect((int)(i*squareWidth),(int)(u*squareHeight),(int)(squareWidth),(int)(squareWidth));
				}
				g.setColor(new Color(0,0,0,100));
				g.drawRect((int)(i*squareWidth),(int)(u*squareHeight),(int)(squareWidth),(int)(squareWidth));
			}
		}
		this.setBufferedImage(img);
	}
	public void setBufferedImage(BufferedImage img)
	{
		WritableImage fxImage=new WritableImage(1024,1024);
		fxImage=SwingFXUtils.toFXImage(img,fxImage);
		this.setImage(fxImage);
	}
}
