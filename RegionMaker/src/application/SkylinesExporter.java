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
		double startX=rectangle.getX()/map.getXResolution();
		double startY=rectangle.getY()/map.getYResolution();
		double width=rectangle.getWidth()/map.getXResolution();
		double height=rectangle.getHeight()/map.getYResolution();
		int[][]positions=map.subMap((int)startX,(int)startY,(int)width,(int) height);
		int[][]saveMap=new int[1081][1081];
		double scaleX=(double)positions.length/saveMap.length;
		for(int i=0;i<saveMap.length;i++)
		{
			double scaleY=positions[(int)(scaleX*i)].length/saveMap[i].length;
			for(int u=0;u<saveMap[i].length;u++)
			{
				if(scaleX<1 && scaleY<1)
				{
					double x=(double)i*scaleX;
					double y=(double)u*scaleY;
					int x1=(int)(x);
					int x2=x1+1;
					int y1=(int)(y);
					int y2=y1+1;
					double val=0;
					if(x2>=positions.length || y2>=positions[x1].length)
					{
						val=positions[x1][y1];
					}
					else
						val=((positions[x1][y1]*(x2-x)*(y2-y))+
								(positions[x1][y1]*(x-x1)*(y2-y))+
								(positions[x1][y2]*(x2-x)*(y-y1))+
								(positions[x2][y2]*(x-x1)*(y-y1)))/((x2-x1)*(y2-y1));
					saveMap[i][u]=(int)val;
				}
				else
				{
					saveMap[i][u]=positions[(int)(i*scaleX)][(int)(u*scaleY)];
				}
			}
		}
		
		BufferedImage img=new BufferedImage(1081,1081,BufferedImage.TYPE_USHORT_GRAY);
		int[]data=new int[img.getWidth()*img.getHeight()];
		for(int i=0;i<saveMap.length;i++)
		{
			for(int u=0;u<saveMap[i].length;u++)
			{
				double value=((double)saveMap[i][u]/10)+seaLevel; //FIXME Conversion to meters from desimeters. Should not go here
				if(value>maxHeight)
					value=maxHeight;
				if(value<0)
					value=0;
				value=(value/(maxHeight+seaLevel))*65536;
				data[i+(img.getWidth()*u)]=(int)value;
			}
		}
		img.getRaster().setPixels(0,0,img.getWidth(),img.getHeight(),data);
		try
		{
			ImageIO.write(img,"png",file);
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
