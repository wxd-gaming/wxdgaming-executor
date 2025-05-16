package wxdgaming.executor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 任务执行器
 *
 * @author: wxd-gaming(無心道, 15388152619)
 * @version: 2025-05-16 19:21
 */
public abstract class ExecutorService implements Executor {

    public CompletableFuture<Void> future(Runnable runnable) {
        ExecutorJobFutureVoid executorJobFuture = new ExecutorJobFutureVoid(runnable);
        execute(executorJobFuture);
        return executorJobFuture.getFuture();
    }

    public <T> CompletableFuture<T> future(Supplier<T> supplier) {
        ExecutorJobFuture<T> executorJobFuture = new ExecutorJobFuture<>(supplier);
        execute(executorJobFuture);
        return executorJobFuture.getFuture();
    }

    /** 延迟执行一次的任务 */
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        ExecutorJobScheduled executorJobScheduled = new ExecutorJobScheduled(this, command, true);
        ScheduledFuture<?> scheduledFuture = ExecutorFactory.scheduledExecutorService.schedule(executorJobScheduled, delay, unit);
        executorJobScheduled.setSchedule(scheduledFuture);
        return scheduledFuture;
    }

    /** 上一次任务卡住了，不会触发下一次 */
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        ExecutorJobScheduled executorJobScheduled = new ExecutorJobScheduled(this, command, true);
        ScheduledFuture<?> scheduledFuture = ExecutorFactory.scheduledExecutorService.scheduleAtFixedRate(executorJobScheduled, initialDelay, period, unit);
        executorJobScheduled.setSchedule(scheduledFuture);
        return scheduledFuture;
    }

    /** 上一次任务卡住了，依然会触发下一次 */
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long period, TimeUnit unit) {
        ExecutorJobScheduled executorJobScheduled = new ExecutorJobScheduled(this, command, false);
        ScheduledFuture<?> scheduledFuture = ExecutorFactory.scheduledExecutorService.scheduleWithFixedDelay(executorJobScheduled, initialDelay, period, unit);
        executorJobScheduled.setSchedule(scheduledFuture);
        return scheduledFuture;
    }
}
