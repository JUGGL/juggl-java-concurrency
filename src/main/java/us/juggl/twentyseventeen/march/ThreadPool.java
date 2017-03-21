package us.juggl.twentyseventeen.march;

import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class ThreadPool {
    
    private static final Logger LOG = Logger.getLogger("threadpool");
    
    private final Map<Integer, BigInteger> results;
    
    public static void main(String[] args) {
        ThreadPool t = new ThreadPool();
        t.startThreads();
    }
    
    public ThreadPool() {
        this.results = new ConcurrentHashMap<>();
    }
    
    public void startThreads() {
        int threadCount = 5;
        ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);
        System.out.println(String.format("Launching fibonacci calculations on %d threads", threadCount));
        Random rnd = new Random();
        long startTime = Instant.now().toEpochMilli();
        rnd.ints(10, 1000, 10000).forEach(i -> {
            Fibonacci thread = new Fibonacci(i);
            threadPool.submit(thread);
        });
        
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(60, TimeUnit.SECONDS);
        } catch(InterruptedException ie) {
            LOG.log(Level.WARNING, "Interrupted while awaiting thread pool termination.", ie);
        }
        long endTime = Instant.now().toEpochMilli();

        System.out.println(String.format("Generated %d results.\n", results.size()));
        results.entrySet().stream().forEach(s -> System.out.println(String.format("%d: %d", s.getKey(), s.getValue())));
        System.out.println(String.format("Calculations completed in %d milliseconds", (endTime-startTime)));
    }

    private class Fibonacci implements Runnable {
        int n = 0;
        public Fibonacci(int input) {
            n = input;
        }

        public void run() {
            switch (n) {
                case 0:
                    results.put(n, BigInteger.ONE);
                case 1:
                    results.put(n, BigInteger.ONE);
                default:
                    BigInteger fibo1 = BigInteger.ONE;
                    BigInteger fibo2 = BigInteger.ONE;
                    BigInteger fibonacci = BigInteger.ZERO;
                    for (int i = 2; i <= n; i++) {
                        fibonacci = fibo1.add(fibo2);
                        fibo1 = fibo2;
                        fibo2 = fibonacci;
                    }
                    results.put(n, fibonacci);
            }
        }
    }
}
