package hcfs.test.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;

import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.util.Log;

import com.hopebaytech.hcfsmgmt.utils.Logs;

public class EventSocketManager {
	private static final String CLASS_NAME = MgmtAppUtils.class.getSimpleName();

	private static final int SUCCESS = 1;
	private static final int FAILURE = 0;

	private List<Future<Boolean>> futureTaskList;
	private int curMaxListenerId;

	
	public EventSocketManager() {
		futureTaskList = new ArrayList<>();
		curMaxListenerId = -1;
	}
	
	public int addAndStartEventSocket(final int event, final String address) {
		try {
			Logs.i("EventSocketManager", "addAndStartEventSocket", "Create server socket");
			LocalServerSocket serverSocket = new LocalServerSocket(address);
			SocketListener eventListener = new SocketListener(event, serverSocket);
			FutureTask<Boolean> futureTask = new FutureTask<>(eventListener);

			futureTaskList.add(futureTask);
			Thread socketListenerThread = new Thread(futureTask);
			socketListenerThread.start();
			curMaxListenerId++;
			socketListenerThread.setName("thread" + curMaxListenerId);
			return curMaxListenerId;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean isReceiveEvent(final int listenerId, long timeoutSec) {
		try {
			Future<Boolean> futureTask = futureTaskList.get(listenerId);
			return futureTask.get(timeoutSec,TimeUnit.SECONDS);
		} catch (ExecutionException | InterruptedException e) {
			throw new RuntimeException(e);
		} catch (TimeoutException e) {
			return false;
		}
	}
	
	public void cleanup() {
		for(Future<Boolean> futureTask : futureTaskList) {
			//Close socket wouldn't have testServerSocket.accept() throw exception.
			//So thread wouldn't break.
			//http://stackoverflow.com/questions/8525730/how-to-abort-localserversocket-accept
			futureTask.cancel(true);
		}
	}
	
	private class SocketListener implements Callable<Boolean> {

		private int waitingEvent;
		private LocalServerSocket testServerSocket;

		SocketListener(int event, LocalServerSocket serverSocket) {
			Logs.i("EventSocketManager", "SocketListener", "Create server socket listener thread");
			this.waitingEvent = event;
			this.testServerSocket = serverSocket;
		}

		@Override
		public Boolean call() throws Exception {
			try {
				while (true) {
					Logs.d("EventSocketManager", "run", "Server socket listening");
					LocalSocket connection = testServerSocket.accept();
					if (connection != null) {
						BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						if(isFinishRunning(connection, bufferedReader.readLine()))
							return true;
						bufferedReader.close();
						connection.close();
					}
				}
			} finally {
				closeQuietly(testServerSocket);
			}
		}

		private boolean isFinishRunning(LocalSocket connection, String receiveStr) throws Exception {
			if (receiveStr != null) {
				try {
					Logs.d("EventSocketManager", "run", "Socket read line " + receiveStr);
					boolean isReceived = isReceiveEvent(receiveStr);
					connection.getOutputStream().write(SUCCESS);
					if(isReceived)
						return true;
				} catch (JSONException e) {
					connection.getOutputStream().write(FAILURE);
					Logs.d("EventSocketManager", "run", Log.getStackTraceString(e));
				}
			}
			return false;
		}

		private boolean isReceiveEvent(String jsonStr) throws JSONException {
			int[] receivedEvents = JsonUtils.getHCFSEvents(jsonStr);
			for (int event : receivedEvents) {
				if (event == this.waitingEvent)
					return true;
			}
			return false;
		}

		private void closeQuietly(LocalServerSocket resource) {
			try {
				if (resource != null) {
					resource.close();
				}
			} catch(Exception e) {
				Logs.d(CLASS_NAME, "closeQuietly", Log.getStackTraceString(e));
			}
		}
	}
}
