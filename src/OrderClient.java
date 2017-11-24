import orders.*;

public class OrderClient {
	public static void main(String[] args) {
		final String producerQueue = "jms/mdw/hw5/ordersQueue";
		JMSProducer producer = new JMSProducer();
		for (int i = 0; i < 20; i++) {
			Order order;
			if (Math.random() < 0.5) {
				order = new Booking();
			} else {
				order = new NewTrip();
			}
			try {
				System.out.println(String.format("Sending order with ID %d.", order.getID()));
				producer.send(producerQueue, order);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
