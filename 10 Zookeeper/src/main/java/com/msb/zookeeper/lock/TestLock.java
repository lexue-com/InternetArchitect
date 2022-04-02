package com.msb.zookeeper.lock;

import com.msb.zookeeper.configurationcenter.ZKUtils;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author: 马士兵教育
 * @create: 2019-09-20 21:21
 */
public class TestLock {


    ZooKeeper zk ;

    @Before
    public void conn (){
        zk  = ZKUtils.getZK();
    }

    @After
    public void close (){
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void lock(){
        /**
         * 起10个线程创建10个节点，从小到大依次一个个获得锁去干活，前一个干完活释放锁第二个才能获得锁继续。。。
         */
        for (int i = 0; i < 10; i++) {
            new Thread(){
                @Override
                public void run() {
                    WatchCallBack watchCallBack = new WatchCallBack();
                    watchCallBack.setZk(zk);
                    String threadName = Thread.currentThread().getName();
                    watchCallBack.setThreadName(threadName);
                    //每一个线程：
                    //抢锁
                    watchCallBack.tryLock();
                    //干活
                    System.out.println(threadName+" working...");
                    try {
                        //如果不sleep会出现：只有第一个人干完活就结束了
                        //因为第一个线程over work了，第二个线程还没跑到监控前一个节点的地方。。。
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //释放锁
                    watchCallBack.unLock();
                }
            }.start();
        }
        //死循环，主线程不会结束
        while(true){
        }


    }




}
