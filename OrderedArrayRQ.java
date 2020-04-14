import java.io.PrintWriter;
import java.lang.String;

public class OrderedArrayRQ implements Runqueue {

    // Initiate empty proc array
    private Proc[] processes;

    // Total number of processes in the given array
    private int totalElements;

    // Capacity of the array
    private int capacity;

    public OrderedArrayRQ() {
        this(10);
    }

    public OrderedArrayRQ(int capacity) {
        if (capacity > 0) {
            this.capacity = capacity;
            processes = new Proc[this.capacity];
        } else {
            this.capacity = capacity;
            processes = new Proc[this.capacity];
        }
    }

    // Check if the array is full
    public boolean isFull() {
        return totalElements == capacity;
    }

    // Check if the array is empty
    public boolean isEmpty() {
        return totalElements == 0;
    }

    // Return the total number of elements in the given array
    public int size() {
        return totalElements;
    }

    private void doubleCapacity() {
        // private method used for increasing array capacity
        capacity = 2 * capacity;
        Proc[] newProcesses = new Proc[capacity];
        System.arraycopy(processes, 0, newProcesses, 0, processes.length);
        processes = newProcesses;
    }

    private void printAll(Proc[] arr){
        // Private method used for debug purposes
        for (int j = 0; j < arr.length; j++) {
            if (arr[j] != null) {
                System.out.print(arr[j].getProcLabel() + " ");
            }
            else{
                System.out.print("null ");
            }    
        }
        System.out.println("");
    }

    @Override
    public void enqueue(String procLabel, int vt) {
        // Create new proc from given data
        Proc newProc = new Proc(procLabel, vt);

        // if the array is full, double the current capacity
        if (this.isFull()) doubleCapacity();

        // if the array is empty, add the proc to the first position
        if (this.isEmpty()){
            processes[0] = newProc;
        }
        else
        {
            int i = this.size();

            // loop through the array to continuously sort it
            for(;((i >= 1) && (processes[i-1].getVt() > newProc.getVt())); i--)
            {
                processes[i] = processes[i-1];
            }

            processes[i] = newProc;
    
        }

        // increase total number of elements
        totalElements++;
    } // end of enqueue()

    @Override
    public String dequeue() {

        // save the to-be-deleted proc's label
        String result = processes[0].getProcLabel();

        // delete
        processes[0] = null;

        // initiate new array
        Proc[] newProcesses = new Proc[processes.length];

        // decrease number of elements
        totalElements--;

        // copy the content of the old the array to the new
        System.arraycopy(processes, 1, newProcesses, 0, this.totalElements);
        processes = newProcesses;
        return result;

    } // end of dequeue()

    @Override
    public boolean findProcess(String procLabel) {
        if (findProcessByLabel(procLabel) != -1) return true;
        return false;
    } // end of findProcess()

    public int findProcessByLabel(String procLabel){
        // find process label by looping the array
        for (int i = 0; i < processes.length; i++) {
            if (processes[i] == null) break;
            if (processes[i].getProcLabel().equals(procLabel)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean removeProcess(String procLabel) {
        int i = findProcessByLabel(procLabel);
        if (i != -1){
            // Delete the process
            processes[i] = null;
            Proc[] newProcesses = new Proc[processes.length];
            // Decrease the total number of elements
            totalElements--;
            if (i == 0){
                // If the item to be removed is the first item, follow the dequeue() method
                System.arraycopy(processes, 1, newProcesses, 0, processes.length - 1);
            }
            else{
                // If not, split the array into two parts
                System.arraycopy(processes, 0, newProcesses, 0, i);
                System.arraycopy(processes, i + 1, newProcesses, i, processes.length - (i + 1));
            }
            processes = newProcesses;
            return true;
        }
        return false; // placeholder, modify this
    } // end of removeProcess()

    @Override
    public int precedingProcessTime(String procLabel) {
        // find the index of the target process
        int i = findProcessByLabel(procLabel);

        if (i != -1){
            int sum = 0;
            // loops sum
            for (int j = 0; j < i; j++) {
                sum += processes[j].getVt();
            }

            return sum;
        }

        return -1; // placeholder, modify this
    }// end of precedingProcessTime()

    @Override
    public int succeedingProcessTime(String procLabel) {
        // find the index of the target process
        int i = findProcessByLabel(procLabel);

        if (i != -1){
            int sum = 0;

            for (int j = i + 1; j < totalElements; j++) {
                sum += processes[j].getVt();
            }

            return sum;
        }

        return -1; // placeholder, modify this
    } // end of precedingProcessTime()

    @Override
    public void printAllProcesses(PrintWriter os) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < processes.length; i++) {
            if (processes[i] == null) break;
            stringBuilder.append(processes[i].getProcLabel() + " ");
        }
        os.println(stringBuilder.toString().trim());
    } // end of printAllProcesses()

}