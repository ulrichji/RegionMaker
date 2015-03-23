package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

public class SkylinesExporter
{
	private Map map;
	private SelectionRectangle rectangle;
	private File file;
	private double seaLevel=40;
	private double maxHeight=1024-seaLevel;
	public SkylinesExporter(Map map, SelectionRectangle selectionRectangle)
	{
		this.map=map;
		this.rectangle=selectionRectangle;
	}
	public void selectFile()
	{
		Stage stage=new Stage();
		//create the file chooser
		FileChooser fc_openFile=new FileChooser();
		fc_openFile.setTitle("Export cities:skylines");
		fc_openFile.getExtensionFilters().add(new ExtensionFilter("PNG","*.png"));
		//Wait for the filechooser to find the file
		File file = fc_openFile.showSaveDialog(stage);
		//if there is a file, try to open it.
        if (file != null) {
            this.file=file;
        }
	}
	public void export()
	{
		//The pixel coordiante to the upper left corner on the map to start.
		double startX=rectangle.getX()/map.getXResolution();
		double startY=rectangle.getY()/map.getYResolution();
		//the dimensions of the selection in pixels
		double width=rectangle.getWidth()/map.getXResolution();
		double height=rectangle.getHeight()/map.getYResolution();
		
		//Copy out the subsection of the map that is selected.
		int[][]positions=map.subMap((int)startX,(int)startY,(int)width,(int) height);
		//this is the skylines maps dimensions.
		int[][]saveMap=new int[1081][1081];
		//How large is the maps subsection relative to the heightmap we want to use in skylines
		double scaleX=(double)positions.length/saveMap.length;
		//Go through all the pixels in the map we want to save.
		for(int i=0;i<saveMap.length;i++)
		{
			//How much the height of the subsection is relative to the map we want to save.
			double scaleY=positions[(int)(scaleX*i)].length/saveMap[i].length;
			for(int u=0;u<saveMap[i].length;u++)
			{
				//if we need to make the map bigger. Use bilinear interpolation.
				if(scaleX<1 && scaleY<1)
				{
					//the closest pixel
					double x=(double)i*scaleX;
					double y=(double)u*scaleY;
					//round down to get the one pixel
					int x1=(int)(x);
					//round up to get the pixel on the other side.
					int x2=x1+1;
					int y1=(int)(y); //round down
					int y2=y1+1; //round up
					
					//the height of this pixel
					double val=0;
					//if we are moving outside the map
					if(x2>=positions.length || y2>=positions[x1].length)
					{
						//if we are moving outside the map, return the height of the edge.
						val=positions[x1][y1];
					}
					//We are inside the map
					else
						//set the value using bilinear interpolation.
						//Formula taken from wikipedia: http://en.wikipedia.org/wiki/Bilinear_interpolation
						val=((positions[x1][y1]*(x2-x)*(y2-y))+
								(positions[x1][y1]*(x-x1)*(y2-y))+
								(positions[x1][y2]*(x2-x)*(y-y1))+
								(positions[x2][y2]*(x-x1)*(y-y1)))/((x2-x1)*(y2-y1));
					
					//save the value.
					saveMap[i][u]=(int)val;
				}
				else
				{
					//the image must be scaled down and there is no need for interpolation.
					saveMap[i][u]=positions[(int)(i*scaleX)][(int)(u*scaleY)];
				}
			}
		}
		
		//the image to save the data to.
		BufferedImage img=new BufferedImage(1081,1081,BufferedImage.TYPE_USHORT_GRAY);
		//In order to set the bufferedimage correctly, we need to set the data using an array.
		int[]data=new int[img.getWidth()*img.getHeight()];
		//loop through all the pixels in the map.
		for(int i=0;i<saveMap.length;i++)
		{
			for(int u=0;u<saveMap[i].length;u++)
			{
				//Convert the mapdata to meters and increase the height according to sealevel.
				double value=((double)saveMap[i][u]/10)+seaLevel; //FIXME Conversion to meters from desimeters. Should not go here
				//Clipping, and high areas are set to maxValue
				if(value>maxHeight)
					value=maxHeight;
				//We are clipping again and the negative heights are set to 0
				if(value<0)
					value=0;
				//Calculate the height to be an USHORT value. 
				value=(value/(maxHeight+seaLevel))*65536;
				//set the data value at the pixel we are at now.
				data[i+(img.getWidth()*u)]=(int)value;
			}
		}
		//Set the raster of the image to get the 16-bit pixels correctly.
		img.getRaster().setPixels(0,0,img.getWidth(),img.getHeight(),data);
		try
		{
			//write the image to a file.
			ImageIO.write(img,"png",file);
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
