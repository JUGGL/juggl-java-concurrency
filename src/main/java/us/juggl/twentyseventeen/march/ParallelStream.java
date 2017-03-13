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
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A Java application which uses parallel stream to execute threaded activities
 */
public class ParallelStream {


    private static final Logger LOG = Logger.getLogger("parallelstream");

    private Map<Integer, BigInteger> fibs = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
        ParallelStream t = new ParallelStream();
        t.startThreads();
    }

    public void startThreads() {
        long startTime = Instant.now().toEpochMilli();  // Capture the start time
        Random rnd = new Random();

        // Spin up 10 threads to calculate fibonacci numbers, then collect the public keys together into a list
        Map<Integer, BigInteger> results = rnd.ints(10, 1000, 10000)
                                        .parallel()
                                        .mapToObj(i -> new Integer(i))
                                        .collect(Collectors.toMap(i -> i.intValue(), i -> this.fibonacci(i)));

        long endTime = Instant.now().toEpochMilli();    // Capture the end time

        results.entrySet().stream().forEach(s -> System.out.println(String.format("%d: %d", s.getKey(), s.getValue())));
        System.out.println(String.format("Answers took %d milliseconds to be calculated", (endTime-startTime)));
    }

    public BigInteger fibonacci(int number) {
      switch(number) {
        case 0:
          return BigInteger.ONE;
        case 1:
          return BigInteger.ONE;
        default:
          BigInteger fibo1 = BigInteger.ONE;
          BigInteger fibo2 = BigInteger.ONE;
          BigInteger fibonacci = BigInteger.ZERO;
          for (int i = 2; i <= number; i++) {
            fibonacci = fibo1.add(fibo2);
            fibo1 = fibo2;
            fibo2 = fibonacci;
          }
          return fibonacci;
      }
    }
}
