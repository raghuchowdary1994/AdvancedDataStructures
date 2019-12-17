// Min heap implementation
public class minHeap {

	private Building[] Heap;
	private int size;
	private int maxsize;

	// constructor 
	public minHeap(int maxsize) {
		this.maxsize = maxsize;
		this.size = -1;
		this.Heap = new Building[this.maxsize];
	}

	// return the parent index
	private int parent(int pos) {
		return (pos - 1) / 2;
	}
	// return the index of left child
	private int leftChild(int pos) {
		return (2 * pos) + 1;
	}
	// return the index of right child
	private int rightChild(int pos) {
		return (2 * pos) + 2;
	}
	// insert operation on minHeap
	public void insert(Building new_building) {
		if (this.size >= this.maxsize) {
			return;
		}
		if (this.size == -1) {
			this.Heap[++this.size] = new_building;
			return;
		}
		this.Heap[++this.size] = new_building;
		int current = this.size;

		while (true) {
			Building b_current = this.Heap[current];
			Building b_parent = this.Heap[parent(current)];
			
			// check the condition of execution time and based on building number if execution time 
			// is same check on the building number
			
			if (b_current.getExecuted_time() < b_parent.getExecuted_time()) {
				swap(current, parent(current));
				current = parent(current);
			} else if (b_current.getExecuted_time() == b_parent.getExecuted_time()) {
				if (b_current.getBuildingNum() < b_parent.getBuildingNum()) {
					swap(current, parent(current));
					current = parent(current);
				} else {
					break;
				}
			} else {
				break;
			}
		}
	}

	public Building[] getHeap() {
		return Heap;
	}

	public void setHeap(Building[] heap) {
		Heap = heap;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getMaxsize() {
		return maxsize;
	}

	public void setMaxsize(int maxsize) {
		this.maxsize = maxsize;
	}

	private void swap(int fpos, int spos) {
		Building tmp;
		tmp = this.Heap[fpos];
		this.Heap[fpos] = this.Heap[spos];
		this.Heap[spos] = tmp;
	}


	// remove operation on minHeap
	public Building removeMin() {
		if (this.size == -1)
			return null;

		if (this.size == 0) {
			Building minB = this.Heap[0];
			this.size = -1;
			return minB;
		}
		Building minB = this.Heap[0];
		this.Heap[0] = Heap[this.size];
		this.size--;
		this.minHeapify(0);
		return minB;
	}
	// heapify operation to maintain the heap consistent
	private void minHeapify(int pos) {

		int minL = pos, minR = pos, min = pos;
		int left_child = this.leftChild(pos);
		int right_child = this.rightChild(pos);

		if (this.size == 0) {
			return;
		}
		// after insertion or deletion heapify between two elements 
		if (this.size == 1) {
			Building b_current = this.Heap[pos];
			Building b_leftchild = this.Heap[left_child];
			if (b_current.getExecuted_time() > b_leftchild.getExecuted_time()) {
				min = left_child;
			} else if (b_current.getExecuted_time() == b_leftchild.getExecuted_time()) {
				if (b_current.getBuildingNum() > b_leftchild.getBuildingNum()) {
					min = left_child;
				} else {
					min = pos;
				}
			}

			if (min != pos) {
				swap(min, pos);
			}

		}
		// normal heapify operation
		if (left_child < this.size && right_child < this.size) {

			Building b_current = this.Heap[pos];
			Building b_leftchild = this.Heap[left_child];
			Building b_rightchild = this.Heap[right_child];

			// based on execution times and on building number if execution time is same
			// select the minimum one among the node, left child and right child
			if (b_current.getExecuted_time() > b_leftchild.getExecuted_time()) {
				minL = left_child;
			} else if (b_current.getExecuted_time() == b_leftchild.getExecuted_time()) {
				if (b_current.getBuildingNum() > b_leftchild.getBuildingNum()) {
					minL = left_child;
				} else {
					minR = pos;
				}
			}
			if (b_current.getExecuted_time() > b_rightchild.getExecuted_time()) {
				minR = right_child;
			} else if (b_current.getExecuted_time() == b_rightchild.getExecuted_time()) {
				if (b_current.getBuildingNum() > b_rightchild.getBuildingNum()) {
					minR = right_child;
				} else {
					minR = pos;
				}
			}
			if (minL != minR) {
				Building b1 = this.Heap[minL];
				Building b2 = this.Heap[minR];
				if (b1.getExecuted_time() > b2.getExecuted_time()) {
					min = minR;
				} else if (b1.getExecuted_time() == b2.getExecuted_time()) {
					if (b1.getBuildingNum() > b2.getBuildingNum()) {
						min = minR;
					} else {
						min = minL;
					}
				} else {
					min = minL;
				}
			}
			// if minimum not the node then swap with least one and do heapify again
			if (min != pos) {
				swap(min, pos);
				minHeapify(min);
			}

		}

	}

}
