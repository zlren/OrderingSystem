package lab.zlren.sell.service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zlren on 17/10/6.
 */
public class LockTest {

    @Autowired
    private RedisLock redisLock;

    public void main(String[] args) {

        // // 加锁
        // long time = System.currentTimeMillis() + 10 * 1000;
        // if (this.redisLock.lock(key, String.valueOf(time))) { // 超时时间10s
        //     throw new MyExecption("人太多了，请稍后再试");
        // }
        //
        // // 业务
        //
        // // 解锁
        // this.redisLock.unlock(key, String.valueOf(time));
    }
}
