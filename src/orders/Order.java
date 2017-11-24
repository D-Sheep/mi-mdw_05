package orders;

import java.io.Serializable;
import java.util.Random;

public abstract class Order implements Serializable {
	private static final long serialVersionUID = -7729897658029525266L;
	protected static final Random PRNG = new Random(); 
	
	private long ID;
	
	public Order() {
		ID = PRNG.nextLong();
	}
	
	public long getID() {
		return ID;
	}
}
