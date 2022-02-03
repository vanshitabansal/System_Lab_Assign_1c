import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class DeadlockAvoidance {

    //Declaring variables and arrays
    static ArrayList<Integer> available = new ArrayList<>();
    static Semaphore mutex, mutex2;
    static int noOfResources;
    static int noOfProcesses;
    static int count = 0;
    public static void main(String[] args) throws InterruptedException, FileNotFoundException {

        //Reading contents from file "input.txt"
        File file = new File("input.txt");
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {

            //Storing input in arrays
            String str = sc.nextLine();
            String[] splited1 = str.split(" ");
            if(splited1.length!=2)
                return;
            int n = Integer.parseInt(splited1[0]);
            int m = Integer.parseInt(splited1[1]);
            int available_resources[] = new int[m];
            int current_allocation[][] = new int[n][m];
            int max_allocation[][] = new int[n][m];
            int need[][] = new int[n][m];
            boolean executed[] = new boolean[n];
            ArrayList<Integer> safe_sequence = new ArrayList<>();
            int process = -1,flag = 1,c = 0;

            str = sc.nextLine();
            String[] splited2 = str.split(" ");

            //Filling available_resources array
            for (int i = 0; i < m; i++) {
                available_resources[i] = Integer.parseInt(splited2[i]);
            }

            //Filling current_allocation array
            while (c < n) {
                str = sc.nextLine();
                String[] splited3 = str.split(" ");
                for (int i = 0; i < m; i++) {
                    current_allocation[c][i] = Integer.parseInt(splited3[i]);
                }
                c++;
            }

            //Filling max_allocation array
            c = 0;
            while (c < n) {
                str = sc.nextLine();
                String[] splited3 = str.split(" ");
                for (int i = 0; i < m; i++) {
                    max_allocation[c][i] = Integer.parseInt(splited3[i]);
                }
                c++;
            }

            //Calculating current need of all the processes
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    need[i][j] = max_allocation[i][j] - current_allocation[i][j];
                }
            }

            //Printing Need array on screen
            System.out.println("-------Need of Resources--------");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    System.out.print(need[i][j] + " ");
                }
                System.out.println();
            }

            for (int i = 0; i < m; i++) {
                available.add(available_resources[i]);
            }

            //checking whether safe sequence exists or not
            while (true) {

                //iteratng over the requirements of all processes
                for (int i = 0; i < n; i++) {
                    process = -1;

                    //if current process i is not executed then check
                    if (!executed[i]) {
                        flag = 1;
                        for (int j = 0; j < m; j++) {
                            if (need[i][j] <= available_resources[j]) {
                                process = i;
                            } else {
                                flag = 0;
                                break;
                            }
                        }

                        //flag == 1 means count of all the needed resources are less than available resources
                        if (flag == 1 && process != -1) {

                            //So add this process to safe sequence and set executed as true
                            safe_sequence.add(process);
                            executed[process] = true;

                            //Make the current allocated resources available to other processes now
                            for (int k = 0; k < m; k++) {
                                available_resources[k] += current_allocation[process][k];
                            }
                            process = -1;
                        }
                    }
                }

                //if no process has all the needed resources are less than available resources then safe seq is not possible
                if (process != -1) {
                    System.out.println("No safe sequence possible :(");
                    return;
                }
                if (safe_sequence.size() == n)
                    break;
            }

            //Found one of the safe sequence (Printing..)
            System.out.println("-------Safe Sequence Found--------");
            for (int i = 0; i < safe_sequence.size() - 1; i++) {
                System.out.print("P" + safe_sequence.get(i) + " -> ");
            }
            System.out.print("P" + safe_sequence.get(safe_sequence.size() - 1));
            System.out.println();

            //Threading to simulate the execution
            noOfProcesses = n;
            noOfResources = m;
            mutex = new Semaphore(1);
            mutex2 = new Semaphore(1);
            System.out.println("\nExecuting Processes...\n");
            // Creation of threads
            for (int i = 0; i < noOfProcesses; i++) {
                ArrayList<Integer> needOfProcess = new ArrayList<>();
                ArrayList<Integer> allocOfProcess = new ArrayList<>();
                for (int j = 0; j < noOfResources; j++) {
                    needOfProcess.add(need[i][j]);
                    allocOfProcess.add(current_allocation[i][j]);
                }
                MyThread processThread = new MyThread(allocOfProcess, needOfProcess, i);
                processThread.start(); // starting newly created threads
            }
            boolean readyToExit = false;
            // wait till all the processes have completed execution
            while (!readyToExit) {
                mutex2.acquire();
                if (count == noOfProcesses)
                    readyToExit = true;
                mutex2.release();
            }
            // exit the program
            System.out.println("All Processes Finished");
        }
    }
}
