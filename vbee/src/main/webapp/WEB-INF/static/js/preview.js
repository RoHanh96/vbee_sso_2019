var id_preview;
$(document).ready(function(){
	normalizationArticle();
	catchEventClickCompleteNormalize();
});

function catchEventClickCompleteNormalize(){
	$('#btn-completed').click(function(){
		// if press 'OK' button
		if(confirm("Xác nhận hoàn thành việc kiểm tra chính tả ?")){
			var button_complete = $(this);
			var id = $('#article_id').val();
			$.ajax({
				type: 'GET',
				url: dns + '/newspapers/update-nsw?id=' + id,
				error: function(error){
					console.log('error');
				},
				success: function(response){
					if(response.status == 1){
						button_complete.html('<span class="fa fa-check">&nbsp; Đã hoàn thành</span>');
					}
				}
			});
		}
	});
}

function normalizationArticle() {
	$('.btn-normalize').click(function() {
		var id = $('#article_id').val();
		$.ajax({
			type: 'GET',
			url: dns + '/api/v1/articles/' + id + '?fields=previewValue',
			error: function(error){
				console.log('error');
			},
			success: function(response){
				if(response.status == 1){
					var words = response.results.previewValue;
					if(words == null || words == '' || words == undefined){
						$.notify({
							title : '<strong>Tin nhắn: </strong>',
							message : 'Bài báo chưa được chuẩn hóa !!!'
						}, {
							// setting
							delay : 5000,
							type : 'danger'
						});
					}else{
						words = JSON.parse(words);
						showPreviewPopup(words, id);
					}
				}
			}
		});
	});
}

function showPreviewPopup(words, id) {
	words = words.words;
	$('.expandation-form').hide();
	if (words.length === 0) {
		$('.notify-preview').show();
		$('.standardize-content').prop('disabled', true);
	}
	if (words === undefined) {
		words = new Array();
	}
	if(words.length == 0){
		$.notify({
			title : '<strong>Tin nhắn: </strong>',
			message : 'Bài báo không chứa từ cần chuẩn hóa !!!'
		}, {
			// setting
			delay : 5000,
			type : 'success'
		});
		return;
	}
	$('.section').hide();
	var previewModal = $('#preview');
	var rowParent = previewModal.find('.parent');
	var previewBody = previewModal.find('tbody');
	previewBody.html('');
	for (var i = 0; i < words.length; i++) {
		var row = rowParent.clone().removeClass('parent');
		row.show();
		var id_name = 'preview_' + (i + 1);
		row.attr('id', id_name);
		row.find('.order').html((i + 1));
		row.find('.word').html(words[i].key);
		row.find('.expandation').html(words[i].expandation);
		if (words[i].status == 'checked') {
			row.find('.mark-check').hide();
			row.find('.checked').show();
			row.find('.status').val('checked');
		}
		if(words[i].doubt){
			row.addClass('table-danger');
		}
		row.find('.doubt').html(words[i].doubt);
		previewModal.modal('show');
		previewBody.append(row);
		for (var j = 0; j < words[i].expandations.length; j++) {
			var expandation = words[i].expandations[j];
			var expandiatonRow = rowParent.clone().removeClass('parent')
					.addClass('child').addClass(id_name);
			expandiatonRow.find('.action-show-more').html('');
			expandiatonRow.find('.check-complete').html('');
			expandiatonRow.find('.word').html(expandation['context']);
			expandiatonRow.find('.expandation')
					.html(expandation['expandation']);
			expandiatonRow.find('.nsw_precise').html(expandation.nsw_precise);
			previewBody.append(expandiatonRow);
		}
	}
	rowParent.hide();
	previewModal.modal('show');
	id_preview = $.now();
	catchEventClickMarkChecked(id_preview);
	catchEventPreviewShowMore(id_preview);
	catchEventPreviewEdit(id_preview);
	catchEventPreviewExpandationSave(id_preview)
	catchEventPreviewExpandationSaveActionEnter(id_preview);
	catchEvenPreviewSave(words, id, id_preview);
}

function catchEventClickMarkChecked(id_time) {
	$('.mark-check').click(function() {
		if (id_time != id_preview)
			return;
		$(this).hide();
		var parent = $(this).parents('.check-complete');
		parent.find('.status').val("checked");
		parent.find('.checked').show();
	});
}

function catchEventPreviewShowMore(id_time) {
	$('#preview .show-more').on(
			'click',
			function() {
				if (id_time != id_preview)
					return;
				var row = $(this).parents('tr');
				var showMore = row.find('.show-more');
				if (showMore.hasClass('fa-chevron-circle-right')) {
					$('#preview .child').fadeOut(
							'fast',
							function() {
								$('#preview .fa-chevron-circle-down')
										.removeClass('fa-chevron-circle-down')
										.addClass('fa-chevron-circle-right');
								$('.child.' + row.attr('id')).fadeIn('slow');
								row.find('.show-more-icon').removeClass(
										'fa-chevron-circle-right').addClass(
										'fa-chevron-circle-down');
							});

				} else {
					$('#preview .child').fadeOut('fast');
					$('#preview .fa-chevron-circle-down').removeClass(
							'fa-chevron-circle-down').addClass(
							'fa-chevron-circle-right');
				}
			});
}

