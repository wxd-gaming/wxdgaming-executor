package executortest;

import lombok.extern.slf4j.Slf4j;
import wxdgaming.executor.*;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class ExecutorServiceTest {

    public static void main(String[] args) {
        ExecutorServicePlatform executorServicePlatform = ExecutorFactory.create("4", 10);

        MyRunnable myRunnable = new MyRunnable();
        executorServicePlatform.execute(() -> log.info("我是一个任务"));
        executorServicePlatform.schedule(
                myRunnable,
                1,
                TimeUnit.SECONDS
        );

        executorServicePlatform.schedule(
                myRunnable.myRunnableQueue,
                1,
                TimeUnit.SECONDS
        );

        executorServicePlatform.schedule(
                new MyRunnableQueue(),
                1,
                TimeUnit.SECONDS
        );

        MyTimerRunnable myTimerRunnable = new MyTimerRunnable();
        myTimerRunnable.scheduledFuture = executorServicePlatform.scheduleAtFixedRate(
                myTimerRunnable,
                1,
                1,
                TimeUnit.SECONDS
        );

        MyTimerRunnable2 myTimerRunnable2 = new MyTimerRunnable2();
        executorServicePlatform.scheduleWithFixedDelay(
                myTimerRunnable2,
                1,
                1,
                TimeUnit.SECONDS
        );

        MyTimerRunnable2Queue command = new MyTimerRunnable2Queue();
        command.scheduledFuture = executorServicePlatform.scheduleWithFixedDelay(
                command,
                1,
                1,
                TimeUnit.SECONDS
        );
    }

    private static class MyRunnable extends ExecutorEvent implements Runnable {
        MyRunnableQueue myRunnableQueue;

        public MyRunnable() {
            myRunnableQueue = new MyRunnableQueue();
        }

        @Override public void onEvent() throws Exception {
            log.info("1 {}", Utils.stack());
            throw new RuntimeException("1");
        }
    }

    private static class MyRunnableQueue extends ExecutorEvent implements Runnable, IExecutorQueue {

        @Override public String queueName() {
            return "1";
        }

        @Override public void onEvent() throws Exception {
            log.info("1 {}", getStack());
        }

    }

    private static class MyTimerRunnable implements Runnable {

        int runCount = 0;
        ScheduledFuture<?> scheduledFuture;

        @Override public void run() {
            final int c = ++runCount;
            if (c >= 5) {
                scheduledFuture.cancel(true);
                log.info("MyTimerRunnable cancel");
            }
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
            log.info("MyTimerRunnable {}", c);
        }

    }

    private static class MyTimerRunnable2 implements Runnable {


        @Override public void run() {
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
            log.info("MyTimerRunnable2");
        }

    }

    private static class MyTimerRunnable2Queue implements Runnable, IExecutorQueue {

        ScheduledFuture<?> scheduledFuture;

        @Override public String queueName() {
            return "2";
        }

        @Override public void run() {
            scheduledFuture.cancel(true);
            log.info("MyTimerRunnable2Queue cancel");
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
            log.info("MyTimerRunnable2Queue");
        }

    }
}
