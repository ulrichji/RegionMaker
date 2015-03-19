package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Map
{
	//TODO legg til mer metadata som hører til filformatet for å gjøre klassen mer generell
	//Kartdata som en 2d tabell
	private int[][]map;
	//oppløsningen i x- og y-retning
	private double resx,resy;
	private int width, height;
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
	public double getXResolution()
	{
		return resx;
	}
	public double getYResolution()
	{
		return resy;
	}
	public int getWidth()
	{
		return width;
	}
	public int getHeight()
	{
		return height;
	}
	//Returnerer en string som tilsvarer bokstavene mellom start og stopp. Gjør også konverteringen fra bytes til chars
	private String substring(byte[]a,int start,int stop)
	{
		char[]str=new char[stop-start];
		for(int i=0;i<stop-start;i++)
		{
			str[i]=(char)a[i+start];
		}
		return String.copyValueOf(str);
	}
	//TODO throw exception
	public void loadMap(String path) throws IOException
	{
		File f=new File(path);
		@SuppressWarnings("resource")
		FileInputStream fStream=new FileInputStream(f);
		//første blokk er metadata og hver blokk består av 1024 bytes
		byte[]block=new byte[1024];
		//legger verdier fra streamen i block tilsvarende størrelsen til block
		fStream.read(block);
		
		//For å sjekke metadataen kan du lese spesifikasjonen på nettet
		width=Integer.parseInt(substring(block,853+7,853+7+6).trim());
		height=width;//TODO Fiks noe av dette, dette trenger ikke å stemme
		resx=Double.parseDouble(substring(block,816,816+12).trim());
		resy=Double.parseDouble(substring(block,828,828+12).trim());
		
		//lag kartdata
		map=new int[width][height];
		
		for(int i=0;i<width;i++)
		{
			//Les neste blokk
			fStream.read(block);
			//størrelsen på denne blokken
			int columnSize=Integer.parseInt(substring(block,12,18).trim());
			int dataCount=Math.min(columnSize,146);
			int dataLeft=columnSize;
			//144 bytes av kartdaten er metadata til blokken. Start etter dette.
			int offset=144;
			int count=0;
			int count2=0;
			while(dataLeft>0)
			{
				//Parse neste tall
				int number=Integer.parseInt(substring(block,offset,offset+6).trim());
				//Konverter fra decimeter til meter. Dette går fint da høydeoppløsningen på Simcity er 3 meter
				//TODO fiks konverteringen utfra verdier i metadata
				map[i][columnSize-1-count]=(number+5)/10;
				//Øk offsett for å finne når neste tall starter
				offset+=6;
				count++;
				count2++;
				dataLeft--;
				//Dersom vi er ferdig med en blokk uten å være ferdig med kolonnen
				if(count2>=dataCount && dataLeft!=0)
				{
					//Siden det er flere tall på en kolonne enn det er plass til i en blokk vil det være plass til 170 datapunkter i en blokk som kommer etter første blokk i en kolonne
					dataCount=Math.min(170,dataLeft);
					offset=0;
					count2=0;
					fStream.read(block);
				}
			}
		}
	}
	//Returnerer bildet mellom 
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
	public void setMap(int[][]map)
	{
		this.map=map;
		this.width=map.length;
		this.height=map[0].length;
	}
	public int[][]getMap()
	{
		return map;
	}
}
