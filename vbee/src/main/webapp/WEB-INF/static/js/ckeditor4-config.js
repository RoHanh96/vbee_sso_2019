$(document).ready(function(){
	CKEDITOR.replace( 'editor', {
		language : 'vi',
		filebrowserUploadUrl: 'http://s1.store.baonoivn.xyz/upload.php',
		toolbar: [
			{ name: 'document', groups: [ 'mode', 'document', 'doctools' ], items: [ 'Source', '-', 'Save', 'NewPage', 'Preview', 'Print', '-', 'Templates' ] },
			{ name: 'clipboard', groups: [ 'clipboard', 'undo' ], items: [ 'Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'Undo', 'Redo' ] },
			{ name: 'editing', groups: [ 'find', 'selection', 'spellchecker' ], items: [ 'Find', 'Replace', '-', 'SelectAll', '-', 'Scayt' ] },
			{ name: 'forms', items: [ 'Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField' ] },
			'/',
			{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ], items: [ 'Bold', 'Italic', 'Underline', 'Strike', 'Subscript', 'Superscript', '-', 'CopyFormatting', 'RemoveFormat' ] },
			{ name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'align', 'bidi' ], items: [ 'NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', '-', 'Blockquote', 'CreateDiv', '-', 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock', '-', 'BidiLtr', 'BidiRtl', 'Language' ] },
			{ name: 'links', items: [ 'Link', 'Unlink', 'Anchor' ] },
			{ name: 'insert', items: [ 'Image', 'Flash', 'Table', 'HorizontalRule', 'Smiley', 'SpecialChar', 'PageBreak', 'Iframe' ] },
			'/',
			{ name: 'styles', items: [ 'Styles', 'Format', 'Font', 'FontSize' ] },
			{ name: 'colors', items: [ 'TextColor', 'BGColor' ] },
			{ name: 'tools', items: [ 'Maximize', 'ShowBlocks' ] },
			{ name: 'others', items: [ '-' ] },
			{ name: 'about', items: [ 'About' ] }
		]
	});

	// Cấu hình lại màu nền giao diện.
	CKEDITOR.config.uiColor = '#9AB8F3';
	CKEDITOR.config.resize_enabled = false;
	CKEDITOR.config.height = '500px';
	CKEDITOR.config.extraPlugins = 'uploadimage';
	CKEDITOR.config.uploadUrl = 'http://s1.store.baonoivn.xyz/upload.php';
	CKEDITOR.config.removeDialogTabs = 'image:advanced';
	var urlCkeditor = '';
	CKEDITOR.instances.editor.on( 'fileUploadRequest', function( evt ) {
	    var fileLoader = evt.data.fileLoader;
		var size = fileLoader.file.size;
		if(size/1024/1024 > 1){
			$.notify({
				title : '<strong>Tin nhắn: </strong>',
				message : 'File upload chỉ được nhỏ hơn 1MB !!!'
			}, {
				// setting
				delay : 5000,
				type : 'danger'
			});
			urlCkeditor = '';
			return;		
		}
		var ValidImageTypes = ["image/gif", "image/jpeg", "image/png", "image/jpeg"];
		if ($.inArray(fileLoader.file.type, ValidImageTypes) < 0) {
			$.notify({
				title : '<strong>Tin nhắn: </strong>',
				message : 'File upload chỉ được định dạng jpg, png, jpeg, gif !!!'
			}, {
				// setting
				delay : 5000,
				type : 'danger'
			});
			urlCkeditor = '';
			return;		
		}
    	var formData =  new FormData();
		var filename = string_to_slug(fileLoader.fileName);
		urlCkeditor = 'http://s1.store.baonoivn.xyz/articles/contents/' + filename;
		var blob = fileLoader.file.slice(0, -1, fileLoader.file.type); 
		var newFile = new File([blob], filename, {type: fileLoader.file.type});
		
		formData.append('dir', 'articles/contents');
		formData.append('audio', newFile);
		$.ajax({
			url : 'http://s1.store.baonoivn.xyz/upload.php',
			type : 'POST',
			data : formData,
			processData : false,
			contentType : false,
			error: function(){
				alert('Xảy ra lỗi! Vui lòng thử lại sau !!!');
			},
			success: function(response){
				console.log(response);
				if (response == 'upload file complete') {
					
				}else{
					$.notify({
					title : '<strong>Tin nhắn: </strong>',
					message : 'Lỗi upload từ server !!!'
					}, {
						// setting
						delay : 5000,
						type : 'danger'
					});
				}
			}			
		});
    	fileLoader.xhr.send();
	    // Prevented the default behavior.
	  	evt.stop();		
	});
		
	CKEDITOR.instances.editor.on( 'fileUploadResponse', function( evt ) {
	    // Prevent the default response handler.
	    evt.stop();
	    evt.data.url = urlCkeditor;
	    	
	
	});
});