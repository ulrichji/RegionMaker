package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.concurrent.Task;

public class MapLoadTask extends Task<Map> {
	private File file;
	
	public MapLoadTask(String path) {
		this.file = new File(path);
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
	
	@Override
	protected Map call() throws IOException {
		Map map = new Map();
		
		@SuppressWarnings("resource")
		FileInputStream fStream=new FileInputStream(file);
		//første blokk er metadata og hver blokk består av 1024 bytes
		byte[]block=new byte[1024];
		//legger verdier fra streamen i block tilsvarende størrelsen til block
		fStream.read(block);
		
		//For å sjekke metadataen kan du lese spesifikasjonen på nettet
		map.setWidth(Integer.parseInt(substring(block,853+7,853+7+6).trim()));
		map.setHeight(map.getWidth()); //TODO Fiks noe av dette, dette trenger ikke å stemme
		map.setResx(Double.parseDouble(substring(block,816,816+12).trim()));
		map.setResy(Double.parseDouble(substring(block,828,828+12).trim()));
		map.setResz(Double.parseDouble(substring(block,840,840+12).trim()));
		//lag kartdata
		map.setMap(new int[map.getWidth()][map.getHeight()]);
		
		for(int i=0; i < map.getWidth(); i++)
		{
			//Update the loading progress properties
			this.updateProgress(i, map.getWidth());
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
				//legg inn data i map
				map.getMap()[i][columnSize-1-count]=number;
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
		
		return map;
	}

}
