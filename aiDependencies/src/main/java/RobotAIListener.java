package robotarena;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

/*
The specs say "allow each robot to receive notifications" but also says make reasonable modifications to allow it
I feel that making an extention of RobotAI rather than require each AI to implement an empty tell() function fulfuls
the requirement of allowing AI to receive messages reasonably.
 */

abstract class RobotAIListener implements RobotAI {
  protected Object mutex = new Object();
  protected BlockingQueue<NotificationMessage> notificationQueue;

  public RobotAIListener() {
    notificationQueue = new ArrayBlockingQueue<NotificationMessage>(10);
  }

  public void tell(NotificationMessage notificationMessage) {
    synchronized(mutex) {
      try {
        notificationQueue.put(notificationMessage);
      } catch(InterruptedException e) {
        
      }
    }
  }
}
