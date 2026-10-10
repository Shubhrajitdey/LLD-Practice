/*In Challenge 1, you sorted exactly two accounts (fromAccount and toAccount) by ID.

In a payment gateway or settlement engine, a single transaction often involves an arbitrary list of accounts (e.g., settling an order among a Buyer, a Seller, a Delivery Partner, and a Platform Fee account).

Multiple batch jobs run concurrently, each receiving an arbitrary list of accounts to lock and update together:

Batch 1: Locks [Acc-4, Acc-1, Acc-9]

Batch 2: Locks [Acc-9, Acc-2, Acc-4]

Batch 3: Locks [Acc-1, Acc-9]

If threads lock accounts in the order they appear in the incoming list, deadlock is guaranteed.

The Requirements
Write a method public boolean settleBatch(List<Account> accounts, double settlementAmount) inside a SettlementService class:

Dynamic Ordering: Each account has a unique ID (e.g., long id). Ensure your method locks the accounts in a globally consistent order, regardless of how the list is ordered when passed in.

Deduplication: A buggy client might accidentally pass the same account twice in the list (e.g., [Acc-2, Acc-4, Acc-2]). Your code must handle or filter duplicates so you do not attempt to lock the same resource twice in a nested or inconsistent manner.

Clean Execution & Release:

Acquire all locks in order.

Perform the balance deduction/credit for each account.

Release all acquired locks cleanly in reverse order inside a finally block, ensuring no lock leaks even if an unexpected exception occurs during the updates. */
package Multithread;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

class Account {
    private final long id;
    private double balance;
    final ReentrantLock lock = new ReentrantLock();

    public Account(long id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    public long getId() { return id; }
    public double getBalance() { return balance; }
    public void updateBalance(double amount) { this.balance += amount; }
}

class SettlementService {
    public boolean settleBatch(List<Account> accounts, double settlementAmount) {
        List<Account> uniqueList = accounts.stream()
                                           .distinct()
                                           .sorted(Comparator.comparingLong(Account::getId))
                                           .collect(Collectors.toList());

        List<Account> acquiredLocks = new ArrayList<>();
        // Journal tracking each mutation so it can be rolled back on error
        Map<Account, Double> appliedChanges = new LinkedHashMap<>();

        try {
            // 1. Lock all resources in global ID order
            for (Account acc : uniqueList) {
                acc.lock.lock();
                acquiredLocks.add(acc);
                System.out.println("🔒 " + Thread.currentThread().getName() 
                        + " acquired lock on Account: " + acc.getId());
            }

            // 2. Perform updates with fail-safe journal
            for (Account acc : uniqueList) {
                // Example business check: Account 3 has some failure condition
                if (acc.getId() == 3 && acc.getBalance() < settlementAmount) {
                    throw new IllegalStateException("Insufficient funds on Account ID: " + acc.getId());
                }

                acc.updateBalance(settlementAmount);
                appliedChanges.put(acc, settlementAmount);
            }
            System.out.println("✅ " + Thread.currentThread().getName() + " completed batch update.");

            return true; // Whole batch committed cleanly

        } catch (Exception ex) {
            System.err.println("❌ Batch failed: " + ex.getMessage() + ". Rolling back applied updates...");
            
            // ROLLBACK: Revert every account that had its balance changed prior to the failure
            for (Map.Entry<Account, Double> entry : appliedChanges.entrySet()) {
                Account mutatedAccount = entry.getKey();
                double amountToReverse = entry.getValue();
                mutatedAccount.updateBalance(-amountToReverse); // Undo mutation
                System.err.println("🔄 "+Thread.currentThread().getName() + " Reverted Account " + mutatedAccount.getId() + " by " + (-amountToReverse));
            }
            return false;

        } finally {
            // 3. Always release locks in reverse order
            for (int i = acquiredLocks.size() - 1; i >= 0; i--) {
                System.out.println("🔓 " + Thread.currentThread().getName() 
                        + " released lock on Account: " + acquiredLocks.get(i).getId());
                acquiredLocks.get(i).lock.unlock();
                
            }
        }
    }
}
public class MultiAccountSettlementService {
    public static void main(String[] args) throws InterruptedException {
        Account acc1 = new Account(1, 1000.0);
        Account acc2 = new Account(2, 2000.0);
        Account acc4 = new Account(4, 4000.0);
        Account acc9 = new Account(9, 9000.0);

        // Batch 1: Accounts in order [4, 1, 9] with an accidental duplicate of 4
        List<Account> batch1Accounts = Arrays.asList(acc4, acc1, acc9, acc4);

        // Batch 2: Accounts in opposite order [9, 2, 4]
        List<Account> batch2Accounts = Arrays.asList(acc9, acc2, acc4);

        // Batch 3: Another overlapping order [1, 9]
        List<Account> batch3Accounts = Arrays.asList(acc1, acc9);


        SettlementService settlementService = new SettlementService();
        
        Thread t1 = new Thread(()->{
            settlementService.settleBatch(batch3Accounts, 200);
        }, "Thread-1");

        Thread t2 = new Thread(()->{
            settlementService.settleBatch(batch2Accounts, 100);
        }, "Thread-2");

        Thread t3 = new Thread(()->{
            settlementService.settleBatch(batch1Accounts, 50);
        }, "Thread-3");

        t1.start();
        t2.start();
        t3.start();


        t1.join();
        t2.join();
        t3.join();

        List<Account> allAccounts = Arrays.asList(acc1, acc2, acc4, acc9);
        System.out.println("=== Current Account Balances ===");
        for (Account acc : allAccounts) {
            System.out.println("Account ID: " + acc.getId() + " | Balance: " + acc.getBalance());
        }
    }
}
