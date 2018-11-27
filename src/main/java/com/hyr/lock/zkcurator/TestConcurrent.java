package com.hyr.lock.zkcurator;

import java.util.HashMap;
import java.util.Map;

/*******************************************************************************
 * @date 2018-11-27 上午 10:54
 * @author: <a href=mailto:huangyr>黄跃然</a>
 * @Description:
 ******************************************************************************/
public class TestConcurrent {

    private static Map<Integer, Integer> cacheMap = new HashMap<Integer, Integer>();
    private static int index = 0;

    public static void main(String[] args) {
        final String zkConfig = "192.168.0.193:2181";

        final String lockname="incr";
        for (int i = 0; i < 1000; i++) {

            new Thread(new Runnable() {
                public void run() {

                    try {
                        ZookeeperLock.concurrentOperation(new ZookeeperLock.LockCallBall() {
                            public void handle() {
                                //获得了锁, 进行业务流程
                                index++;
                                System.out.println("Enter mutex, time:" + index);
                                if (cacheMap.containsKey(index)) {
                                    System.out.println("the concurrent not safe. has the same i:" + index);
                                    System.exit(-1);
                                } else {
                                    cacheMap.put(index, index);
                                }
                            }
                        }, zkConfig,lockname);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();

        }
    }
}
