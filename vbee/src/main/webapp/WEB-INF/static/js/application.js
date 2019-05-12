var dns = document.location.origin;
var socket;
var isCreated = false;
moment.locale('vi');
$(document).ready(function() {
	goBack();
	catchEventUploadImage();
	catchEventCreateNews();
	catchEventUpdateNews();
	catchEventReSynthesize();
	catchEventFocus();
	catchEventCKEditor();
	catchEventSummary();
	catchEventSynthesizeWhenCreated();
	catchEventReNormalize();
	catchEventUploadThumbnailAndSaveImage();
});

function catchEventUploadThumbnailAndSaveImage(){
	$('#submit-upload-image-article').submit(function(evt) {
		evt.preventDefault();
		var data;
		data = new FormData();
		var url;
		var file = $('.input-image')[0].files[0];
		var filename = string_to_slug($('.input-image').val().split(/(\\|\/)/g).pop());
		url = 'http://s1.store.baonoivn.xyz/articles/thumbnails/' + filename;
		var blob = file.slice(0, -1, file.type); 
		var newFile = new File([blob], filename, {type: file.type});
		data.append('dir', 'articles/thumbnails');
		data.append('audio', newFile);
		$.ajax({
			url : 'http://store.thuyetminhphim.vn/upload.php',
			type : 'POST',
			data : data,
			processData : false,
			contentType : false,
			error: function(){
				alert('error');
			},
			success: function(response){ 
				console.log(response);
				if (response == 'upload file complete') {
					$('#image-render').attr('src', url);
					saveImageArticle();
				}else{
					$.notify({
						title : '<strong>Tin nhắn: </strong>',
						message : 'Upload thất bại !!!'
					}, {
						// setting
						delay : 5000,
						type : 'danger'
					});
				}
			}
		});
	});
}

function saveImageArticle(){
	var article = {
		'id' : parseInt($('#article_id').val()),
		'picture': $('#image-render').attr('src')
	}
	$.ajax({
		type: 'PUT',
		url: dns + '/newspapers/update-image',
		data: JSON.stringify(article),
		contentType : "application/json",
		dataType : "json",
		error: function(error){
			alert('error');
		},
		success: function(response){
			if(response.status == 1){
				$.notify({
					title : '<strong>Tin nhắn: </strong>',
					message : 'Upload và lưu ảnh đại diện thành công !!!'
				}, {
					// setting
					delay : 5000,
					type : 'success'
				});
			}else{
				$.notify({
					title : '<strong>Tin nhắn: </strong>',
					message : 'Lưu thất bại !!!'
				}, {
					// setting
					delay : 5000,
					type : 'success'
				});
			}
		}
	});
}

function catchEventReNormalize(){
	$('.btn-re-normalize').click(function(){
		$(this).html('<i class="fa fa-spinner fa-spin"></i> Đang xử lý');
		$(this).prop('disabled', true);
		var button = $(this);
		var article = {
			'id' : parseInt($('#article_id').val())
		}
		$.ajax({
			type: 'POST',
			url: dns + '/newspapers/normalization',
			data: JSON.stringify(article),
			contentType : "application/json",
			dataType : "json",
			error: function(error){
				alert('error');
				button.html('Chuẩn hóa lại');
				button.prop('disabled', false);
			},
			success: function(response){
				button.html('Chuẩn hóa lại');
				button.prop('disabled', false);
				if(response.status == 1){
					$.notify({
						title : '<strong>Tin nhắn: </strong>',
						message : 'Bài báo đang được chuẩn hóa. Vui lòng chờ 5 giây !!!'
					}, {
						// setting
						delay : 5000,
						type : 'success'
					});
				}
			}
		});
	});
}

function catchEventSynthesizeWhenCreated(){
	$('.btn-synthesis').click(function(){
		if(isCreated){
			var button = $(this);
			$(this).html('<i class="fa fa-spinner fa-spin"></i> Đang xử lý');
			$(this).prop('disabled', true);
			var article = {
				'id' : parseInt($('#article_id').val())
			}
			console.log(article);
			$.ajax({
				type: 'POST',
				url: dns + '/newspapers/synthesis',
				data: JSON.stringify(article),
				contentType : "application/json",
				dataType : "json",
				error: function(error){
					alert('error');
					button.html('Tổng hợp');
					button.prop('disabled', false);
				},
				success: function(response){
					if(response.status == 1){
						button.html('<i class="fa fa-spinner fa-spin"></i> Đang xử lý');
						button.prop('disabled', false);
						$.notify({
							title : '<strong>Tin nhắn: </strong>',
							message : 'Bài báo đang được tổng hợp !!!'
						}, {
							// setting
							delay : 5000,
							type : 'success'
						});
						setTimeout(function(){
					  		window.location.href = dns + '/newspapers';
						},2000);
					}
				}
			});
		}else{
			$.notify({
				title : '<strong>Tin nhắn: </strong>',
				message : 'Vui lòng thêm mới bài báo trước khi tổng hợp !!!'
			}, {
				// setting
				delay : 5000,
				type : 'danger'
			});
		}
	});
}

