package Backend;

/*	twoDGaussianDistribution.java
*	
*	Takes two Points and creates two independant distributions in either direction.
*	givePoint() then outputs a Point object within these two distributions
*/

import java.util.Random;
import java.lang.Math;

public class twoDGuassianDistribution{

	private Point start;
	private Point end;

	private Random random;
	
	private double sigma_x;
	private double miu_x;

	private double sigma_y;
	private double miu_y;

	public twoDGuassianDistribution(Point start, Point end){
		this.start = start;
		this.end = end;

		int width = end.x - start.x;
		int length = end.y - start.y;

		this.miu_x = (Math.random() * width + 1) + start.x;
		this.miu_y = (Math.random() * length + 1) + start.y;
		// this.miu_y = start.y + miu_x;

		//Still needs work on the size
		this.sigma_x = (Math.random() * (8)) + 1;
		this.sigma_y = (Math.random() * (8)) + 1;

		this.random = new Random();
	}

	public Point givePoint(){
		int x;
		int y;
		do {
			x = (int) (miu_x + random.nextGaussian() * sigma_x);
			y = (int) (miu_y + random.nextGaussian() * sigma_y);
		} while ((x >= end.x) || (x < start.x-1) || (y >= end.y) || (y < start.y-1));
		return new Point(x,y);
	}
}