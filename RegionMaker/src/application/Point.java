package application;

public class Point
{
	public int x,y;
	double depth;
	public Point(int x,int y,double depth)
	{
		this.x=x;
		this.y=y;
		this.depth=depth;
	}
	public String toString()
	{
		return "[Point x="+x+" y="+y+" depth="+depth+"]";
	}
}