function catchEventUpdateNews(){
	$('.btn-save-synthesis').click(function(){
		var urlImage = $('#image-render').attr('src');
		$(this).html('<i class="fa fa-spinner fa-spin"></i> Đang xử lý');
		$(this).prop('disabled', true);
		var button_save = $(this);
		if(urlImage.trim() != ''){
			updateNewspaperAndSynthesis(button_save);
			return false;
		}
		
		var data;
		data = new FormData();
		var url;
		var file = $('.input-image')[0].files[0];
		var filename = string_to_slug($('.input-image').val().split(/(\\|\/)/g).pop());
		url = 'http://s1.store.baonoivn.xyz/articles/thumbnails/' + filename;
		var blob = file.slice(0, -1, file.type); 
		var newFile = new File([blob], filename, {type: file.type});
		data.append('dir', 'articles/thumbnails');
		data.append('audio', newFile);
		$.ajax({
			url : 'http://s1.store.baonoivn.xyz/upload.php',
			type : 'POST',
			data : data,
			processData : false,
			contentType : false,
			error: function(){
				alert('error');
			},
			success: function(response){
				console.log(response);
				if (response == 'upload file complete') {
					$('#image-render').attr('src', url);
					updateNewspaperAndSynthesis(button_save);
				}else{
					$.notify({
						title : '<strong>Tin nhắn: </strong>',
						message : 'Bài báo chưa có ảnh đại diện !!!'
					}, {
						// setting
						delay : 5000,
						type : 'danger'
					});
					button_save.html('Thêm mới và chuẩn hóa');
					button_save.prop('disabled', false);
				}
			}			
		});
	
	});
}


function goBack() {
	$(".btn-back").click(function(event) {
		if (history.length == 1) {
			return false;
		} else {
			event.preventDefault();
			history.back(1);
		}
	});
}

function catchEventUploadImage() {
	$('.btn-image-upload').on('click', function() {
		$('.input-image').click();
	});
}

function readURL(input) {
	if (input.files && input.files[0]) {
		$('.article_image_alert_size').hide();
		$('.article_image_alert_empty').hide();
		var reader = new FileReader();
		var fsize = input.files[0].size;
		reader.onload = function(e) {
			$('#image-render').attr('src', e.target.result).width(140).height(
					90);
		};

		reader.readAsDataURL(input.files[0]);
		if((fsize/1024/1024) > 1)
			$('.article_image_alert_size ').show();
		$('#btn-upload').show();
	}
}

function validateFields(){
	var pass = true;
	if($('.article_title').val().trim() == ''){
		$('.article_title_alert').show();
		pass = false;
	}
	if($('.article_lead').val().trim() == ''){
		$('.article_lead_alert').show();
		pass = false;
	}
	if(CKEDITOR.instances.editor.document.getBody().getText().trim() == ''){
		$('.article_content_alert').show();
		pass = false;
	}
	if($('#image-render').attr('src') == ''){
		$('.article_image_alert_empty').show();
		pass = false;
	}
	if($('.article_image_alert_size').css('display') == 'block'){
		pass = false;
	}
	return pass;
}

function updateNewspaperAndSynthesis(button){
	if(!validateFields()){
		$.notify({
			title : '<strong>Tin nhắn: </strong>',
			message : 'Vui lòng điền đầy đủ thông tin bài báo !!!'
		}, {
			// setting
			delay : 5000,
			type : 'danger'
		});
		return;
	}
	var tags =[];
	var tagArray = $('.article_tags').val().split(',');
	for(var i = 0; i < tagArray.length; i++){
		var tag = {
			'name' : tagArray[i]
		};
		tags.push(tag);
	}
	var category = {
		'id' : parseInt($('#category').find(':selected').val()),
		'name' : $('#category').find(':selected').text()
	};
	var article = {
		'id' : $('#article_id').val(),
		'websiteName': 'Etadi News',
		'picture' : $('#image-render').attr('src'),
		'title' : $('.article_title').val(),
		'lead' : $('.article_lead').val(),
		'content' : CKEDITOR.instances.editor.getData(),
		'text' : CKEDITOR.instances.editor.document.getBody().getText().trim(),
		'category': category,
		'tags' : tags
		
	};
	synthesizeArticle(article, button);
}

