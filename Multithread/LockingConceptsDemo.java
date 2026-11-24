package Multithread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.*;

public class LockingConceptsDemo {

    public static void main(String[] args) throws InterruptedException {
        SmartBankAccount account = new SmartBankAccount(500.0);
        ExecutorService executor = Executors.newFixedThreadPool(6);

        System.out.println("=== 1. AtomicInteger & volatile (Failed PIN & Auto-Freeze) ===");
        for (int i = 1; i <= 3; i++) {
            final int attempt = i;
            executor.submit(() -> {
                account.recordFailedLogin();
                System.out.println("Attempt " + attempt + " | Frozen status: " + account.isAccountFrozen());
            });
        }
        Thread.sleep(200);

        System.out.println("\n=== 2. ReadWriteLock (Concurrent Readers vs Exclusive Writer) ===");
        // Two reader threads read simultaneously without blocking each other
        executor.submit(() -> System.out.println("Reader 1 Balance: " + account.getBalance()));
        executor.submit(() -> System.out.println("Reader 2 Balance: " + account.getBalance()));
        // One writer thread blocks readers briefly to update balance
        executor.submit(() -> {
            account.deposit(200.0);
            System.out.println("Writer deposited 200.0");
        });
        Thread.sleep(200);

        System.out.println("\n=== 3. ReentrantLock + tryLock + Condition (Wait & Signal) ===");
        // Thread A tries to withdraw $1000 (currently only $700 available), so it waits on Condition
        executor.submit(() -> {
            try {
                System.out.println("Withdraw Thread: Waiting up to 2s for $1000...");
                boolean success = account.withdrawWithTimeout(1000.0, 2000);
                System.out.println("Withdraw Thread result: " + (success ? "SUCCESS" : "TIMED OUT"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread.sleep(500); // Ensure Withdraw Thread starts waiting first

        // Thread B deposits $500 and signals the Condition to wake Thread A
        executor.submit(() -> {
            System.out.println("Deposit Thread: Adding $500 and signaling...");
            account.depositAndNotify(500.0);
        });

        Thread.sleep(500);

        System.out.println("\n=== 4. synchronized (Simple Intrinsic Monitor Lock) ===");
        executor.submit(() -> account.updateAccountNickname("Primary Salary Account"));
        executor.submit(() -> account.updateAccountNickname("Emergency Vault"));

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("\nFinal Balance: " + account.getBalance());
    }
}

class SmartBankAccount {

    // 1. volatile: Visibility across threads (no stale CPU cache reads)
    private volatile boolean accountFrozen = false;

    // 2. AtomicInteger: Lock-free CAS atomicity for counters
    private final AtomicInteger failedPinAttempts = new AtomicInteger(0);

    // 3. ReadWriteLock: Multiple concurrent readers, single exclusive writer
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    // 4. ReentrantLock + Condition: Explicit lock with timeouts and state-based waiting
    private final ReentrantLock transferLock = new ReentrantLock(true);
    private final Condition sufficientFunds = transferLock.newCondition();

    private double balance;

    public SmartBankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public boolean isAccountFrozen() {
        return accountFrozen;
    }

    public void recordFailedLogin() {
        if (failedPinAttempts.incrementAndGet() >= 3) {
            this.accountFrozen = true;
        }
    }

    public double getBalance() {
        readLock.lock();
        try {
            return balance;
        } finally {
            readLock.unlock();
        }
    }

    public void deposit(double amount) {
        writeLock.lock();
        try {
            balance += amount;
        } finally {
            writeLock.unlock();
        }
    }

    public boolean withdrawWithTimeout(double amount, long timeoutMs) throws InterruptedException {
        if (!transferLock.tryLock(timeoutMs, TimeUnit.MILLISECONDS)) {
            return false;
        }
        try {
            while (balance < amount) {
                if (!sufficientFunds.await(timeoutMs, TimeUnit.MILLISECONDS)) {
                    return false;
                }
            }
            balance -= amount;
            return true;
        } finally {
            transferLock.unlock();
        }
    }

    public void depositAndNotify(double amount) {
        transferLock.lock();
        try {
            balance += amount;
            sufficientFunds.signalAll();
        } finally {
            transferLock.unlock();
        }
    }

    // 5. synchronized: Simple intrinsic monitor lock
    public synchronized void updateAccountNickname(String newNickname) {
        System.out.println(Thread.currentThread().getName() + " updated nickname to: " + newNickname);
    }
}
