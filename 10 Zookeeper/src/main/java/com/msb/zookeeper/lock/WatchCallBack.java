package com.msb.zookeeper.lock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author: 马士兵教育
 * @create: 2019-09-20 21:26
 */
public class WatchCallBack   implements Watcher  ,AsyncCallback.StringCallback ,AsyncCallback.Children2Callback ,AsyncCallback.StatCallback {

    ZooKeeper zk ;
    String threadName;   //线程名,如Thread-0、Thread-1
    String pathName;     //节点名,如有序节点/lock0000000010、/lock0000000011
    CountDownLatch cc = new CountDownLatch(1);
    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    public void tryLock(){
        try {

            System.out.println(threadName + "  create....");
            //   if(zk.getData("/"))从锁的根目录如果能活得当前线程，说明他曾来过,可以直接return去干活了
            //10个线程依次创建10个有序节点，如lock0000000010
            zk.create("/lock",threadName.getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL,this,"father");
            cc.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void unLock(){
        try {
            zk.delete(pathName,-1);
            System.out.println(threadName + " over work....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }



    /**
     *  zk.create对应的回调
     * @param rc
     * @param path 路径
     * @param ctx 上下文
     * @param name 创建出来的节点名
     */
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        //节点创建成功，数据threadName.getBytes()不为空
        if(name != null ){
            System.out.println(threadName  +"  create node : " +  name );   //Thread-0 create node : /lock0000000011
            pathName =  name ;
            zk.getChildren("/",false,this ,"children");
        }

    }

    /**
     * zk.getChildren 对应的回调
     * @param rc
     * @param path
     * @param ctx
     * @param childrenList 创建的节点列表[lock0000000010,...,lock0000000019]
     * @param stat
     */
    @Override
    public void processResult(int rc, String path, Object ctx, List<String> childrenList, Stat stat) {

        //一定能看到自己前边的。。
//        System.out.println(threadName+"look locks.....");
//        for (String child : children) {
//            System.out.println(child);
//        }
        //排序
        Collections.sort(childrenList);
        //当前pathName截掉/后再list中所在位置
        int i = childrenList.indexOf(pathName.substring(1));
        //是不是第一个
        if(i == 0){
            //只有第一个可以获得锁
            System.out.println(threadName +" i am first....");
            try {
                //可重入锁 ：获得锁的人把自己线程写入锁目录里面
                //zk.setData("/",threadName.getBytes(),-1);
                cc.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            //不是第一个则向前取一个并watch(必须加)，剩余9个线程都走else每个节点都监控前一个节点的事件
            // callBack(也要加)一方失败/中途掉线等
            zk.exists("/"+childrenList.get(i-1),this,this,"children");
        }
    }


    /**
     * 节点事件
     * @param event
     */
    @Override
    public void process(WatchedEvent event) {
        //如果第一个哥们那个锁释放了，其实只有第二个收到了回调事件！！
        //如果不是第一个哥们，中间某一个挂了，也能造成他后边的收到这个通知，从而让他后边那个跟去watch挂掉这个哥们前边的。。。
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                zk.getChildren("/",false,this ,"children");
                break;
            case NodeDataChanged:
                break;
            case NodeChildrenChanged:
                break;
        }

    }

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        //偷懒
    }
}
