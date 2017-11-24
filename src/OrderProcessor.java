import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import orders.*;

public class OrderProcessor {
	private static JMSConsumer consumer = new JMSConsumer();
	private static JMSProducer bookingProducer = new JMSProducer();
	private static JMSProducer newTripProducer = new JMSProducer();
	
	private static final String consumerQueue = "jms/mdw/hw5/ordersQueue";
	private static final String bookingProducerQueue = "jms/mdw/hw5/bookingsQueue";
	private static final String newTripProducerQueue = "jms/mdw/hw5/newTripsQueue";
	
	public static void main(String[] args) {
		
		consumer.addOnMessageCallback(new Callback() {
			@Override
			public void call(Object... args) {
				onMessageReceived((Message)args[0]);
			}
		});
		
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
	}
	
	public static void onMessageReceived(Message msg) {
		ObjectMessage objMsg = (ObjectMessage)msg;
		try {
			Order order = (Order)objMsg.getObject();
			processOrder(order);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public static void processOrder(Order order) {
		long orderID = order.getID();
		if (order instanceof Booking) {
			System.out.println(String.format("Order %d is a booking.", orderID));
			try {
				bookingProducer.send(bookingProducerQueue, order);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (order instanceof NewTrip) {
			System.out.println(String.format("Order %d is a new trip.", orderID));
			try {
				newTripProducer.send(newTripProducerQueue, order);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
