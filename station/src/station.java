import java.util.Random;

public class station implements Runnable {
	int stationNum, work, amt, input, output;
	conveyors inputConveyor, outputConveyor;
	Random random = new Random();
	
	public station(int stationNum, int work, int amt) { //Constructor
		this.stationNum = stationNum;
		this.work = work;
		this.amt = amt;
		this.setInput();
		this.setOutput();
	}

	public void run() { //Override 
		System.out.println("Routing Station " + this.stationNum + ": Input connection set to conveyor number C" + this.input);
		System.out.println("Routing Station " + this.stationNum + ": Output connection set to conveyor number C" + this.output);
		
		System.out.println("Routing Station " + this.stationNum + ": Workload set. "
				+ "Station " + this.stationNum + " has a total of " + this.work + " package groups to move");
		
		while(work != 0) { //Checks for packages
			if(inputConveyor.lock.tryLock()) {//checks in lock
				System.out.println("Routing Station " + this.stationNum + ": Has lock on conveyer C" + this.input + ".");
				
				if(outputConveyor.lock.tryLock()) {//checks out lock
					System.out.println("Routing Station " + this.stationNum + ": Has lock on conveyer C" + this.output + ".");
					
					movePack(); //Moves a package
					
				}else{
					System.out.println("Routing Station " + this.stationNum + ": Unable to lock output - releasing lock on input conveyer C" + this.input + ".");
					inputConveyor.lock.unlock(); //If out is lock then release in
					
					try { //Sleeps thread for random time so it wont deadlock
						Thread.sleep(100+random.nextInt(151)); 
					}catch(Exception e){}
				}
			}else if(outputConveyor.lock.isHeldByCurrentThread()) {//Checks out if in is locked
				System.out.println("Routing Station " + this.stationNum + ": Unable to lock input - releasing lock on output conveyer C" + this.output + ".");
				outputConveyor.lock.unlock();
					
				try { //Sleep
					Thread.sleep(100+random.nextInt(151));
				}catch(Exception e){}
				}
		}
		// Final message all done
		System.out.print("\n\n* * Station " + this.stationNum + ": Work load successfully completed * *\n\n\n");
	}
	
	public void movePack() {
		this.work--; // Work is less
		
		System.out.println("Routing Station " + this.stationNum + ": Successfully moves packages on conveyor C" + this.input + ".");
		System.out.println("Routing Station " + this.stationNum + ": Successfully moves packages on conveyor C" + this.output + ".");
		System.out.println("Routing Station " + this.stationNum + ": Has " + this.work + " package groups left to move.\n\n");
		
		//Release both locks
		System.out.println("Routing Station " + this.stationNum + ": Unlocks input conveyor C" + this.input + ".");
		inputConveyor.lock.unlock();
		
		System.out.println("Routing Station " + this.stationNum + ": Unlocks output conveyor C" + this.output + ".");
		outputConveyor.lock.unlock();
	}
	
	public void setInput() { // Station goes in from left to right 
		if(this.stationNum == 0) {
			this.input = amt;
		}else{
			this.input = stationNum-1;
		}
	}
	
	public void setOutput() {
		this.output = this.stationNum;
	}
	
	public void setInputCon(conveyors inputConveyor) {
		this.inputConveyor = inputConveyor;
	}
	
	public void setOutputCon(conveyors outputConveyor) {
		this.outputConveyor = outputConveyor;
	}
	
	public int getInputNum() {
		return this.input;
	}
	
	public int getOutputNum() {
		return this.output;
	}
}
