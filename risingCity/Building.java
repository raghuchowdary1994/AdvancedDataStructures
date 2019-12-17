
public class Building {

	// building class fields buildingNum, executed_time and total_time
	private int buildingNum;
	private int executed_time;
	private int total_time;
	public int workingDays = 0; // used to track consecutive working days

	public RedBlackNode refNodeinTree;
	// reference to node in redblack tree

	// constructor to initialize the object
	public Building(int buildingNum, int total_time) {
		this.buildingNum = buildingNum;
		this.executed_time = 0;
		this.total_time = total_time;
	}

	public Building(int buildingNum, int executed_time, int total_time) {
		this.buildingNum = buildingNum;
		this.executed_time = executed_time;
		this.total_time = total_time;
	}

	public int getBuildingNum() {
		return buildingNum;
	}

	public void setBuildingNum(int buildingNum) {
		this.buildingNum = buildingNum;
	}

	public int getExecuted_time() {
		return executed_time;
	}

	public void setExecuted_time(int executed_time) {
		this.executed_time = executed_time;
	}

	public void updateExecutedTime(int executed_value) {
		this.executed_time += executed_value;
	}

	public int getTotal_time() {
		return total_time;
	}

	public void setTotal_time(int total_time) {
		this.total_time = total_time;
	}

}
