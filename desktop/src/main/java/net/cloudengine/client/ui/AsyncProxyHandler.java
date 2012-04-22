package net.cloudengine.client.ui;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;


public class AsyncProxyHandler<T> implements InvocationHandler {

	private static final int EXEC_FETCHER_THREADS = 4;
	private static ThreadPoolExecutor executor;
	
	static {
		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
		ThreadFactory threadFactory = new ThreadFactory( ) {
	        public Thread newThread(Runnable r) {
	            Thread t = new Thread(r);
	            t.setName("Async Method exec" + t.getId() + " " + System.identityHashCode(t));
	            t.setDaemon(false);
	            return t;
	        }
	    };
	    executor = new ThreadPoolExecutor(EXEC_FETCHER_THREADS, 16, 2, TimeUnit.SECONDS, workQueue, threadFactory);
	}
	
	private T controller;
	private Callback callback;
	private IProgressMonitor monitor = null;
	
	public AsyncProxyHandler(T controller, Callback callback,IProgressMonitor monitor) {
		this.controller = controller;
		this.callback = callback;
		this.monitor = monitor;
	}
	
	public AsyncProxyHandler(T controller, Callback callback) {
		this.controller = controller;
		this.callback = callback;
	}
	
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// FIXME hacer tratamiento de errores.
		Runnable run = new MethodExecutor<T>(controller, method, args, callback, monitor);
		executor.execute(run);
		return null;
	}	
}