package net.cloudengine.service.amqp;

public interface AMQPService {
	
	public boolean send(String message, EndPoint endPoint);

}
