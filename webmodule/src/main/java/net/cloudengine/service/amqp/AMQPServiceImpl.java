package net.cloudengine.service.amqp;

import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class AMQPServiceImpl implements AMQPService {
	
	private Map<String,RabbitTemplate> templates;

	public AMQPServiceImpl(Map<String, RabbitTemplate> templates) {
		super();
		this.templates = templates;
	}

	@Override
	public boolean send(String message, EndPoint endPoint) {
		RabbitTemplate template = templates.get(endPoint.name());
		if (template == null) {
			throw new IllegalArgumentException("Invalid EndPoint");
		}
		
		if (endPoint.getRountingKey() != null) {
			template.convertAndSend(endPoint.getRountingKey(), message);
		} else {
			template.convertAndSend(message);
		}
		
		return true;
	}
	

}
