"AsyncTask #1@5641" prio=5 tid=0x8b nid=NA waiting
  java.lang.Thread.State: WAITING
	 blocks AsyncTask #1@5641
	  at java.lang.Object.wait(Object.java:-1)
	  at java.lang.Thread.parkFor$(Thread.java:2135)
	  - locked <0x1641> (a java.lang.Object)
	  at sun.misc.Unsafe.park(Unsafe.java:358)
	  at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:230)
	  at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.awaitNanos(AbstractQueuedSynchronizer.java:2101)
	  at java.util.concurrent.LinkedBlockingQueue.poll(LinkedBlockingQueue.java:467)
	  at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1086)
	  at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1147)
	  at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:636)
	  at java.lang.Thread.run(Thread.java:764)

"AsyncTask #2@5645" prio=5 tid=0x8c nid=NA waiting
  java.lang.Thread.State: WAITING
	 blocks AsyncTask #2@5645
	  at java.lang.Object.wait(Object.java:-1)
	  at java.lang.Thread.parkFor$(Thread.java:2135)
	  - locked <0x1642> (a java.lang.Object)
	  at sun.misc.Unsafe.park(Unsafe.java:358)
	  at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:230)
	  at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.awaitNanos(AbstractQueuedSynchronizer.java:2101)
	  at java.util.concurrent.LinkedBlockingQueue.poll(LinkedBlockingQueue.java:467)
	  at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1086)
	  at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1147)
	  at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:636)
	  at java.lang.Thread.run(Thread.java:764)

"AsyncTask #3@5646" prio=5 tid=0x8d nid=NA waiting
  java.lang.Thread.State: WAITING
	 blocks AsyncTask #3@5646
	  at java.lang.Object.wait(Object.java:-1)
	  at java.lang.Thread.parkFor$(Thread.java:2135)
	  - locked <0x1643> (a java.lang.Object)
	  at sun.misc.Unsafe.park(Unsafe.java:358)
	  at java.util.concurrent.locks.LockSupport.parkNanos(LockSupport.java:230)
	  at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.awaitNanos(AbstractQueuedSynchronizer.java:2101)
	  at java.util.concurrent.LinkedBlockingQueue.poll(LinkedBlockingQueue.java:467)
	  at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1086)
	  at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1147)
	  at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:636)
	  at java.lang.Thread.run(Thread.java:764)

"FinalizerDaemon@4671" daemon prio=5 tid=0x7c nid=NA waiting
  java.lang.Thread.State: WAITING
	 blocks FinalizerDaemon@4671
	  at java.lang.Object.wait(Object.java:-1)
	  at java.lang.Object.wait(Object.java:422)
	  at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:188)
	  - locked <0x1640> (a java.lang.Object)
	  at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:209)
	  at java.lang.Daemons$FinalizerDaemon.runInternal(Daemons.java:232)
	  at java.lang.Daemons$Daemon.run(Daemons.java:103)
	  at java.lang.Thread.run(Thread.java:764)

"main@4666" prio=5 tid=0x2 nid=NA runnable
  java.lang.Thread.State: RUNNABLE
	  at android.os.BinderProxy.transactNative(Binder.java:-1)
	  at android.os.BinderProxy.transact(Binder.java:748)
	  at android.app.IActivityManager$Stub$Proxy.handleApplicationCrash(IActivityManager.java:4321)
	  at com.android.internal.os.RuntimeInit$KillApplicationHandler.uncaughtException(RuntimeInit.java:115)
	  at java.lang.ThreadGroup.uncaughtException(ThreadGroup.java:1068)
	  at java.lang.ThreadGroup.uncaughtException(ThreadGroup.java:1063)
	  at java.lang.Thread.dispatchUncaughtException(Thread.java:1953)

"FinalizerWatchdogDaemon@4669" daemon prio=5 tid=0x7d nid=NA waiting
  java.lang.Thread.State: WAITING
	 blocks FinalizerWatchdogDaemon@4669
	  at java.lang.Object.wait(Object.java:-1)
	  at java.lang.Daemons$FinalizerWatchdogDaemon.sleepUntilNeeded(Daemons.java:297)
	  - locked <0x163f> (a java.lang.Daemons$FinalizerWatchdogDaemon)
	  at java.lang.Daemons$FinalizerWatchdogDaemon.runInternal(Daemons.java:277)
	  at java.lang.Daemons$Daemon.run(Daemons.java:103)
	  at java.lang.Thread.run(Thread.java:764)

