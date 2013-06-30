package verilog.awt;

public class PortName
{
	private String name=null;
	private float angle=0;

	public PortName(String name, float angle)
	{
		super();
		this.name = name;
		this.angle = angle;
	}
	
	public void draw()
	{
		
	}
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the angle
	 */
	public float getAngle()
	{
		return angle;
	}

	/**
	 * @param angle the angle to set
	 */
	public void setAngle(float angle)
	{
		this.angle = angle;
	}
}
