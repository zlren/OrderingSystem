package lab.zlren.sell.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Created by zlren on 17/10/6.
 */
@Component
@Slf4j
public class RedisLock {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 加锁
     *
     * @param key
     * @param value
     * @return
     */
    public boolean lock(String key, String value) {
        if (this.stringRedisTemplate.opsForValue().setIfAbsent(key, value)) {
            return true;
        }

        String currentValue = this.stringRedisTemplate.opsForValue().get(key);
        // 如果锁过期
        if (!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            String oldValue = this.stringRedisTemplate.opsForValue().getAndSet(key, value);
            if (!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)) {
                // 证明在我设置的时候没有人动过这个锁
                return true;
            }
        }

        return false;
    }

    /**
     * 解锁
     *
     * @param key
     * @param value
     */
    public void unlock(String key, String value) {
        String currentValue = this.stringRedisTemplate.opsForValue().get(key);
        if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
            this.stringRedisTemplate.opsForValue().getOperations().delete(key);
        }
    }



}