"Thread-3@4682" prio=5 tid=0x84 nid=NA runnable
  java.lang.Thread.State: RUNNABLE
	  at libcore.io.Linux.accept(Linux.java:-1)
	  at libcore.io.BlockGuardOs.accept(BlockGuardOs.java:64)
	  at android.system.Os.accept(Os.java:43)
	  at android.net.LocalSocketImpl.accept(LocalSocketImpl.java:336)
	  at android.net.LocalServerSocket.accept(LocalServerSocket.java:90)
	  at com.android.tools.fd.runtime.Server$SocketServerThread.run(Server.java:157)
	  at java.lang.Thread.run(Thread.java:764)

"ReferenceQueueDaemon@4672" daemon prio=5 tid=0x7b nid=NA waiting
  java.lang.Thread.State: WAITING
	 blocks ReferenceQueueDaemon@4672
	  at java.lang.Object.wait(Object.java:-1)
	  at java.lang.Daemons$ReferenceQueueDaemon.runInternal(Daemons.java:178)
	  - locked <0xc43> (a java.lang.Class)
	  at java.lang.Daemons$Daemon.run(Daemons.java:103)
	  at java.lang.Thread.run(Thread.java:764)

"HeapTaskDaemon@4670" daemon prio=5 tid=0x7e nid=NA waiting for monitor entry
  java.lang.Thread.State: BLOCKED
	  at dalvik.system.VMRuntime.runHeapTasks(VMRuntime.java:-1)
	  at java.lang.Daemons$HeapTaskDaemon.runInternal(Daemons.java:461)
	  at java.lang.Daemons$Daemon.run(Daemons.java:103)
	  at java.lang.Thread.run(Thread.java:764)

"queued-work-looper@5350" prio=5 tid=0x88 nid=NA runnable
  java.lang.Thread.State: RUNNABLE
	  at android.os.MessageQueue.nativePollOnce(MessageQueue.java:-1)
	  at android.os.MessageQueue.next(MessageQueue.java:325)
	  at android.os.Looper.loop(Looper.java:142)
	  at android.os.HandlerThread.run(HandlerThread.java:65)

"Jit thread pool worker thread 0@4667" daemon prio=5 tid=0x78 nid=NA runnable
  java.lang.Thread.State: RUNNABLE
	 Incompatible thread state: thread not suspended

"Signal Catcher@4668" daemon prio=5 tid=0x79 nid=NA waiting
  java.lang.Thread.State: WAITING
	 Incompatible thread state: thread not suspended

"Binder:2608_1@4673" prio=5 tid=0x7f nid=NA runnable
  java.lang.Thread.State: RUNNABLE
	 Incompatible thread state: thread not suspended

"Binder:2608_2@4674" prio=5 tid=0x80 nid=NA runnable
  java.lang.Thread.State: RUNNABLE
	 Incompatible thread state: thread not suspended

"Profile Saver@4676" daemon prio=5 tid=0x81 nid=NA runnable
  java.lang.Thread.State: RUNNABLE
	 Incompatible thread state: thread not suspended

"RenderThread@5032" prio=5 tid=0x85 nid=NA runnable
  java.lang.Thread.State: RUNNABLE
	 Incompatible thread state: thread not suspended

"hwuiTask2@5066" prio=5 tid=0x86 nid=NA runnable
  java.lang.Thread.State: RUNNABLE
	 Incompatible thread state: thread not suspended

"hwuiTask1@5067" prio=5 tid=0x87 nid=NA runnable
  java.lang.Thread.State: RUNNABLE
	 Incompatible thread state: thread not suspended

"Binder:2608_3@5433" prio=5 tid=0x89 nid=NA runnable
  java.lang.Thread.State: RUNNABLE
	 Incompatible thread state: thread not suspended

"Binder:2608_4@5434" prio=5 tid=0x8a nid=NA runnable
  java.lang.Thread.State: RUNNABLE
	 Incompatible thread state: thread not suspended

