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

    protected Node headNode;
    protected int listLength;
    
    /**
     * Constructs empty linked list
     */
    public OrderedLinkedListRQ() {
        headNode = null;
        listLength = 0;
    }  // end of OrderedLinkedList()


    @Override
    public void enqueue(String procLabel, int vt) {
        if (headNode == null){
            headNode = new Node(new Proc(procLabel, vt));
        }
        else {
            


        }

    } // end of enqueue()


    @Override
    public String dequeue() {
        // Implement me

        return ""; // placeholder, modify this
    } // end of dequeue()


    @Override
    public boolean findProcess(String procLabel) {
        // Implement me

        return false; // placeholder, modify this
    } // end of findProcess()


    @Override
    public boolean removeProcess(String procLabel) {
        // Implement me

        return false; // placeholder, modify this
    } // End of removeProcess()


    @Override
    public int precedingProcessTime(String procLabel) {
        // Implement me

        return -1; // placeholder, modify this
    } // end of precedingProcessTime()


    @Override
    public int succeedingProcessTime(String procLabel) {
        // Implement me

        return -1; // placeholder, modify this
    } // end of precedingProcessTime()


    @Override
    public void printAllProcesses(PrintWriter os) {
        //Implement me

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

        public void setNodeValue(Node node){
            nextNode = node;
        }
    }

} // end of class OrderedLinkedListRQ
