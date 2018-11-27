package com.hyr.lock.zkcurator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/*******************************************************************************
 * @date 2018-11-27 上午 11:07
 * @author: <a href=mailto:huangyr>黄跃然</a>
 * @Description: Zookeeper Distributed Lock
 ******************************************************************************/
public class ZookeeperLock {
    private static final String root = "/curator/bonree/locks";

    public static void concurrentOperation(LockCallBall callBall, String zkconfig) {
        try {
            //创建zookeeper的客户端
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            CuratorFramework client = CuratorFrameworkFactory.newClient(zkconfig, retryPolicy);
            client.start();

            //创建分布式锁, 锁空间的根节点路径为/curator/lock
            InterProcessMutex mutex = new InterProcessMutex(client, root);
            mutex.acquire();

            callBall.handle(); // 接口回调

            //完成业务流程, 释放锁
            mutex.release();

            //关闭客户端
            client.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void concurrentOperation(LockCallBall callBall, String zkconfig, String lockname) {
        try {
            //创建zookeeper的客户端
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            CuratorFramework client = CuratorFrameworkFactory.newClient(zkconfig, retryPolicy);
            client.start();

            //创建分布式锁, 锁空间的根节点路径为/curator/lock
            InterProcessMutex mutex = new InterProcessMutex(client, root + "/" + lockname);
            mutex.acquire();

            callBall.handle(); // 接口回调

            //完成业务流程, 释放锁
            mutex.release();

            //关闭客户端
            client.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract static class LockCallBall {
        public abstract void handle();
    }


}
