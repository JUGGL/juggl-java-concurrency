package us.juggl.twentyseventeen.march;

/**
 * A simple example of a basic {@link Thread} implementation. Since this project is meant to be run from Maven, by
 * default Maven will own the thread Monitor. In order to ensure that we can handle {@code notify()} and {@code wait()}
 * correctly, we wrap the thread operations in a synchronized block which synchronizes on the thread instance itself.
 */
public class SimpleThreads {
    private MyThread myThread;

    public static void main(String[] args) {
        SimpleThreads main = new SimpleThreads();
        main.startThread();
    }

    public void startThread() {
        // Create an instance of MyThread
        myThread = new MyThread();

        System.out.println("Starting thread");
        myThread.start();
        myThread.setModified();
        synchronized(myThread) {    // We must use a synchronized block because this may be run from Maven, and using a synchronized block allows this program to have the thread Monitor
            try {
                myThread.wait();
            } catch (InterruptedException ie) {
                // Fall through
            }
        }
        System.out.println("Thread run complete:"+myThread.isModified());
    }
    
    private class MyThread extends Thread {
        
        private boolean modified = false;
        
        public void setModified() {
            this.modified = true;
        }

        public String isModified() {
            return modified?"true":"false";
        }

        @Override
        public void run() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                // Fall through
            }
            synchronized(myThread) {    // Again, we have to use synchronized to avoid problems with Maven
                this.notify();
            }
        }
    }
}