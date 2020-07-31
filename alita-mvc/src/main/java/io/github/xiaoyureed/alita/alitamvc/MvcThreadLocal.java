package io.github.xiaoyureed.alita.alitamvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/19 22:09
 * @Description: store request & response of each http request.每次请求 request 和 response 都是新的, 所以不放在Store中了
 */
public final class MvcThreadLocal {

    private static final Logger log = LoggerFactory.getLogger(MvcThreadLocal.class);

    private static final ThreadLocal<Map<String, Object>> thread_local_map = new ThreadLocal<>();

    private MvcThreadLocal(){}

    public static void set(String key, Object value) {
        Map<String, Object> map = thread_local_map.get();
        if (map == null) {
            map = new HashMap<>();
            thread_local_map.set(map);
        }
        map.put(key, value);
    }

    public static Object get(String key) {
        Map<String, Object> map = thread_local_map.get();
        if (map == null) {
            log.error(">>> thread local wasn't init");
            return null;
        }
        return map.get(key);
    }

    /**
     * clear, prevent leak
     */
    public static void clear() {
        thread_local_map.remove();
    }
}
