import java.io.PrintWriter;
import java.lang.String;

/**
 * Implementation of the Runqueue interface using a Binary Search Tree.
 *
 * Your task is to complete the implementation of this class. You may add
 * methods and attributes, but ensure your modified class compiles and runs.
 *
 * @author Sajal Halder, Minyi Li, Jeffrey Chan
 */
public class BinarySearchTreeRQ implements Runqueue {

    private BSTNode root;

    public BinarySearchTreeRQ() {
        root = null;
    }

    @Override
    public void enqueue(String procLabel, int vt) {
        if (root == null) {
            root = new BSTNode(new Proc(procLabel, vt));
        } else {
            enqueue(root, new Proc(procLabel, vt));
        }
    } // end of enqueue()

    // adds process to tree using recursion
    private void enqueue(BSTNode node, Proc process) {
        // traverse left
        if (process.getVt() < node.getVt()) {
            if (node.getLeft() == null) {
                node.setLeft(new BSTNode(process));
            } else {
                enqueue(node.getLeft(), process);
            }
        } else // traverse right
        {
            if (node.getRight() == null) {
                node.setRight(new BSTNode(process));
            } else {
                enqueue(node.getRight(), process);
            }
        }
    }

    @Override
    public String dequeue() {
        if (root == null) {
            return "";
        } else {
            // finds the smallest vt node
            BSTNode node = root;
            BSTNode prev = null;
            while (node.getLeft() != null) {
                prev = node;
                node = node.getLeft();
            }

            // smallest vt is the root node
            if (prev == null) {
                root = root.getRight();
            }
            // smallest vt not the root node
            else {
                prev.setLeft(node.getRight());
            }

            return node.getProcLabel();
        }
    } // end of dequeue()

    @Override
    public boolean findProcess(String procLabel) {
        return findProcess(root, procLabel);
    } // end of findProcess()

    // finds a process using recursion
    private boolean findProcess(BSTNode node, String procLabel) {
        if (node == null) {
            return false;
        } else {
            if (node.getProcLabel().equals(procLabel)) {
                return true;
            } else {
                return findProcess(node.getRight(), procLabel) || findProcess(node.getLeft(), procLabel);
            }
        }
    }

    @Override
    public boolean removeProcess(String procLabel) {
        boolean success = findProcess(procLabel);

        if (success && procLabel.equals(root.getProcLabel())) {
            root = removeProcess(root, procLabel);
        } else {
            removeProcess(root, procLabel);
        }

        return success; // placeholder, modify this
    } // end of removeProcess()

    // removes a process using recursion
    public BSTNode removeProcess(BSTNode node, String procLabel) {
        if (node == null) {
            return null;
        } else if (procLabel.equals(node.getProcLabel())) {
            // 0 child nodes
            if (node.getRight() == null && node.getLeft() == null) {
                return null;
            }
            // 1 child nodes
            else if (node.getRight() == null && node.getLeft() != null) {
                return node.getLeft();
            } else if (node.getRight() != null && node.getLeft() == null) {
                return node.getRight();
            }
            // 2 child nodes
            else {
                BSTNode prevMin = null;
                BSTNode min = node.getRight();
                while (min.getLeft() != null) {
                    prevMin = min;
                    min = min.getLeft();
                }
                prevMin.setLeft(min.getRight());

                // switch position of deleted node with min node
                min.setLeft(node.getLeft());
                min.setRight(node.getRight());
                return min;
            }
        } else {
            node.setRight(removeProcess(node.getRight(), procLabel));
            node.setLeft(removeProcess(node.getLeft(), procLabel));
            return node;
        }
    }

    @Override
    public int precedingProcessTime(String procLabel) {
        BSTNode node = getNode(root, procLabel);

        if (node != null) {
            return precedingProcessTime(root, node.getProc());
        }
        return -1;
    } // end of precedingProcessTime()

    private int precedingProcessTime(BSTNode node, Proc proc) {
        int sum;

        if (node == null) {
            sum = 0;
        } else if (node.getVt() < proc.getVt()) {
            sum = totalProcessTime(node.getLeft()) + node.getVt() + precedingProcessTime(node.getRight(), proc);
        } else if (node.getVt() > proc.getVt()) {
            sum = precedingProcessTime(node.getLeft(), proc);
        } else {
            if (node.getProcLabel().equals(proc.getProcLabel())) {
                sum = totalProcessTime(node.getLeft());
            } else {
                sum = totalProcessTime(node.getLeft()) + precedingProcessTime(node.getRight(), proc);
            }
        }
        return sum;
    }

    private BSTNode getNode(BSTNode node, String procLabel) {
        if (node == null) {
            return null;
        } else if (node.getProcLabel().equals(procLabel)) {
            return node;
        } else {
            BSTNode right = getNode(node.getRight(), procLabel);
            BSTNode left = getNode(node.getLeft(), procLabel);
            if (right != null) {
                return right;
            } else if (left != null) {
                return left;
            } else {
                return null;
            }
        }
    }

    // finds total processing time of a tree using recursion
    private int totalProcessTime(BSTNode node) {
        if (node != null) {
            return node.getVt() + totalProcessTime(node.getLeft()) + totalProcessTime(node.getRight());
        } else {
            return 0;
        }
    }

    @Override
    public int succeedingProcessTime(String procLabel) {
        BSTNode node = getNode(root, procLabel);
        if (node != null) {
            return succeedingProcessTime(root, node.getProc());
        }
        return -1;
    } // end of precedingProcessTime()

    private int succeedingProcessTime(BSTNode node, Proc proc) {
        int sum;

        if (node == null) {
            sum = 0;
        } else if (node.getVt() > proc.getVt()) {
            sum = totalProcessTime(node.getRight()) + node.getVt() + succeedingProcessTime(node.getLeft(), proc);
        } else if (node.getVt() < proc.getVt()) {
            sum = succeedingProcessTime(node.getRight(), proc);
        } else {
            if (node.getProcLabel().equals(proc.getProcLabel())) {
                sum = totalProcessTime(node.getRight());
            } else {
                sum = succeedingProcessTime(node.getRight(), proc);
            }
        }
        return sum;
    }

    @Override
    public void printAllProcesses(PrintWriter os) {
        os.println(getAllProcesses(root).trim());
    } // end of printAllProcess()

    // returns all the processes in ascending vt order as a string using recursion
    private String getAllProcesses(BSTNode node) {
        if (node != null) {
            return getAllProcesses(node.getLeft()) + node.getProcLabel() + " " + getAllProcesses(node.getRight());
        } else {
            return "";
        }
    }

    public class BSTNode {
        private BSTNode left;
        private BSTNode right;
        private Proc process;

        public BSTNode(Proc process) {
            this.left = null;
            this.right = null;
            this.process = process;
        }

        public BSTNode(BSTNode left, BSTNode right, Proc process) {
            this.left = left;
            this.right = right;
            this.process = process;
        }

        public BSTNode getRight() {
            return right;
        }

        public BSTNode getLeft() {
            return left;
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

        public void setLeft(BSTNode left) {
            this.left = left;
        }

        public void setRight(BSTNode right) {
            this.right = right;
        }
    }
} // end of class BinarySearchTreeRQ
