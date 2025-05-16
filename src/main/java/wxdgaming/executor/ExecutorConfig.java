package wxdgaming.executor;

import lombok.Getter;

/**
 * 线程池配置
 *
 * @author: wxd-gaming(無心道, 15388152619)
 * @version: 2025-02-13 15:05
 **/
@Getter
public class ExecutorConfig {

    public static final ExecutorConfig BASIC_INSTANCE = new ExecutorConfig(2, 5000);
    public static final ExecutorConfig LOGIC_INSTANCE = new ExecutorConfig(8, 5000);
    public static final ExecutorConfig VIRTUAL_INSTANCE = new ExecutorConfig(100, 5000);

    private final int coreSize;
    private final int maxQueueSize;

    public ExecutorConfig(int coreSize, int maxQueueSize) {
        this.coreSize = coreSize;
        this.maxQueueSize = maxQueueSize;
    }
}
