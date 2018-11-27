package com.hyr.lock.redis;


import redis.clients.jedis.Jedis;

/**
 * redis锁
 * a.互斥性。在任意时刻，只有一个客户端能持有锁。
 * b.不会发生死锁。即使有一个客户端在持有锁的期间崩溃而没有主动解锁，也能保证后续其他客户端能加锁。
 * c.具有容错性。只要大部分的Redis节点正常运行，客户端就可以加锁和解锁。 TODO 未实现
 * d.加锁和解锁必须是同一个客户端，客户端自己不能把别人加的锁给解了。
 */
public class RedisLock {
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    /**
     * 尝试获取分布式锁
     *
     * @param jedis      Redis客户端
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {
        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        return LOCK_SUCCESS.equals(result);
    }
}