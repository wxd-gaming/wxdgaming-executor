package wxdgaming.executor;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 线程执行器工厂
 *
 * @author: wxd-gaming(無心道, 15388152619)
 * @version: 2025-05-15 11:20
 **/
public class ExecutorFactory {

    static final ExecutorMonitor EXECUTOR_MONITOR = new ExecutorMonitor();
    static final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    static final ConcurrentHashMap<String, ExecutorService> EXECUTOR_MAP = new ConcurrentHashMap<>();

    /** 基础线程池 */
    public static ExecutorService EXECUTOR_SERVICE_BASIC;
    /** 业务线程池 */
    public static ExecutorService EXECUTOR_SERVICE_LOGIC;
    /** 虚拟线程池 */
    public static ExecutorService EXECUTOR_SERVICE_VIRTUAL;

    static {
        EXECUTOR_SERVICE_BASIC = create("basic", ExecutorConfig.BASIC_INSTANCE.getCoreSize());
        EXECUTOR_SERVICE_LOGIC = create("logic", ExecutorConfig.LOGIC_INSTANCE.getCoreSize());
        EXECUTOR_SERVICE_VIRTUAL = createVirtual("virtual", ExecutorConfig.VIRTUAL_INSTANCE.getCoreSize());
    }

    public static ExecutorService getExecutor(String name) {
        return EXECUTOR_MAP.get(name);
    }

    /** 创建平台线程池 */
    public static ExecutorServicePlatform create(String name, int corePoolSize) {
        ExecutorServicePlatform executorServicePlatform = new ExecutorServicePlatform(name, corePoolSize);
        EXECUTOR_MAP.put(name, executorServicePlatform);
        return executorServicePlatform;
    }

    /** 创建虚拟线程池 */
    public static ExecutorServiceVirtual createVirtual(String name, int corePoolSize) {
        ExecutorServiceVirtual executorServiceVirtual = new ExecutorServiceVirtual(name, corePoolSize);
        EXECUTOR_MAP.put(name, executorServiceVirtual);
        return executorServiceVirtual;
    }

}
