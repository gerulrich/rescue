package net.cloudengine.service.impl;

import static net.cloudengine.dao.support.SearchParametersBuilder.forClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;

import net.cloudengine.dao.support.Repository;
import net.cloudengine.dao.support.RepositoryLocator;
import net.cloudengine.dao.support.SearchParametersBuilder;
import net.cloudengine.model.auth.User;
import net.cloudengine.model.config.AppProperty;
import net.cloudengine.model.gcm.RegisteredDevice;
import net.cloudengine.service.ConfigurationService;
import net.cloudengine.service.GCMMessage;
import net.cloudengine.service.GCMService;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

@Service
public class GCMServiceImpl implements GCMService {

	private static final Logger logger = LoggerFactory.getLogger(GCMServiceImpl.class);
	private ConfigurationService configService;
	private ExecutorService executorService;
	private Repository<RegisteredDevice, ObjectId> deviceRepository;
	
	
	@Autowired
	public GCMServiceImpl(
			ConfigurationService configService, 
			RepositoryLocator repositoryLocator,
			ExecutorService executorService) {
		super();
		this.configService = configService;
		this.deviceRepository = repositoryLocator.getRepository(RegisteredDevice.class);
		this.executorService = executorService;
	}

	@Override
	public void sendMessage(final GCMMessage msg, final User toUser) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				sendInternal(msg, toUser);
			}
		});
	}
	
	@Override
	public RegisteredDevice getDevices(User user) {
		SearchParametersBuilder builder = SearchParametersBuilder.forClass(RegisteredDevice.class);
		builder.eq("username", user.getUsername());
		return deviceRepository.findOne(builder.build());
	}
	
	@Override
	public void registerDevice(String user, String deviceId) {
		SearchParametersBuilder builder = forClass(RegisteredDevice.class);
		builder.eq("username", user);
		RegisteredDevice registeredDevice = deviceRepository.findOne(builder.build());
		if (registeredDevice == null) {
			registeredDevice = new RegisteredDevice();
			registeredDevice.setUsername(user);
			registeredDevice.setDevices(new HashSet<String>());
			registeredDevice.getDevices().add(deviceId);
			deviceRepository.save(registeredDevice);			
		} else {
			registeredDevice.getDevices().add(deviceId);
			deviceRepository.update(registeredDevice);
		}
	}

	private void sendInternal(GCMMessage msg, User toUser) {
		AppProperty property = configService.getProperty("gcm.key");
		if (property == null) {
			logger.debug("GCM key not found, skipping sending messages to devices");
			return;
		}		
		RegisteredDevice registeredDevice = this.getDevices(toUser);
		if (registeredDevice == null || registeredDevice.getDevices().isEmpty()) {
			logger.debug("The user {} has no devices registered", toUser.getUsername());
			return;
		}
		
		Sender sender = new Sender(property.getValue());
		
		Message message = new Message.Builder()
			.addData("type", msg.getType())
			.addData("message", msg.getBody())
			.addData("account", toUser.getUsername())
			.build();
		
		try {
			MulticastResult result = sender.send(message, new ArrayList<String>(registeredDevice.getDevices()), 5);
			if ( result.getSuccess() == registeredDevice.getDevices().size()) {
				logger.debug("All messages are sent successfully to user {}", toUser.getUsername());	
			} else {
				logger.debug("Total messages: {}, sent successfully to user {}: {}", 
					new Object[] { registeredDevice.getDevices().size(), result.getSuccess(), toUser.getUsername()});
			}
		} catch (IOException e) {
			logger.error("The message could not be sent", e);
		}
	}    	
}