function catchEventPreviewEdit(id_time) {
	$('#preview .edit').on(
			'click',
			function() {
				if (id_time != id_preview)
					return;
				var row = $(this).parents('tr');
				var expandationForms = $('.expandation-form:visible');
				row.find('.expandation').hide();
				row.find('.expandation-form input').val(
						row.find('.expandation').text());
				row.find('.expandation-form').show();
				row.find('.expandation-form input').focus();

				$.each(expandationForms, function() {
					var currentRow = $(this).parents('tr');
					if (currentRow.attr('id') !== row.attr('id')) {
						$(this).fadeOut(
								'fast',
								function() {
									currentRow.find('.expandation').html(
											$(this).find(
													'.expandation-form input')
													.val());
									currentRow.find('.expandation').show();
								});
					}
				});
			});
}

function catchEventPreviewExpandationSaveActionEnter(id_time){
	$('.expandation-text').on("keypress", function(e) {
		if (id_time != id_preview)
			return;
        /* ENTER PRESSED*/
        if (e.keyCode == 13) {
			var row = $(this).parents('tr');
			row.find('.mark-check').hide();
			row.find('.status').val("checked");
			row.find('.checked').show();
			var expandation = row.find('.expandation');
			var expandationInput = row.find('.expandation-form input');
			if (expandation.text() !== expandationInput.val()) {
				expandation.html(expandationInput.val());
				if (!row.hasClass('child')) {
					$('.child.' + row.attr('id') + ' .expandation').html(
							expandationInput.val());
				}
			}
			row.find('.expandation-form').hide();
			expandation.show();	           
        }
    });
}

function catchEventPreviewExpandationSave(id_time) {
	$('#preview .expandation-save').on('click', function() {
		// console.log('expandation save');
		if (id_time != id_preview)
			return;
		var row = $(this).parents('tr');
		row.find('.mark-check').hide();
		row.find('.status').val("checked");
		row.find('.checked').show();
		var expandation = row.find('.expandation');
		var expandationInput = row.find('.expandation-form input');
		if (expandation.text() !== expandationInput.val()) {
			expandation.html(expandationInput.val());
			if (!row.hasClass('child')) {
				$('.child.' + row.attr('id') + ' .expandation').html(
						expandationInput.val());
			}
		}
		row.find('.expandation-form').hide();
		expandation.show();
	});
}

function catchEvenPreviewSave(words, id, id_time) {
	$('.save-preview').on('click', function(event) {
		if (id_time != id_preview)
			return;
		var jsonPreview = encodingPreviewItem(words);
		var data = {
			'id' : id,
			'previewValue' : jsonPreview
		};
		$.ajax({
			type : 'PUT',
			url : dns + "/newspapers/update-preview",
			data : JSON.stringify(data),
			dataType : 'json',
			contentType : "application/json"
		}).done(function(response) {
			// console.log('save result: ' + response);

			if (response.status == 1) {
				$('.preview-article').val(jsonPreview);
				$.notify({
					title : '<strong>System Message: </strong>',
					message : 'Lưu thành công'
				}, {
					// setting
					delay : 5000,
					type : 'success'
				});
			} else {
				$.notify({
					title : '<strong>Error Message: </strong>',
					message : 'Lưu thất bại. Hãy thử lại!!!'
				}, {
					// setting
					delay : 5000,
					type : 'danger'
				});
			}
		});
	});
}

function encodingPreviewItem(words) {
	var value = [];
	for (var i = 0; i < words.length; i++) {
		var word = {};
		var id_name = 'preview_' + (i + 1);
		var row = $('#' + id_name);
		word['key'] = row.find('.word').html();
		word['expandation'] = row.find('.expandation').html();
		word['status'] = row.find('.status').val();
		if(row.find('.doubt').html() == 'true'){
			word['doubt'] = true;	
		}else{
			word['doubt'] = false;
		}
		var expandations = [];
		$('.' + id_name).each(function() {
			var child = {};
			child['context'] = $(this).find('.word').html();
			child['expandation'] = $(this).find('.expandation').html();
			child['nsw_precise'] = parseFloat($(this).find('.nsw_precise').html());
			expandations.push(child);
		});
		word["expandations"] = expandations;
		value.push(word);
	}
	var data = {};
	data['words'] = value;
	var jsonPreview = JSON.stringify(data);
	return jsonPreview;
}