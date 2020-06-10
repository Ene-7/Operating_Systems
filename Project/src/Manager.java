

public class Manager implements Runnable{
    private String name;
    private Thread ManagerThread;

    Manager(String Num){
        setName("Manager_" + Num);
        this.ManagerThread = new Thread(this, name);
    }

    public void start() {
        ManagerThread.start();
    }

    @Override
    public void run() {
        //TODO Fill in this with functions of Manager
    }

    public void setName(String in){
        this.name = in;
    }

    public String getName(){
        return this.name;
    }


    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-Main.time)+"] "+getName()+": "+m);
    }

}
