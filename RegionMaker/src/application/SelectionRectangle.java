package application;

public class SelectionRectangle
{
	private double x,y,width,height;
	public SelectionRectangle(double layoutX,double layoutY,double width,double height)
	{
		this.x=layoutX;
		this.y=layoutY;
		this.width=width;
		this.height=height;
	}
	public double getX()
	{
		return x;
	}
	public double getY()
	{
		return y;
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
