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

    // Head node
    private Node first;

    // Length of the linked list
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
        // Create new node from the given data
        Node newNode = new Node(new Proc(procLabel, vt));

        // Initially, there is no previous node
        Node previous = null;
        // Initially, the current node is the first node
        Node current = first;

        // Loop through the linked list, and replace the current node if the any vt value is lower than the given vt
        while (current != null && vt >= current.getVt()){
            previous = current;
            current = current.getNextNode();
        }

        // no previous node = empty linked list
        if (previous == null){
            newNode.nextNode = first;
            first = newNode;
        }

        else{
            previous.nextNode = newNode;
            newNode.nextNode = current;
        }
        // increase length of the linked list after successfully enqueue
        length++;
    } // end of enqueue()


    @Override
    public String dequeue() {
        // return null if the list is empty
        if (first == null) return null;

        // save deleted process' label
        String result = first.getProcLabel();

        // chance the first node to the next node
        first = first.getNextNode();
        // decrease the length of the linkedlist
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
                // if the target is the first item, use dequeue()
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
        // Set current node to the head node
        Node current = first;
        int sum = 0;

        // Find target node
        Node target = findNodeByProcLabel(procLabel);

        // Find target node index
        int targetIndex = findFirstIndexOfItem(procLabel);
        if (target != null){
            // loop through the linked list
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
            // get sum of all node
            while(current != null){
                sum += current.getVt();
                current = current.nextNode;
            }
            
            // result = sum of all node - target's vt - sum of all nodes bf target
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
        // Basic Node class

        // Each Node contains 1 process
        protected Proc process;

        // Pointer to next node
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
