package executortest;

import lombok.extern.slf4j.Slf4j;
import wxdgaming.executor.ExecutorFactory;
import wxdgaming.executor.ExecutorServiceVirtual;
import wxdgaming.executor.IExecutorQueue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class ExecutorServiceVirtualTest {

    public static void main(String[] args) {

        ExecutorServiceVirtual executorServiceVirtual = ExecutorFactory.createVirtual("4", 100);
        executorServiceVirtual.execute(new MyRunnable());
        executorServiceVirtual.execute(new MyRunnable());
        executorServiceVirtual.execute(new MyRunnableQueue());
        executorServiceVirtual.execute(new MyRunnableQueue());
        executorServiceVirtual.execute(new MyTimerRunnable());
        executorServiceVirtual.execute(new MyTimerRunnable());
        executorServiceVirtual.execute(new MyTimerRunnable2());
        executorServiceVirtual.execute(new MyTimerRunnable2());
        executorServiceVirtual.execute(new MyTimerRunnable2Queue());
        executorServiceVirtual.execute(new MyTimerRunnable2Queue());

        executorServiceVirtual.scheduleAtFixedRate(new MyTimerRunnable2Queue(), 1, 1, TimeUnit.SECONDS);

        // LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(15));
    }

    private static class MyRunnable implements Runnable {

        @Override public void run() {
            log.info("1");
        }
    }

    private static class MyRunnableQueue implements Runnable, IExecutorQueue {

        @Override public String queueName() {
            return "1";
        }

        @Override public void run() {
            log.info("1");
        }

    }

    private static class MyTimerRunnable implements Runnable {

        @Override public void run() {
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
            log.info("MyTimerRunnable");
        }

    }

    private static class MyTimerRunnable2 implements Runnable {

        @Override public void run() {
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
            log.info("MyTimerRunnable2");
        }

    }

    /** 构建一个比较耗时的队列任务 */
    private static class MyTimerRunnable2Queue implements Runnable, IExecutorQueue {

        @Override public String queueName() {
            return "2";
        }

        @Override public void run() {
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
            log.info("MyTimerRunnable2Queue");
        }

    }
}
