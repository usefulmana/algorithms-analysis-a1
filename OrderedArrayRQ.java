import java.io.PrintWriter;
import java.lang.String;

public class OrderedArrayRQ implements Runqueue {
    private Proc[] processes;
    private int totalElements;
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

    public boolean isFull() {
        return totalElements == capacity;
    }

    public boolean isEmpty() {
        return totalElements == 0;
    }

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
        
        Proc newProc = new Proc(procLabel, vt);
        if (this.isFull()) doubleCapacity();
        if (this.isEmpty()){
            processes[0] = newProc;
        }
        else
        {
            int i = this.size();

            for(;((i >= 1) && (processes[i-1].getVt() > newProc.getVt())); i--)
            {
                processes[i] = processes[i-1];
            }

            processes[i] = newProc;
    
        }
        totalElements++;
    } // end of enqueue()

    @Override
    public String dequeue() {

        String result = processes[0].getProcLabel();
        processes[0] = null;
        Proc[] newProcesses = new Proc[processes.length];
        totalElements--;
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
            processes[i] = null;
            Proc[] newProcesses = new Proc[processes.length];
            totalElements--;
            if (i == 0){
                System.arraycopy(processes, 1, newProcesses, 0, processes.length - 1);
            }
            else{
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
        int i = findProcessByLabel(procLabel);

        if (i != -1){
            int sum = 0;

            for (int j = 0; j < i; j++) {
                sum += processes[j].getVt();
            }

            return sum;
        }

        return -1; // placeholder, modify this
    }// end of precedingProcessTime()

    @Override
    public int succeedingProcessTime(String procLabel) {
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
        for (int i = 0; i < processes.length; i++) {
            if (processes[i] == null)
                break;
            os.print(processes[i].getProcLabel() + " ");
        }
        os.println("");

    } // end of printAllProcesses()

}