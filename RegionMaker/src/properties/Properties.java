package properties;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Properties implements Cloneable
{
	private final static String propertiesPath="properties_regionMaker.txt";
	//descriptors for parsing an writing the properties file
	private final static String oceanAngleDescriptor="Ocean Angle";
	private final static String mapViewWidthDescriptor="MapView width";
	private final static String mapViewHeightDescriptor="MapView height";
	private final static String seaLevelDescriptor="Sea level";
	
	private double oceanAngle=30;
	private double mapViewWidth=1024;
	private double mapViewHeight=1024;
	private  double seaLevel=4;
	
	public Properties(Properties properties)
	{
		this.oceanAngle=properties.getOceanAngle();
		this.mapViewWidth=properties.getMapViewWidth();
		this.mapViewHeight=properties.getMapViewHeight();
		this.seaLevel=properties.getSeaLevel();
	}

	public Properties()
	{
	}

	public double getSeaLevel()
	{
		return seaLevel;
	}

	public void setSeaLevel(double seaLevel)
	{
		this.seaLevel = seaLevel;
	}
	
	public double getOceanAngle()
	{
		return oceanAngle;
	}

	public void setOceanAngle(double oceanAngle)
	{
		this.oceanAngle=oceanAngle;
	}

	public double getMapViewWidth()
	{
		return mapViewWidth;
	}

	public void setMapViewWidth(double mapViewWidth)
	{
		this.mapViewWidth=mapViewWidth;
	}

	public double getMapViewHeight()
	{
		return mapViewHeight;
	}

	public void setMapViewHeight(double mapViewHeight)
	{
		this.mapViewHeight=mapViewHeight;
	}
	
	@SuppressWarnings("resource")
	public void loadProperties()
	{
		File f=new File(propertiesPath);
		
		if(f.exists())
		{
			System.out.println("Loading properties");
			
			try
			{
				BufferedReader reader=new BufferedReader(new FileReader(f));
				String line;
				
				while((line=reader.readLine()) != null)
				{
					String identifier=line.substring(0,line.indexOf(":"));
					String value=line.substring(line.indexOf(":")+1).trim();
					
					if(identifier.equals(mapViewHeightDescriptor))
						setMapViewHeight(Double.parseDouble(value));
					else if(identifier.equals(mapViewWidthDescriptor))
						setMapViewWidth(Double.parseDouble(value));
					else if(identifier.equals(oceanAngleDescriptor))
						setOceanAngle(Double.parseDouble(value));
					else if(identifier.equals(seaLevelDescriptor))
						setSeaLevel(Double.parseDouble(value));
					else
						System.err.println("The identifier \""+identifier+"\" is unknown");
				}
				
			}catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("Properties file not found! Creating new.");
			saveProperties();
		}
	}
	
	
	public void saveProperties()
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
