# Java Concurrent Programming

Slides for this session can be found [HERE](http://bit.ly/2mDy0Vy).


## Simple Threads Example - us.juggl.twentyseventeen.march.SimpleThreads

* Run with `mvn clean compile exec:java@simple`


## Thread Pool Example - us.juggl.twentyseventeen.march.ThreadPool

* Run with `mvn clean compile exec:java@threadpool`


## Parallel Stream Example - us.juggl.twentyseventeen.march.ParallelStream

* Run with `mvn clean compile exec:java@parallelstream`


## Synchronized Block Deadlock - us.juggl.twentyseventeen.march.SynchronizedBlockDeadlock

* Run with `mvn clean compile exec:java@syncblockdeadlock`
* How to diagnose a deadlock?
  * `jstack -l <PID>`
  * jvisualvm
  * jconsole
  * ThreadMXBeam/JMX - http://javaconceptoftheday.com/detect-deadlocked-threads-using-threadmxbean-class-java/

## ReentrantReadWriteLock Deadlock - us.juggl.twentyseventeen.march.DeadlockHell

* Run with `mvn clean compile exec:java@deadlockhell`
* How to diagnose this deadlock?
  * `jstack -l <PID>`
  * jvisualvm
  * ThreadMXBeam/JMX - http://javaconceptoftheday.com/detect-deadlocked-threads-using-threadmxbean-class-java/

None of the tools above will indicate directly that there is a deadlock. 

```
"Thread-2" #13 prio=5 os_prio=0 tid=0x00007f00ec006000 nid=0x2d74 waiting on condition [0x00007f012f8d6000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x000000076e36f720> (a java.util.concurrent.locks.ReentrantReadWriteLock$NonfairSync)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:836)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireQueued(AbstractQueuedSynchronizer.java:870)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquire(AbstractQueuedSynchronizer.java:1199)
	at java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock.lock(ReentrantReadWriteLock.java:943)
	at us.juggl.twentyseventeen.march.DeadlockHell$Writer.run(DeadlockHell.java:61)
	- locked <0x000000076e36f5e0> (a java.util.concurrent.locks.ReentrantReadWriteLock)
	at java.lang.Thread.run(Thread.java:745)

"Thread-1" #12 prio=5 os_prio=0 tid=0x00007f00ec001800 nid=0x2d73 waiting for monitor entry [0x00007f012f9d7000]
   java.lang.Thread.State: BLOCKED (on object monitor)
	at us.juggl.twentyseventeen.march.DeadlockHell$Reader.run(DeadlockHell.java:47)
	- waiting to lock <0x000000076e36f5e0> (a java.util.concurrent.locks.ReentrantReadWriteLock)
	at java.lang.Thread.run(Thread.java:745)
```