function catchEventReSynthesize(){
	$('.btn-re-synthesis').click(function(){
		var article = {
			'id' : $('#article_id').val(),
		};
		synthesizeArticle(article, $(this));
	});
}

function synthesizeArticle(article, button){
	console.log(article);
	$.ajax({
		url : dns + '/newspapers',
		method: 'PUT',
		data: JSON.stringify(article),
		contentType : "application/json",
		dataType : "json",
		error: function(){
			console.log('error !!!');
			button.html('Tổng hợp');
			button.prop('disabled', false);
		},
		success: function(response){
			if(response.status == 1){
				button.html('Tổng hợp');
				button.prop('disabled', false);
				$.notify({
					title : '<strong>Tin nhắn: </strong>',
					message : 'Sửa báo thành công. Báo đang được tổng hợp lại... !!!'
				}, {
					// setting
					delay : 5000,
					type : 'success'
				});
				
				setTimeout(function(){
					  window.location.href = dns + '/newspapers';
				},2000);
			}
		} 
	});	
}

function createNewspapersAndNormalize(button){
	var tags =[];
	var tagArray = $('.article_tags').val().split(',');
	for(var i = 0; i < tagArray.length; i++){
		var tag = {
			'name' : tagArray[i]
		};
		tags.push(tag);
	}
	var category = {
		'id' : parseInt($('#category').find(':selected').val()),
		'name' : $('#category').find(':selected').text()
	};
	var publicDate = new Date(moment().format()).getTime();
	var article = {
		'websiteName': $('#website').find(':selected').text(),
		'websiteId': parseInt($('#website').find(':selected').val()),
		'url': $('.article_url').val(),
		'picture' : $('#image-render').attr('src'),
		'title' : $('.article_title').val(),
		'lead' : $('.article_lead').val(),
		'content' : CKEDITOR.instances.editor.getData(),
		'text' : $('.article_title').val() + '. ' + CKEDITOR.instances.editor.document.getBody().getText().trim(),
		'category': category,
		'tags' : tags,
		'publicDate' : publicDate
		
	};
	$.ajax({
		url : dns + '/newspapers',
		method: 'POST',
		data: JSON.stringify(article),
		contentType : "application/json",
		dataType : "json",
		error: function(){
			alert('error !!!');
		},
		success: function(response){
			if(response.status == 1){
				button.html('Thêm mới và chuẩn hóa');
				button.prop('disabled', true);
				isCreated = true;
				$('#article_id').val(response.results.id);
				$('.btn-normalize').removeClass('none-display');
				$('.btn-synthesis').removeClass('none-display');
				$.notify({
				title : '<strong>Tin nhắn: </strong>',
				message : 'Thêm mới thành công. Báo đã được chuẩn hóa !!!'
				}, {
					// setting
					delay : 5000,
					type : 'success'
				});
			}else{
				button.html('Thêm mới và chuẩn hóa');
				button.prop('disabled', false);
				$.notify({
				title : '<strong>Tin nhắn: </strong>',
				message : 'Tiêu đề bài báo đã bị trùng. Xin vui lòng chọn bài khác !!!'
				}, {
					// setting
					delay : 5000,
					type : 'danger'
				});
			}
		} 
	});	
}

