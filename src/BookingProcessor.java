import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import orders.Booking;

public class BookingProcessor {
	final static JMSConsumer consumer = new JMSConsumer();
	final static String consumerQueue = "jms/mdw/hw5/bookingsQueue";
	
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
			Booking booking = (Booking)objMsg.getObject();
			long bookingID = booking.getID();
			System.out.println(String.format("Booking %d processed.", bookingID));
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
