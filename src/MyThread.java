import java.util.ArrayList;

public class MyThread extends Thread{
    ArrayList<Integer> need;
    ArrayList<Integer> alloc;
    int processID;
    boolean hasCompleted = false;
    public void run(){
        while(!hasCompleted){
            try {
                DeadlockAvoidance.mutex.acquire();
                boolean permitted = true;
                for(int i=0;i<DeadlockAvoidance.noOfResources;i++){
                    if(need.get(i) > DeadlockAvoidance.available.get(i)){
                        permitted = false;
                        break;
                    }
                }
                if(permitted){
                    System.out.println("--> Process " + processID);

                    System.out.print("\tAllocated :   ");
                    for(int i=0;i<DeadlockAvoidance.noOfResources;i++)
                        System.out.print(alloc.get(i) + "  ");
                    System.out.println();

                    System.out.print("\tNeeded    :   ");
                    for(int i=0;i<DeadlockAvoidance.noOfResources;i++)
                        System.out.print(need.get(i) + "  ");
                    System.out.println();

                    System.out.print("\tAvailable :   ");
                    for(int i=0;i<DeadlockAvoidance.noOfResources;i++)
                        System.out.print(DeadlockAvoidance.available.get(i) + "  ");
                    System.out.println();

                    System.out.println("\tResource Allocated!");
                    System.out.println("\tResource Released!");

                    for(int i=0;i<DeadlockAvoidance.noOfResources;i++){
                        int newAvail = DeadlockAvoidance.available.get(i) + alloc.get(i);
                        DeadlockAvoidance.available.set(i, newAvail);
                        alloc.set(i, 0);
                    }

                    System.out.print("\tNow Available :   ");
                    for(int i=0;i<DeadlockAvoidance.noOfResources;i++)
                        System.out.print(DeadlockAvoidance.available.get(i) + "  ");
                    System.out.println("\n\n");
                    hasCompleted = true;
                    DeadlockAvoidance.mutex2.acquire();
                    DeadlockAvoidance.count++;
                    DeadlockAvoidance.mutex2.release();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Do Something
            DeadlockAvoidance.mutex.release();
        }
    }
    MyThread(ArrayList<Integer> alloc, ArrayList<Integer> need, int processID){
        this.need = need;
        this.processID = processID;
        this.alloc = alloc;
    }
}
