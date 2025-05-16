# wxdgaming-executor

#### 介绍

线程池执行器
线程池队列，
定时任务，

#### 软件架构

高效性的线程池

支持定时任务，支持队列任务，支持延迟任务

#### 使用

1. 创建

   ```java
   创建平台线程池池 名字是test 最大线程数是10
   ExecutorServicePlatform executorServicePlatform = ExecutorFactory.create("test", 10);
   
   ```

   ```java
   创建虚拟线程池池 名字是test 最大线程数是100
   ExecutorServiceVirtual executorServiceVirtual = ExecutorFactory.createVirtual("test", 100);
   
   ```

需特别注意虚拟线程池并非真正的线程池，而是一种轻量级的线程实现方式，它的创建和销毁开销较小，适用于需要大量并发任务的场景。

虚拟线程的每一个任务都会创建一个新的虚拟线程来执行任务，但是数量限制，同时执行任务的虚拟线程只有100个;

2. 提交任务普通任务

   ```java
   executorServicePlatform.execute(() -> log.info("我是一个任务"));
   executorServiceVirtual.execute(() -> log.info("我是一个任务"));
   ```

3. 提交一个延迟任务

   ```java
   executorServicePlatform.schedule(() -> log.info("我是一个延迟任务"), 1000);   
   executorServiceVirtual.schedule(() -> log.info("我是一个延迟任务"), 1000);
   ```

4. 提交一个定时任务

   ```java
   executorServicePlatform.scheduleAtFixedRate(() -> log.info("我是一个定时任务"), 1, 1, TimeUnit.SECONDS);
   executorServiceVirtual.scheduleAtFixedRate(() -> log.info("我是一个定时任务"), 1, 1, TimeUnit.SECONDS);
   ```
5. 提交队列任务
   需要注意，队列任务顾名思义是所有的任务都会被放入队列中，等待执行，也就是说虽然是线程池但是每个队列在同意时间只有一个任务在执行，当上一个任务执行完成才会执行下一个任务
   ```java
     
     //队列任务就是实现接口 IExecutorQueue 来提供队列名称
     
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
     
   executorServicePlatform.execute(() -> log.info("我是一个定时任务"), 1, 1, TimeUnit.SECONDS);
   executorServiceVirtual.execute(() -> log.info("我是一个定时任务"), 1, 1, TimeUnit.SECONDS);
        
   ```
6. 输出

   ```java
   19:39:42.699 [4-1] INFO executortest.ExecutorServiceTest -- 我是一个任务
   19:39:43.714 [4-6] INFO executortest.ExecutorServiceTest -- MyTimerRunnable2Queue cancel
   19:39:43.713 [4-3] INFO executortest.ExecutorServiceTest -- 1 executortest.ExecutorServiceTest$MyRunnable#<init>():65
   19:39:43.713 [4-2] INFO executortest.ExecutorServiceTest -- 1 executortest.ExecutorServiceTest$MyRunnable#onEvent():69
   19:39:43.714 [4-7] INFO executortest.ExecutorServiceTest -- 1 executortest.ExecutorServiceTest#main():30
   19:39:43.714 [4-2] ERROR wxdgaming.executor.ExecutorEvent -- executortest.ExecutorServiceTest#main():16
   java.lang.RuntimeException: 1
      at executortest.ExecutorServiceTest$MyRunnable.onEvent(ExecutorServiceTest.java:70)
      at wxdgaming.executor.ExecutorEvent.run(ExecutorEvent.java:41)
      at wxdgaming.executor.ExecutorJob.run(ExecutorJob.java:26)
      at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
      at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
      at wxdgaming.executor.ThreadRunnable.run(ThreadRunnable.java:12)
      at java.base/java.lang.Thread.run(Thread.java:1583)
   19:39:48.713 [4-4] INFO executortest.ExecutorServiceTest -- MyTimerRunnable 1
   19:39:48.713 [4-4] WARN wxdgaming.executor.ExecutorMonitor -- 线程: 4-4, 执行器: executortest.ExecutorServiceTest#main():37, 执行时间: 5000ms
   19:39:48.714 [4-5] INFO executortest.ExecutorServiceTest -- MyTimerRunnable2
   19:39:48.714 [4-5] WARN wxdgaming.executor.ExecutorMonitor -- 线程: 4-5, 执行器: executortest.ExecutorServiceTest#main():45, 执行时间: 5000ms
   19:39:48.729 [4-6] INFO executortest.ExecutorServiceTest -- MyTimerRunnable2Queue
   19:39:48.729 [4-6] WARN wxdgaming.executor.ExecutorMonitor -- 线程: 4-6, 执行器: executortest.ExecutorServiceTest#main():53, 执行时间: 5015ms
   19:39:49.742 [4-8] INFO executortest.ExecutorServiceTest -- MyTimerRunnable2
   19:39:49.742 [4-8] WARN wxdgaming.executor.ExecutorMonitor -- 线程: 4-8, 执行器: executortest.ExecutorServiceTest#main():45, 执行时间: 5015ms
   ```
