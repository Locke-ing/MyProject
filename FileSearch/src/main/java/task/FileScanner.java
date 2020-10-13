package task;

import java.io.File;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FileScanner {

    private ExecutorService pool = Executors.newFixedThreadPool(4);


    // 计数器，不传入数值，表示初始化的值为0
    private volatile AtomicInteger count = new AtomicInteger();

    // 线程等待的锁对象
    private Semaphore semaphore = new Semaphore(0);//第3种实现：acquire()阻塞等待一定数量的许可

    private ScanCallback callback;

    public FileScanner(ScanCallback callback) {
        this.callback = callback;
    }

    /**
     * 扫描文件目录
     * 最开始，不知道有多少子文件夹，不知道应该启动多少个线程
     * @param path
     */
    public void scan(String path) {
        count.incrementAndGet();//启动根目录扫描任务，计数器++i
        doScan(new File(path));
    }

    /**
     *
     * @param dir 待处理的文件夹
     */
    private void doScan(File dir){
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    callback.callback(dir);//文件保存操作
                    File[] children = dir.listFiles();//下一级文件和文件夹
                    if (children != null) {
                        for (File child : children) {
                            if (child.isDirectory()) {//如果是文件夹，递归处理
                                count.incrementAndGet();//启动子文件夹扫描任务，计数器++i
                                System.out.println("当前任务数："+count.get());
                                doScan(child);
                            }
                        }
                    }
                }finally {// 保证线程计数不管是否出现异常，都能够进行-1操作
                    int r = count.decrementAndGet();
                    if(r == 0){
                        semaphore.release();
                    }
                }
            }
        });
    }

    /**
     * 等待扫描任务结束（scan方法）
     * 多线程的任务等待：thread.start();
     * 1.join():需要使用线程Thread类的引用对象
     * 2.wait()线程间的等待，
     */
    public void waitFinish() throws InterruptedException {
        try {
            // 第三种实现
            semaphore.acquire();
        }finally {
            // 阻塞等待直到任务完成，完成后需要关闭线程池
            System.out.println("关闭线程池...");
            // 两种关闭线程池的方式，内部实现原理都是通过内部thread.interrupt()来中断
            pool.shutdownNow();
        }
    }

}
