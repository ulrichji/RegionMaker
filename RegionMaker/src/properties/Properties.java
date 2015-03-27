package properties;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Properties
{
	private final static String propertiesPath="properties_regionMaker.txt";
	//descriptors for parsing an writing the properties file
	private final static String oceanAngleDescriptor="Ocean Angle";
	private final static String mapViewWidthDescriptor="MapView width";
	private final static String mapViewHeightDescriptor="MapView height";
	private final static String seaLevelDescriptor="Sea level";
	
	private static double oceanAngle=30;
	private static double mapViewWidth=1024;
	private static double mapViewHeight=1024;
	private static double seaLevel=4;
	
	public static double getSeaLevel()
	{
		return seaLevel;
	}

	public static void setSeaLevel(double seaLevel)
	{
		Properties.seaLevel = seaLevel;
	}
	
	public static double getOceanAngle()
	{
		return oceanAngle;
	}

	public static void setOceanAngle(double oceanAngle)
	{
		Properties.oceanAngle=oceanAngle;
	}

	public static double getMapViewWidth()
	{
		return mapViewWidth;
	}

	public static void setMapViewWidth(double mapViewWidth)
	{
		Properties.mapViewWidth=mapViewWidth;
	}

	public static double getMapViewHeight()
	{
		return mapViewHeight;
	}

	public static void setMapViewHeight(double mapViewHeight)
	{
		Properties.mapViewHeight=mapViewHeight;
	}
	
	public static void loadProperties()
	{
		File f=new File(propertiesPath);
		
		if(f.exists())
		{
			System.out.println("Loading properties");
		}
		else
		{
			System.out.println("Properties file not found! Creating new.");
			saveProperties();
		}
	}
	
	
	private static void saveProperties()
	{
		File f=new File(propertiesPath);
		BufferedWriter writer=null;
		
		try
		{
			if(!f.exists())
				f.createNewFile();
			
			writer=new BufferedWriter(new FileWriter(f));
			
			writer.write(oceanAngleDescriptor+": "+Double.toString(oceanAngle));
			writer.newLine();
			writer.write(seaLevelDescriptor+": "+Double.toString(seaLevel));
			writer.newLine();
			writer.write(mapViewWidthDescriptor+": "+Double.toString(mapViewWidth));
			writer.newLine();
			writer.write(mapViewHeightDescriptor+": "+Double.toString(mapViewHeight));
			System.out.println("Properties saved successfully");
			
		}catch(IOException e)
		{
			System.err.println("Properties not saved successfully");
			e.printStackTrace();
		}
		finally
		{
			try
			{
				writer.close();
			}catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void showPropertiesWindow()
	{
		PropertiesWindowController window=new PropertiesWindowController();
		window.showPropertiesWindow();
	}
}
