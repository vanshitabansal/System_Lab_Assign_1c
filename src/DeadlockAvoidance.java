import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class DeadlockAvoidance {
    static ArrayList<Integer> available = new ArrayList<>();
    static Semaphore mutex, mutex2;
    static int noOfResources;
    static int noOfProcesses;
    static int count = 0;
    public static void main(String[] args) throws InterruptedException {
        int n=5;
        int m=3;
        int max_resources[]={10,5,7};
        int current_allocation[][]={
                {0,1,0},
                {2,0,0},
                {3,0,2},
                {2,1,1},
                {0,0,2}
        };
        int max_allocation[][]={
                {7,5,3},
                {3,2,2},
                {9,0,2},
                {2,2,2},
                {4,3,3}
        };
        int need[][]=new int[n][m];
        int available_resources[]=new int[m];
        boolean executed[]=new boolean[n];
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                need[i][j]=max_allocation[i][j]-current_allocation[i][j];
                available_resources[j]+=current_allocation[i][j];
            }
        }
        System.out.println("-------Need of Resources--------");
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                System.out.print(need[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println("-------Available Resources--------");

        for(int i=0;i<m;i++){
            available_resources[i]=max_resources[i]-available_resources[i];
            System.out.print(available_resources[i]+" ");
            available.add(available_resources[i]);
        }
        System.out.println();
        ArrayList<Integer> safe_sequence=new ArrayList<>();
        int process=-1;
        int flag=1;
        while(true){
            //System.out.println("ji");
            for(int i=0;i<n;i++){
                process=-1;
                if(!executed[i]) {
                    flag=1;
                    for(int j=0;j<m;j++){
                        if(need[i][j]<=available_resources[j]){
                            process=i;
                        }
                        else{
                            flag=0;
                            break;
                        }
                    }
                    if(flag==1 && process!=-1) {
                        safe_sequence.add(process);
                        executed[process] = true;
                        for (int k = 0; k < m; k++) {
                            available_resources[k] += current_allocation[process][k];
                        }
                        process = -1;
                    }
                }
            }
            if(process!=-1){
                    System.out.println("No safe sequence possible :(");
                    return;
            }
            if(safe_sequence.size()==n)
                break;
        }
        System.out.println("-------Safe Sequence of the processes is--------");
        for(int i=0;i<safe_sequence.size()-1;i++){
            System.out.print("P"+safe_sequence.get(i)+" -> ");
        }
        System.out.print("P"+safe_sequence.get(safe_sequence.size()-1));
        System.out.println();

        //Threading
        noOfProcesses = n;
        noOfResources = m;
        mutex = new Semaphore(1);
        mutex2 = new Semaphore(1);
        System.out.println("\nExecuting Processes...\n");
        for(int i=0;i<noOfProcesses;i++){
            ArrayList<Integer> needOfProcess = new ArrayList<>();
            ArrayList<Integer> allocOfProcess = new ArrayList<>();
            for(int j=0;j<noOfResources;j++) {
                needOfProcess.add(need[i][j]);
                allocOfProcess.add(current_allocation[i][j]);
            }
            MyThread processThread = new MyThread(allocOfProcess, needOfProcess, i);
            processThread.start();
            //processThread.join();
        }
        boolean readyToExit = false;
        while(!readyToExit){
            mutex2.acquire();
            if(count == noOfProcesses)
                readyToExit = true;
            mutex2.release();
        }
        System.out.println("All Processes Finished");
    }
}
