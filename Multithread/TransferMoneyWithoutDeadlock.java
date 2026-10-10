package Multithread;

import java.util.Arrays;

class Account{
    private final int id;
    private final String name;
    private double balance;
    public Account(int id, String name) {
        this.id = id;
        this.name = name;
        this.balance = 0.0;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void debit(double amount){
        balance = balance - amount;
    }
    public void credit(double amount){
        balance = balance + amount;
    }
}

class AccountService {

    public void transferMoney(Account fromAccount, Account toAccount, double amount) {
        // Thread 1: transferMoney(accA, accB, 100);
        // Thread 2: transferMoney(accB, accA, 50);

        Account[] arr = new Account[]{fromAccount,toAccount};
        Arrays.sort(arr, (x, y) -> Integer.compare(x.getId(), y.getId()));

        synchronized (arr[0]) {
            System.out.println(Thread.currentThread().getName() + " locked " + arr[0].getId());
            synchronized (arr[1]) {
                System.out.println(Thread.currentThread().getName() + " locked " + arr[1].getId());
                
                fromAccount.debit(amount);
                toAccount.credit(amount);
            }
        }
    }
}

public class TransferMoneyWithoutDeadlock{
    public static void main(String[] args) {
        Account a = new Account(10,"Bittu");
        Account b = new Account(11, "Dey");

        AccountService a1 = new AccountService();
        AccountService b1 = new AccountService();

        Thread t1 = new Thread(()->{
            a1.transferMoney(a, b, 20);
        },"thread1");
        Thread t2 = new Thread(()->{
            b1.transferMoney(b, a, 10);
        },"thread2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {}

        System.err.println("Task completed");
    }
}