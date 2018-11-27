package com.hyr.lock.zkcurator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/*******************************************************************************
 * @date 2018-11-27 上午 10:51
 * @author: <a href=mailto:huangyr>黄跃然</a>
 * @Description:
 ******************************************************************************/
public class TestZKLock {

    public static void main(String[] args) throws Exception {

        //创建zookeeper的客户端

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.0.193:2181", retryPolicy);

        client.start();

        //创建分布式锁, 锁空间的根节点路径为/curator/lock

        InterProcessMutex mutex = new InterProcessMutex(client, "/curator/lock");

        mutex.acquire();

        //获得了锁, 进行业务流程

        System.out.println("Enter mutex");

        //完成业务流程, 释放锁

        mutex.release();

        //关闭客户端

        client.close();

    }
}