function catchEventCreateNews(){
	$('.btn-add').click(function(){
		if(!validateFields()){
			$.notify({
				title : '<strong>Tin nhắn: </strong>',
				message : 'Vui lòng điền đầy đủ thông tin bài báo !!!'
			}, {
				// setting
				delay : 5000,
				type : 'danger'
			});
			return;
		}
		
		$(this).html('<i class="fa fa-spinner fa-spin"></i> Đang xử lý');
		$(this).prop('disabled', true);
		var button_add = $(this);
		button_add.html()
		var data =  new FormData();
		var url;
		var file = $('.input-image')[0].files[0];
		var filename = string_to_slug($('.input-image').val().split(/(\\|\/)/g).pop());
		url = 'http://s1.store.baonoivn.xyz/articles/thumbnails/' + filename;
		var blob = file.slice(0, -1, file.type); 
		var newFile = new File([blob], filename, {type: file.type});
		data.append('dir', 'articles/thumbnails');
		data.append('audio', newFile);
		$.ajax({
			url : 'http://s1.store.baonoivn.xyz/upload.php',
			type : 'POST',
			data : data,
			processData : false,
			contentType : false,
			error: function(){
				alert('Xảy ra lỗi! Vui lòng thử lại sau !!!');
				button_add.html('Thêm mới và chuẩn hóa');
				button_add.prop('disabled', false);
			},
			success: function(response){
			if (response == 'upload file complete') {
				$('#image-render').attr('src', url);
				createNewspapersAndNormalize(button_add);
			}else{
				$.notify({
				title : '<strong>Tin nhắn: </strong>',
				message : 'Bài báo chưa có ảnh đại diện !!!'
				}, {
					// setting
					delay : 5000,
					type : 'danger'
				});
				button_add.html('Thêm mới và chuẩn hóa');
				button_add.prop('disabled', false);
			}
			}			
		});
	
	});
}

function catchEventCKEditor(){
	CKEDITOR.on('instanceReady', function(evt) {
    	var editor = evt.editor;
	    editor.on('focus', function(e) {
			$('.article_content_alert').hide();
			wordCountCkEditor();
    	});
    	editor.on('key', function(e){
    		wordCountCkEditor();
    	});
    	
	});
}

function wordCountCkEditor(){
	var total_words;
	var  total_sentences;
	if(CKEDITOR.instances.editor.document.getBody().getText().trim() == ''){
		total_words = 0;
		total_sentences = 0;
	}else {
		total_words =  CKEDITOR.instances.editor.document.getBody().getText().trim().split(/ +/).length;
		total_sentences = CKEDITOR.instances.editor.document.getBody().getText().trim().split(/[\.\?\!]\s/).length;
	}
	$('#display_count_words').html(total_words);
	$('#display_count_sentences').html(total_sentences);
}

function catchEventFocus(){
	
	$('.article_title').focus(function(){
		$('.article_title_alert').hide();
	});
	$('.article_lead').focus(function(){
		$('.article_lead_alert').hide();
	});
}

function catchEventSummary(){
	$('.btn-summary').click(function(){
		if( CKEDITOR.instances.editor.document.getBody().getText().trim().split(/[\.\?\!]\s/).length < 10){
			$.notify({
				title : '<strong>Tin nhắn: </strong>',
				message : 'Nội dung bài báo cần phải có ít nhất 10 câu để tự động tóm tắt !!!'
			}, {
				// setting
				delay : 5000,
				type : 'danger'
			});
			return;	
		}
		$(this).html('<i class="fa fa-spinner fa-spin fa-1x fa-fw"></i>Đang xử lý');
		$(this).prop('disabled', true);
		wsSummary(CKEDITOR.instances.editor.document.getBody().getText(), '.article_summary_alert', $(this));
	});
}

function wsSummary(text, desElement, button){
	try{
		socket = new WebSocket(host);
		socket.onopen = function(){
			console.log('ws on open');	
			 var data = {
	        	'INPUT_TEXT' : text,
	        	'index': '1234'
        	};
        	socket.send(JSON.stringify(data));	
		}
		socket.onmessage = function(msg){
			console.log('recieved !!!');
			console.log(JSON.parse(msg.data));
			$('' + desElement).html(msg);
			button.html('Tóm tắt tự động');
			button.prop('disabled', false);
			socket.close();
		}
		
		socket.onclose = function(){
			console.log("Stopped summary");
        }
       	
	}catch(exception){
		
	}
}

function string_to_slug (str) {
    str = str.replace(/^\s+|\s+$/g, ''); // trim
    str = str.toLowerCase();
  
    // remove accents, swap ñ for n, etc
    var from = "aAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆfFgGhHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRsStTuUùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴzZ·/_,:;";
    var to   = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabbccddddeeeeeeeeeeeeeeeeeeeeeeeeffgghhiiiiiiiiiiiijjkkllmmnnooooooooooooooooooooooooooooooooooooppqqrrssttuuuuuuuuuuuuuuuuuuuuuuuuvvwwxxyyyyyyyyyyyyzz------";
    for (var i=0, l=from.length ; i<l ; i++) {
        str = str.replace(new RegExp(from.charAt(i), 'g'), to.charAt(i));
    }

    str = str.replace(/\s+/g, '-') // collapse whitespace and replace by -
        .replace(/-+/g, '-'); // collapse dashes

    return str;
}
