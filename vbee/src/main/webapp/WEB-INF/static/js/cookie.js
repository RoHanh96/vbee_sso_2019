$(document).ready(function(){
	catchEventLogout();
});

function catchEventLogout(){
	$('#logout').click(function(){
		$.ajax({
			url: dns + '/logout-cms',
			type: 'GET',
			error: function(error){
			},
			success: function(response){
				if(response.status == 1){
					window.location.href = response.results;
				}			
			}
		});
	});
}

function deleteCookie(name, path, domain) {
    if (getCookie(name)) document.cookie = name + '=' +
        ((path) ? ';path=' + path : '') +
        ((domain) ? ';domain=' + domain : '') +
        ';expires=Thu, 01-Jan-1970 00:00:01 GMT';
}

function setCookie(cname, cvalue, exdays) {
    var d = new Date(moment().format());
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+ d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

//var token = getCookie('access-token');
//$.ajax({
//	url: 'https://maps.baonoivn.vn/api/0.1/user/check_authen',
//	type: 'GET',
//	beforeSend: function (xhr) {
//        xhr.setRequestHeader('Authorization', token);
//    },
//    error: function(data, textStatus, xhr){
//    	window.location.href = 'demo3.com';
//    },
//    success: function(response){
//    	if(response.status != 1 || response.role != 2){
//    		window.location.href = 'demo3.com';
//    		return;
//    	}
//    }
//});



