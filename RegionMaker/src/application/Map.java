package application;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Map
{
	//TODO legg til mer metadata som hører til filformatet for å gjøre klassen mer generell
	//Kartdata som en 2d tabell
	private int[][]map;
	//oppløsningen i x- ,y- , og z-retning
	private double resx,resy,resz;
	private int width, height;
	//the slope of the seabed in degrees
	private double oceanAngle=30;
	public Map()
	{
	}
	public double getRealWidth()
	{
		return resx*width;
	}
	
	public double getRealHeight()
	{
		return resy*height;
	}
	
	public void setResx(double resx) {
		this.resx = resx;
	}
	
	public void setResy(double resy) {
		this.resy = resy;
	}
	public void setResz(double resz)
	{
		this.resz=resz;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public double getXResolution()
	{
		return resx;
	}
	public double getYResolution()
	{
		return resy;
	}
	public double getZResolution()
	{
		return resz;
	}
	public int getWidth()
	{
		return width;
	}
	public int getHeight()
	{
		return height;
	}
	
	//a light function to get an image. The interpolation is not good, but it is sufficient for drawing in the view
	public BufferedImage getDrawableMap(int width,int height)
	{
		//the image to return
		BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);
		//the highest and lowest point on the map
		int maxVal=getHighestPoint();
		int minVal=getLowestPoint();
		//the difference between lowest and highets point
		int mapHeight=maxVal-minVal;
		//the ratio between the maps dimensions and the parameter dimensions
		double scaleWidthFactor=(double)getWidth()/width;
		double scaleHeightFactor=(double)getHeight()/height;
		//loop through the image and draw each pixel according to the map.
		for(int i=0;i<width;i++)
		{
			for(int u=0;u<height;u++)
			{
				//the coordinate on the map
				double x=i*scaleWidthFactor;
				double y=u*scaleHeightFactor;
				
				//the grayscale value of the pixel
				int value=(int)(((double)(map[(int)x][(int)y]-minVal)/mapHeight)*255);
				
				//draw the pixel
				image.setRGB(i,u,new Color(value,value,value).getRGB());
			}
		}
		//return.
		return image;
	}
	//returns the lowest point in this map
	private int getLowestPoint()
	{
		int lowestPoint=Integer.MAX_VALUE;
		for(int i=0;i<map.length;i++)
		{
			for(int u=0;u<map[i].length;u++)
			{
				if(map[i][u]<lowestPoint)
					lowestPoint=map[i][u];
			}
		}
		return lowestPoint;
	}
	//returns the hightes point in this map.
	private int getHighestPoint()
	{
		int highestPoint=Integer.MIN_VALUE;
		for(int i=0;i<map.length;i++)
		{
			for(int u=0;u<map[i].length;u++)
			{
				if(map[i][u]>highestPoint)
					highestPoint=map[i][u];
			}
		}
		return highestPoint;
	}
	
	//Returnerer bildet i rektangelet generert av parameterne
	//TODO Throw exception
	public int[][]subMap(int x1,int y1,int width,int height)
	{
		int[][]image=new int[width][height];
		//Fyll image med verdier fra utsnittet fra map
		for(int i=0;i<image.length && i+x1<this.width;i++)
		{
			for(int u=0;u<image[i].length && u+y1<this.height;u++)
				image[i][u]=map[i+x1][u+y1];
		}
		return image;
	}
	//sets this map with the given coordinates
	public void setMap(int[][]map)
	{
		this.map=map;
		this.width=map.length;
		this.height=map[0].length;
	}
	//returns the int array that represents the map
	public int[][]getMap()
	{
		return map;
	}
	//generate oceans on all points lower or equal to the seaLevel parameter
	public void generateOceans(int seaLevel)
	{
		//the visited pixels
		boolean[][] visited=new boolean[this.getWidth()][this.getHeight()];
		//Using breath first search. A queue to store the next pixels to add.
		LinkedList<Point> l=new LinkedList<Point>();
		//The coordinates of the pixels.
		int[][]map=this.getMap();
		//the difference in height between each pixel. That is the angle of the ocean bed.
		double steepnessfactor=(Math.tan(oceanAngle*0.0174532925)*this.getXResolution())/this.getZResolution();
		System.out.println(steepnessfactor);
		//loop through all the points in the map
		for(int i=0;i<visited.length;i++)
		{
			for(int u=0;u<visited.length;u++)
			{
				//If the point is higher than the specified sealevel, we want to look for oceanpixels next to it
				if(map[i][u]>seaLevel)
				{
					//this pixel is visited because it is on land.
					visited[i][u]=true;
					//loop through the neighbours of the pixel
					for(int j=i-1;j<=i+1;j++)
					{
						for(int k=u-1;k<=u+1;k++)
						{
							//Check if the coordinate is not self and a valid coordinate. If it is below sealevel, add it to the queue.
							if(!(k==u && j==i) && j<this.getWidth() && j>=0 && k<this.getHeight() && k>=0 && visited[j][k]==false && map[j][k]<=seaLevel)
							{
								//the pixel below sealevel is visited.
								visited[j][k]=true;
								//Decrease the height of the pixel based on the distance to the visited pixel at map[i][u]
								//that is Euclidian distance.
								double dist=Math.sqrt((double)((i-j)*(i-j))+(double)((u-k)*(u-k)))*steepnessfactor;
								//add the discovered pixel to the queue. We want to decrease the height of the next pixel we find.
								l.add(new Point(j,k,0-dist));
							}
						}
					}
				}
			}
		}
		//loop through the queue and repeat the procedure as over.
		while(!l.isEmpty())
		{
			//the position of the first pixel in the queue.
			Point p=l.poll();
			//loop through all the neighbours of the pixel
			for(int j=p.x-1;j<=p.x+1;j++)
			{
				for(int k=p.y-1;k<=p.y+1;k++)
				{
					//if the neighbour pixel is not visited, a valid coordinate and below sealevel. It will be added to the queue to be visited later.
					if(!(k==p.y&&j==p.x) && j<this.getWidth() && j>=0 && k<this.getHeight() && k>=0 && visited[j][k]==false && map[j][k]<=seaLevel)
					{
						//the ocean pixel is visited.
						visited[j][k]=true;
						//we want to decrease the height relative to the angle of the slope and the distance to the pixel.
						double dist=Math.sqrt((double)((p.x-j)*(p.x-j)*getXResolution())+(double)((p.y-k)*(p.y-k))*getYResolution())*steepnessfactor;
						//decrease the height of the pixel
						map[j][k]=(int)(p.depth-dist);
						//add the ocean pixel to the queue to visit it later.
						l.add(new Point(j,k,p.depth-dist));
					}
				}
			}
		}
	}
}
