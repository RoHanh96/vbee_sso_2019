$(document).ready(function(){
	// Start the web socket connection.
        connect();
});

function connect() {
    // Create and init the SockJS object
    var socket = new SockJS('/ws');
    var stompClient = Stomp.over(socket);
    stompClient.debug = null;
    // Subscribe the '/notify' channell
    stompClient.connect({}, function(frame) {
      stompClient.subscribe('/queue/notify', function(notification) {
        // Call the notify function when receive a notification
        notify(JSON.parse(notification.body).content);
      });
    });
    
    return;
  } // function connect
  
  /**
   * Display the notification message.
   */
function notify(message) {
   	$.notify({
		title : '<strong>Tin nhắn: </strong>',
		message : message
	}, {
		// setting
		delay : 5000,
		type : 'success'
	});
    return;
}

