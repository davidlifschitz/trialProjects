package edu.yu.cs.com1320.project;




public class test1 {
    public static void main(String[] args) throws InterruptedException {
        boolean equal = false;
        while(!equal){
            long origin = System.nanoTime();
            long one = System.nanoTime();
            //TimeUnit.NANOSECONDS.sleep(1);
            long two = System.nanoTime();
            if(one-origin==two-origin){
                equal = true;
            }
        }
        System.out.println(equal);
    }
}