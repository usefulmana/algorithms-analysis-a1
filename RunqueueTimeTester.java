import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class RunqueueTimeTester
{
    private final static int MAX_VT = 100;
    private final static int AVERAGE_AMOUNT = 10;

    // Generates a file with an certain amount of EN commands followed by
    // a unique identifier and random number between 1-MAX_VT
    private static void generate(PrintWriter writer, int amount)
    {
        Random gen = new Random(System.currentTimeMillis());
        for(int i = 1; i <= amount; i++)
        {
            int vt = gen.nextInt(MAX_VT) + 1;
            writer.println("EN P" + i + " " + vt);
        }
        writer.close();
        System.out.println("Done!");
    }

    // Returns the average nanotime of a queue function
    private static long getAverageTime(String filename, String function, String impl)
    {
        Runqueue[] queues = new Runqueue[AVERAGE_AMOUNT];
        for(int i = 0; i < queues.length; i++)
        {
            queues[i] = createRunQueue(filename, impl);
        }

        long sum = 0;

        for(int i = 0; i < queues.length; i++)
        {
            long before = 0; // initial nano time before process is called
            Runqueue queue = queues[i];
            switch(function)
            {
                case "enqueue":
                    // Generates random vtime for test
                    Random gen = new Random(System.currentTimeMillis());
                    int vt = gen.nextInt(MAX_VT) + 1;

                    // Enqueue test
                    before = System.nanoTime();
                    queue.enqueue("TESTPROCESS", vt);
                    break;
                case "dequeue":
                    // Dequeue test
                    before = System.nanoTime();
                    queue.dequeue();
                    break;
                case "preceding":
                    // Finds random label from file to test
                    String randomLabel = getRandomLabelFromFile(filename);

                    // Preceding time test
                    before = System.nanoTime();
                    queue.precedingProcessTime(randomLabel);
                    break;
                default:
                    System.err.println("Unknown function");
                    printCommands();
            }
            long after = System.nanoTime(); // Nano time after process is finished
            sum += after-before; // Total nano time process took
        }
        // Return average
        return sum/AVERAGE_AMOUNT;
    }

    // Returns a random label from a file of enqueue operations
    private static String getRandomLabelFromFile(String filename)
    {
        // Chooses a random line
        int filesize = getFileSize(filename);
        Random gen = new Random(System.currentTimeMillis());
        int randomLine = gen.nextInt(filesize);

        String label = "";
        BufferedReader reader = null;

        try
        {
            reader = new BufferedReader(new FileReader(filename));

            // Reads random line chosen
            for(int i = 0; i < randomLine; i++)
            {
                reader.readLine();
            }
            String line = reader.readLine();
            String[] tokens = line.split(" ");

            // Gets process label
            label = tokens[1];

            reader.close();
        }
        catch (FileNotFoundException ex)
        {
            System.err.println("File not found");
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
        return label;
    }

    // Finds the amount of lines in a file
    private static int getFileSize(String filename)
    {
        BufferedReader reader = null;
        int numlines = 0;
        try
        {
            reader = new BufferedReader(new FileReader(filename));
            while (reader.readLine() != null)
            {
                numlines++;
            }
            reader.close();
        }
        catch (FileNotFoundException ex)
        {
            System.err.println("File not found");
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
        return numlines;
    }

    // Returns a runqueue based on a file
    // File must only only have EN operations only
    private static Runqueue createRunQueue(String filename, String impl)
    {
        Runqueue queue = null;
        switch(impl)
        {
            case "array":
                queue = new OrderedArrayRQ();
                break;
            case "linkedlist":
                queue  = new OrderedLinkedListRQ();
                break;
            case "tree":
                queue = new BinarySearchTreeRQ();
                break;
            default:
                System.err.println("Unknown implementation");
                printCommands();
        }

        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new FileReader(filename));
        }
        catch (FileNotFoundException ex)
        {
            System.err.println("File not found");
            System.exit(1);
        }

        try
        {
            String line;
            while((line = reader.readLine()) != null)
            {
                String[] tokens = line.split(" ");
                if(tokens.length == 3)
                {
                    if(tokens[0].toUpperCase().equals("EN"))
                    {
                        String vtStr = tokens[2];
                        int vt = Integer.parseInt(vtStr);
                        queue.enqueue(tokens[1], vt);
                    }
                }
            }
            reader.close();
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
        catch(NumberFormatException ef)
        {
            System.err.println(ef.getMessage());
        }
        return queue;
    }

    // Prints the commands
    private static void printCommands()
    {
        System.err.println("Commands:");
        System.err.println("RunqueueTimeTester generate <size> <output file>");
        System.err.println("RunqueueTimeTester time <implementation> <functions> <input file>");
        System.err.println("<functions> = <enqueue | dequeue | preceding>");
        System.err.println("<implementation> = <array | linkedlist | tree>");

        System.exit(1);
    }

    public static void main(String[] args)
    {
        String action = args[0]; // either "generate" or "time"

        if(action.equals("generate") && args.length >= 3)
        {
            String sizeStr = args[1];
            int size = 0;
            try
            {
                size = Integer.parseInt(sizeStr);
            }
            catch (NumberFormatException e)
            {
                System.err.println("Unknown size");
                printCommands();
            }

            String output = args[2];
            PrintWriter writer = null;
            try
            {
                writer = new PrintWriter(new FileWriter(output), true);
            }
            catch (IOException e)
            {
                System.err.println(e.getMessage());
            }

            generate(writer, size);
        }
        else if(action.equals("time")  && args.length >= 4)
        {
            String impl = args[1];
            String func = args[2];
            String inputfile = args[3];

            long nanotime = getAverageTime(inputfile, func, impl);
            System.out.println("Average time: " + (double)nanotime/1e+6 + "ms");
        }
        else
        {
            System.err.println("Unknown command");
            printCommands();
        }
    }
}
