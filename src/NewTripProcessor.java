import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import orders.NewTrip;

public class NewTripProcessor {
	public static JMSConsumer consumer = new JMSConsumer();
	public static final String consumerQueue = "jms/mdw/hw5/newTripsQueue";
	
	public static void main(String[] args) {
		Thread consumerThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					consumer.receive(consumerQueue);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		consumerThread.start();
		
		consumer.addOnMessageCallback(new Callback() {
			
			@Override
			public void call(Object... args) {
				onMessageReceived((Message)args[0]);
			}
		});
	}
	
	public static void onMessageReceived(Message msg) {
		ObjectMessage objMsg = (ObjectMessage)msg;
		try {
			NewTrip newTrip = (NewTrip)objMsg.getObject();
			long newTripID = newTrip.getID();
			System.out.println(String.format("New trip %d processed.", newTripID));
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
