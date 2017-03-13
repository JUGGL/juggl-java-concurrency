package us.juggl.twentyseventeen.march;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

/**
 * Created by dphillips on 3/11/17.
 */
public class SynchronizedBlockDeadlock {

    private static final Logger LOG = Logger.getLogger("deadlock");

    private static final int numThreads = 8;
    public static final String ATTEMPT_SYNC_O1 = "Attempting to synchronize on o1 in thread: ";
    public static final String SYNC_THREAD_O1 = "Synchronized on o1 in thread: ";
    public static final String ATTEMPT_THREAD_O2 = "Attempting to synchronize on o2 in thread: ";
    public static final String SYNC_THREAD_O2 = "Synchronized on o2 in thread: ";

    public static void main(String[] args) {
        new SynchronizedBlockDeadlock().runThreads();
    }

    public void runThreads() {
        final Object o1 = new Object();
        final Object o2 = new Object();
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        IntStream.range(0, numThreads).forEach(i -> threadPool.submit(() -> asyncTask(o1, o2)));
    }

    public void asyncTask(Object o1, Object o2) {
        try {
            double rnd = Math.random();
            double top = 0.75;
            long threadId = Thread.currentThread().getId();
            if (rnd >= top) {
                LOG.log(Level.INFO, ATTEMPT_SYNC_O1 +threadId);
                synchronized (o1) {
                    Thread.sleep(Math.round(Math.random()*1000));
                    LOG.log(Level.INFO, SYNC_THREAD_O1 + threadId);
                    synchronized(o2) {
                        Thread.sleep(Math.round(Math.random()*1000));
                        LOG.log(Level.INFO, "o1 -> o2 -> synchronized");
                    }
                }
            } else {
                LOG.log(Level.INFO, ATTEMPT_THREAD_O2 +threadId);
                synchronized (o2) {
                    LOG.log(Level.INFO, SYNC_THREAD_O2 + threadId);
                    Thread.sleep(Math.round(Math.random()*1000));
                    synchronized (o1) {
                        Thread.sleep(Math.round(Math.random()*1000));
                        LOG.log(Level.INFO, "o2 -> o1 -> synchronized");
                    }
                }
            }
            LOG.warning("Synchronized block successfully released.");
        } catch(Exception ise) {
            LOG.log(Level.SEVERE, "Error", ise);
        }
    }
}
