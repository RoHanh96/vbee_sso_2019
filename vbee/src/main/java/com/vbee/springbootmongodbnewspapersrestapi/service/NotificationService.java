package com.vbee.springbootmongodbnewspapersrestapi.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	// The SimpMessagingTemplate is used to send Stomp over WebSocket messages.
//	@Autowired
//	private SimpMessagingTemplate messagingTemplate;

	/**
	 * Send notification to users subscribed on channel "/user/queue/notify".
	 *
	 * The message will be sent only to the user with the given username.
	 * 
	 * @param notification
	 *            The notification message.
	 */
//	public void notify(Notification notification) {
//		messagingTemplate.convertAndSend("/queue/notify", notification);
//		return;
//	}
}