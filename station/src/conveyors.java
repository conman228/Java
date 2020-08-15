import java.util.concurrent.locks.ReentrantLock;



public class conveyors {
	int conveyorNum;
	
	public ReentrantLock lock = new ReentrantLock();//Lock of conveyor
	
	public conveyors(int conveyorNum) {//Constructor 
		this.conveyorNum = conveyorNum;
	}	
}
