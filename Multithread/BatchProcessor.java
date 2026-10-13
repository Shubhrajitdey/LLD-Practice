/*Context: High-Volume Analytics Batch Processor (ExecutorService + Futures + Deadlock Prevention)You are designing an asynchronous analytics pipeline for an e-commerce platform.Requirements:
The system receives hundreds of independent merchant analytics calculation jobs throughout the day.Each job computes monthly metrics for a merchant and returns an AnalyticsReport (containing merchantId, totalRevenue, reportTimestamp).Because calculations involve CPU-bound number crunching and remote database calls, creating raw Thread objects per job will exhaust memory.
The platform needs to fire off a batch of $M$ calculation jobs concurrently, collect all results, and handle timeouts: if any individual merchant report takes longer than 2 seconds, that report should be cancelled/failed gracefully without holding up the batch.
During calculations, each job needs concurrent access to two shared resource registries: a CacheRegistry and an AuditLogRegistry. Different tasks might access these two registries in different sequences. */
package Multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

// 1. Data Model
class AnalyticsReport {
    private final String merchantId;
    private final double totalRevenue;
    private final long durationMs;

    public AnalyticsReport(String merchantId, double totalRevenue, long durationMs) {
        this.merchantId = merchantId;
        this.totalRevenue = totalRevenue;
        this.durationMs = durationMs;
    }

    @Override
    public String toString() {
        return "AnalyticsReport[merchant=" + merchantId + ", revenue=" + totalRevenue + ", time=" + durationMs + "ms]";
    }
}

// 2. Shared Resource Registries with Locks
class CacheRegistry {
    final ReentrantLock lock = new ReentrantLock();

    public void updateCache(String merchantId, double revenue) {
        // TODO: Print/simulate cache update
    }
}

class AuditLogRegistry {
    final ReentrantLock lock = new ReentrantLock();

    public void logAudit(String merchantId, double revenue) {
        // TODO: Print/simulate audit log entry
    }
}

// 3. Shared Service implementing Global Lock Ordering
class MetricsCoordinator {
    private final CacheRegistry cacheRegistry;
    private final AuditLogRegistry auditLogRegistry;

    public MetricsCoordinator(CacheRegistry cacheRegistry, AuditLogRegistry auditLogRegistry) {
        this.cacheRegistry = cacheRegistry;
        this.auditLogRegistry = auditLogRegistry;
    }

    public void recordMetrics(String merchantId, double revenue) {
        // TODO: Acquire cache lock FIRST, then audit lock SECOND
        // TODO: Execute updates and release both in finally blocks (reverse order)
    }
}

// 4. Callable Task
class AnalyticsTask implements Callable<AnalyticsReport> {
    private final String merchantId;
    private final long processingTimeMs;
    private final MetricsCoordinator coordinator;

    public AnalyticsTask(String merchantId, long processingTimeMs, MetricsCoordinator coordinator) {
        this.merchantId = merchantId;
        this.processingTimeMs = processingTimeMs;
        this.coordinator = coordinator;
    }

    @Override
    public AnalyticsReport call() throws Exception {
        // TODO: 1. Simulate work with Thread.sleep(processingTimeMs)
        // TODO: 2. Call coordinator.recordMetrics(...)
        // TODO: 3. Return AnalyticsReport instance
        return null;
    }
}
public class BatchProcessor {
    public static void main(String[] args) {
        CacheRegistry cache = new CacheRegistry();
        AuditLogRegistry audit = new AuditLogRegistry();
        MetricsCoordinator coordinator = new MetricsCoordinator(cache, audit);

        // TODO: Create a fixed thread pool (e.g., size 4)
        ExecutorService executor = null;

        // Tasks: M1, M2, M3 are fast (300ms); M4 is slow (3000ms)
        List<AnalyticsTask> tasks = List.of(
            new AnalyticsTask("M1", 300, coordinator),
            new AnalyticsTask("M2", 300, coordinator),
            new AnalyticsTask("M3", 300, coordinator),
            new AnalyticsTask("M4", 3000, coordinator)
        );

        List<Future<AnalyticsReport>> futures = new ArrayList<>();

        // TODO: Submit all tasks to executor and collect Futures

        // TODO: Loop through futures:
        //       - Read future.get(2, TimeUnit.SECONDS)
        //       - Catch TimeoutException -> call future.cancel(true) and log timeout
        //       - Catch ExecutionException and InterruptedException

        // TODO: Graceful shutdown (executor.shutdown() + awaitTermination())
    }
}
