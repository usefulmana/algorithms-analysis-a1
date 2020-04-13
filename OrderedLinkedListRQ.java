import java.io.PrintWriter;
import java.lang.String;

/**
 * Implementation of the run queue interface using an Ordered Link List.
 *
 * Your task is to complete the implementation of this class.
 * You may add methods and attributes, but ensure your modified class compiles and runs.
 *
 * @author Sajal Halder, Minyi Li, Jeffrey Chan.
 */
public class OrderedLinkedListRQ implements Runqueue {

    private Node first;
    protected int length;
    
    /**
     * Constructs empty linked list
     */
    public OrderedLinkedListRQ() {
        first = null;
    }  // end of OrderedLinkedList()

    // private int size(){
    //     int sum = 0;
    //     Node current = first;

    //     while (current != null){
    //        sum += 1;
    //        current = current.nextNode;
    //     }
    //     return sum;
    // }

    @Override
    public void enqueue(String procLabel, int vt) {
        Node newNode = new Node(new Proc(procLabel, vt));
        Node previous = null;
        Node current = first;

        while (current != null && vt >= current.getVt()){
            previous = current;
            current = current.getNextNode();
        }

        if (previous == null){
            newNode.nextNode = first;
            first = newNode;
        }

        else{
            previous.nextNode = newNode;
            newNode.nextNode = current;
        }
        length++;
    } // end of enqueue()


    @Override
    public String dequeue() {
        if (first == null) return null;

        String result = first.getProcLabel();
        first = first.getNextNode();
        length--;
        return result;// placeholder, modify this
    } // end of dequeue()


    @Override
    public boolean findProcess(String procLabel) {
        
        Node temp = findNodeByProcLabel(procLabel);

        if (temp != null) return true;

        return false; // placeholder, modify this
    } // end of findProcess()


    private Node findNodeByProcLabel(String procLabel){
        Node current = first;

        while (current != null){
           if (current.getProcLabel().equals(procLabel)) return current;
           current = current.nextNode;
        }
        return null;
    }

    @Override
    public boolean removeProcess(String procLabel) {
        Node target = findNodeByProcLabel(procLabel);

        if (target == null){
            return false;
        }
        else {
            int index = findFirstIndexOfItem(procLabel);
            if (index == 0){
                dequeue();
                return true;
            }
            Node previous = findProcByIndex(index - 1);
            Node next = findProcByIndex(index + 1);
            target.nextNode = null;
    
            if (next == null){
                previous.nextNode = null;
                return true;
            }
            else if (next != null) {
                previous.nextNode = next;
                return true;
            }
            return false;
        }
    } // End of removeProcess()

    private int findFirstIndexOfItem(String procLabel){
        int sum = -1;
        Node current = first;
        while (current != null){
    
            if (current.getProcLabel().equals(procLabel)){
                sum += 1;
                break;
            }
            sum += 1;
            current = current.nextNode;
         }
        return sum;
    }

    private Node findProcByIndex(int index){
        int count = 0;
        Node current = first;
        while(count < index){
            current = current.nextNode;
            count++;
        }
        return current;
    }

    @Override
    public int precedingProcessTime(String procLabel) {
        Node current = first;
        int sum = 0;
        Node target = findNodeByProcLabel(procLabel);
        int targetIndex = findFirstIndexOfItem(procLabel);
        if (target != null){
            for (int i = 0; i < targetIndex; i++) {
                sum += current.getVt();
                current = current.nextNode;
            }
    
            return sum;
        }
        return -1; // placeholder, modify this
    } // end of precedingProcessTime()

    @Override
    public int succeedingProcessTime(String procLabel) {
        Node current = first;
        int sum = 0;
        Node target = findNodeByProcLabel(procLabel);
        
        if (target != null){
            while(current != null){
                sum += current.getVt();
                current = current.nextNode;
            }
    
            int finalSum = sum - target.getVt() - precedingProcessTime(procLabel);
    
            return finalSum;
        }
        return -1; // placeholder, modify this
    } // end of precedingProcessTime()


    @Override
    public void printAllProcesses(PrintWriter os) {
        Node current = first;
        StringBuilder stringBuilder = new StringBuilder();
        while (current != null){
            stringBuilder.append(current.getProcLabel() + " ");
            current = current.nextNode;
        }
        os.println(stringBuilder.toString().trim());
    } // end of printAllProcess()

    private class Node {
        protected Proc process;
        protected Node nextNode;

        public Node(Proc proc){
            process = proc;
            nextNode = null;
        }

        public int getVt() {
            return process.getVt();
        }

        public String getProcLabel() {
            return process.getProcLabel();
        }

        public Proc getProc() {
            return process;
        }

        public void setNextNodeValue(Node node){
            nextNode = node;
        }

        public Node getNextNode(){
            return this.nextNode;
        }
    }

} // end of class OrderedLinkedListRQ
