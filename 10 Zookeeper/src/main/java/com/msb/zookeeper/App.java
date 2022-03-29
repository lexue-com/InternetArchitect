package com.msb.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * zk是有session概念的，没有连接池的概念
 *watch:观察/回调/监听
 * watch的注册值发生在 读类型调用，get，exites。。。
 * Watch的回调方法：
 *      场景一：new zk 时候，传入的watch，这个watch是session级别的，跟path 、node没有关系。
 *      场景二：建立好连接后的实际使用，这个watch是针对path的，是一次性有效的，要永久生效得重复注册
 */
public class App
{
    public static void main( String[] args ) throws Exception {
        System.out.println( "Hello World!" );

        //第一类：new zk 时候，传入的watch，这个watch是session级别的，跟path 、node没有关系。
        final CountDownLatch cd = new CountDownLatch(1);
        //new的对象瞬间返回，但是session的创建及连接zk是异步完成的，所以用CountDownLatch倒数计数器锁可以避免连接未完成导致对象不可用
        final ZooKeeper zk = new ZooKeeper("192.168.150.11:2181,192.168.150.12:2181,192.168.150.13:2181,192.168.150.14:2181",
                3000, new Watcher() {   //session的过期时间决定了ephemeral临时节点的生效时间
            //场景一：new zk 时候，传入的watch，这个watch是session级别的，跟path 、node没有关系。
            @Override
            public void process(WatchedEvent event) {
                Event.KeeperState state = event.getState();
                Event.EventType type = event.getType();
                String path = event.getPath();
                System.out.println("new zk watch: "+ event.toString());

                switch (state) {
                    case Unknown:
                        break;
                    case Disconnected:
                        break;
                    case NoSyncConnected:
                        break;
                    case SyncConnected:
                        System.out.println("connected");
                        // 使当前线程在锁存器倒计数至零之前一直等待，除非线程被中断。
                        cd.countDown();
                        break;
                    case AuthFailed:
                        break;
                    case ConnectedReadOnly:
                        break;
                    case SaslAuthenticated:
                        break;
                    case Expired:
                        break;
                }

                switch (type) {
                    case None:
                        break;
                    case NodeCreated:
                        break;
                    case NodeDeleted:
                        break;
                    case NodeDataChanged:
                        break;
                    case NodeChildrenChanged:
                        break;
                }


            }
        });

        cd.await();
        ZooKeeper.States state = zk.getState();
        switch (state) {
            case CONNECTING:
                System.out.println("ing......");
                break;
            case ASSOCIATING:
                break;
            case CONNECTED:
                System.out.println("ed........");
                break;
            case CONNECTEDREADONLY:
                break;
            case CLOSED:
                break;
            case AUTH_FAILED:
                break;
            case NOT_CONNECTED:
                break;
        }
        //添加数据
        String pathName = zk.create("/ooxx", "olddata".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        //1、同步获取数据：调用之后阻塞获取数据    此时getData()返回值类型位为byte[]
          //getData (String path,Watcher watch,Stat stat)    --watch：针对path的回调 传stat返回的是源数据（包括事务ID等），不传返回的是节点上1M那个数据
          //getData (String path,boolen watch,Stat stat)  --boolen值传false:只是单纯取数据 true:代表DefaultWatch被重新注册，指new zk的那个watch
        final Stat  stat=new Stat();
        byte[] node = zk.getData("/ooxx", new Watcher() {
            //场景二：建立好连接后的实际使用，取数据的同时注册一个监听，取完数据之后未来这个path发生变化会再次回调这个方法
            @Override
            public void process(WatchedEvent event) {
                System.out.println("getData watch: "+event.toString());
                try {
、                  //重复注册path的watch：回调处理完继续监听下一次变更
                    zk.getData("/ooxx",this ,stat);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, stat);

        System.out.println(new String(node));

        //“ooxx”被修改会触发回调(getData watch)
        Stat stat1 = zk.setData("/ooxx", "newdata".getBytes(), 0);
        //二次修改还会触发回调吗？如果没有重复注册path监听的话是不会再触发回调的，因为watch是一次性的
        Stat stat2 = zk.setData("/ooxx", "newdata01".getBytes(), stat1.getVersion());

        //2、异步获取：调用之后不阻塞，数据通过回调方法返回  此时getData()返回值类型位为void
          //getData (String path,Watcher watch,DataCallback cb,Object ctx)    --ctx:上下文
          //getData (String path,boolen watch,DataCallback cb,Object ctx)
        System.out.println("-------async start----------");
        zk.getData("/ooxx", false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                System.out.println("-------async call back----------");
                System.out.println(ctx.toString());
                System.out.println(new String(data));

            }

        },"abc");
        System.out.println("-------async over----------");



        Thread.sleep(2222222);
    }
}
