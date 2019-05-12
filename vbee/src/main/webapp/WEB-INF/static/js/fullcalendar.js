$(document).ready(function(){
	fullcalendar();
	removeEvent();
	createScheduleArticles();
	catchEventChangeCategory();
	catchEventEnterSearch();
	hideshowSearchArticle();
	addButtonViewAllSchedule();
});
var currentEvent = {};
var newDataSchedule = [];
var loadData = []; 

function addButtonViewAllSchedule(){
	$('.fc-right').prepend('<button class="btn btn-primary" id="view-all">Tất cả lịch</button>');
	viewAllSchedule();
}

function viewAllSchedule(){
	$('#view-all').click(function(){
		$('#search').val('');
		$('.dropdown-search').html('');
		if(!$('.btn-schedule').hasClass('none-display')){
			$('.title_panel_article').addClass('none-display');
			$('.source_panel_article').addClass('none-display');
			$('.btn-schedule').addClass('none-display');
		}
		$('.article_title').html('');
		$('.article_source').html('');
		$('#articleId').val('');
		window.history.pushState("", "", '/advertisements');
		loadDataSchedule(); 
	});
}

function fullcalendar(){
	var date = new Date();
	var d = date.getDate();
	var m = date.getMonth();
	var y = date.getFullYear();
	var scrollTime = moment().format("HH") + ":00:00";
	var $calendar = $('#calendar').fullCalendar({
		// Default view only agendaWeek	
	    defaultView:'agendaWeek',
	    // Set scroll to current time
	    scrollTime: scrollTime,
	    // Make possible to respond to clicks and selections
	    selectable: true,
		// set timezone
		timezone: 'local',	 
		//set height
		height: 'auto',
	    // This is the callback that will be triggered when a selection is made.
	    // It gets start and end date/time as part of its arguments
	    select: function(start, end, jsEvent, view, resource) {
	    	// If user no chooice article detail
	    	if($('#articleId').val() == ''){
	    		$.notify({
						title : '<strong>Tin nhắn: </strong>',
						message : 'Vui lòng chọn bài báo để tạo mới lịch quảng cáo !!!'
				}, {
						// setting
						delay : 5000,
						type : 'danger'
				});
	    		return false;
	    	}
	    	var startTime = start._d.getTime();
	    	var endTime = end._d.getTime();
	    	//Không tạo lịch khi thời gian tạo < now
	    	if(endTime < new Date(moment().format()).getTime()){
	    		$.notify({
						title : '<strong>Tin nhắn: </strong>',
						message : 'Thời điểm kết thúc phải lớn hơn thời điểm hiện tại. Vui lòng tạo lại !!!'
				}, {
						// setting
						delay : 5000,
						type : 'danger'
				});
	    		return false;
	    	}
	    	// Chỉ được tạo lịch trong 1 ngày
	    	if( start.startOf('day').toDate().getTime() > endTime || endTime > start.endOf('day').toDate().getTime()){
	    		$.notify({
						title : '<strong>Tin nhắn: </strong>',
						message : 'Chỉ được tạo lịch trong 1 ngày. Vui lòng tạo lại !!!'
				}, {
						// setting
						delay : 5000,
						type : 'danger'
				});
	    		return false;
	    	}
	    	// Check trùng lịch
	    	for(var i = 0; i < loadData.length; i++){
			 	if((loadData[i].start < startTime) && (startTime < loadData[i].end) || 
			 		(loadData[i].start < endTime) && (endTime < loadData[i].end)){
			 		$.notify({
						title : '<strong>Tin nhắn: </strong>',
						message : 'Lịch mới không được trùng với lịch cũ !!!'
					}, {
						// setting
						delay : 5000,
						type : 'danger'
					});
					return false;
				}
			 }
			 for(var j = 0; j < newDataSchedule.length; j++){
			 	if((newDataSchedule[j].start < startTime) && (startTime < newDataSchedule[j].end) || 
			 		(newDataSchedule[j].start < endTime) && (endTime < newDataSchedule[j].end)){
			 		$.notify({
							title : '<strong>Tin nhắn: </strong>',
							message : 'Lịch mới không được trùng với lịch đã tạo !!!'
					}, {
							// setting
							delay : 5000,
							type : 'danger'
					});
					return false;
				}
			 }
	 		var title = prompt("Xác định tạo lịch mới ? Chọn vị trí quảng cáo cho báo");
			     // If did not pressed Cancel button
			     if (title != null) {
				      // Create event
				      if(isNaN(parseInt(title))){
					      	$.notify({
								title : '<strong>Tin nhắn: </strong>',
								message : 'Đầu vào không phải là dạng số. Vui lòng nhập chính xác !!!'
							}, {
									// setting
									delay : 5000,
									type : 'danger'
							});
					      	return false;
				      }
				      if(parseInt(title) > 10 || parseInt(title) < 1){
				      	$.notify({
							title : '<strong>Tin nhắn: </strong>',
							message : 'Chỉ được quảng cáo trong 10 vị trí đầu !!!'
						}, {
								// setting
								delay : 5000,
								type : 'danger'
						});
				      	return false;
				      }
				       var event = {
			            	eventId : new Date().getTime(),
				       		title: $('.article_title').html(),
				       		start: startTime,
				       		end: endTime,
				       		position: parseInt(title),
				       		articleId : parseInt($('#articleId').val()),
				       		categoryId : $('#category_advertisements').find(':selected').val(),
				       		color  : '#3ba83c'
			      		};
	 				
				      // Push event into fullCalendar's array of events
				      // and displays it. The last argument is the
				      // "stick" value. If set to true the event
				      // will "stick" even after you move to other
				      // year, month, day or week.
				     newDataSchedule.push(event);
		      		$calendar.fullCalendar("renderEvent", event, true);
	 			}
	     	// Whatever happens, unselect selection
	     	$calendar.fullCalendar("unselect");
	 
	    }, // End select callback
	 
	    // Make events editable, globally
	    //editable : true,
	 
	    // Callback triggered when we click on an event
	 
	    eventClick: function(event, jsEvent, view){
	    	currentEvent = event;
	    	$("#modalTitle").html(event.title);
	    	$("#startTime").html('Thời điểm bắt đầu: ' + moment(event.start).format('LLL'));
            $("#endTime").html('Thời điểm kết thúc: ' + moment(event.end).format('LLL'));
            $('#position').html('Vị trí quảng cáo: ' + event.position);
            $('#fullCalModal').modal();
	    		
	    }, // End callback eventClick
	    // Callback triggered when we drop on an event
	   eventDrop: function(event, delta, revertFunc) {

	        if (!confirm("Bạn có chắc chắn muốn thay đổi ?")) {
	            revertFunc();
	        }

    	}, // End callback eventDrop
    	// 
    	eventAfterRender: function( event, element, view ) { 
    	//	console.log(event);
    	}
	});
	loadDataSchedule();
	
	catchEventNext();
	catchEventPrev();
	catchEventTodayButton();
	
	// set detail newspapers advertisement
	if(!$('#articleId').val() == ''){
		$('.title_panel_article').removeClass('none-display');
		$('.source_panel_article').removeClass('none-display');
		$('.btn-schedule').removeClass('none-display');
	}else{
		window.history.pushState("", "", '/advertisements');
	}
}

function loadDataSchedule(){
	if(loadData.length != 0){
		$('#calendar').fullCalendar('removeEventSource', loadData);
	}
	var currentDate = $('#calendar').fullCalendar('getDate');
	var beginOfWeek = currentDate.startOf('week').toDate().getTime();
	var endOfWeek = currentDate.endOf('week').toDate().getTime();
	$.ajax({
		method: 'GET',
		url: dns + '/scheduleArticles?startWeek=' + beginOfWeek + '&endWeek=' + endOfWeek + '&categoryId=' + $('#category_advertisements').find(':selected').val() + '&articleId=' + $('#articleId').val(),
		error: function(){
			console.log('error');
		},
		success: function(response){
			if(response.status == 1){
				loadData = response.results;
				$('#calendar').fullCalendar('addEventSource', loadData);
			}else{
				loadData = [];
			}
		}
	});
}

function removeEvent(){
	$('#remove-event').click(function(){
		$('#calendar').fullCalendar('removeEvents', currentEvent._id);
		if(currentEvent.createdDate == null){
			newDataSchedule = $.grep(newDataSchedule, function(e){ 
    			return e.eventId != currentEvent.eventId; 
			});
		}else{
			$.ajax({
				type: 'GET',
				url: dns + '/delete-schedule?id=' + currentEvent.id,
				error: function(){
					alert("Lỗi kết nối");
				},
				success: function(response){
					if(response.status == 1){
						$.notify({
							title : '<strong>Tin nhắn: </strong>',
							message : 'Xóa thành công !!!'
						}, {
							// setting
							delay : 5000,
							type : 'success'
						});
					}
				}
			});
		}
	});
}

function createScheduleArticles(){
	$('#btn-create-schedule').click(function(){
		if(newDataSchedule.length == 0){
			$.notify({
				title : '<strong>Tin nhắn: </strong>',
				message : 'Bạn chưa tạo thêm lịch. Vui lòng tạo trước !!!'
				}, {
					// setting
					delay : 5000,
					type : 'danger'
			});
			return false;
		}
		$.ajax({
			type : 'POST',
			url : dns + '/scheduleArticles',
			data: JSON.stringify(newDataSchedule),
			contentType : "application/json",
			dataType : "json",
			error: function(){
				conosle.log('error');
			},
			success: function(response){
				console.log(response);
				if(response.status == 1){
					var countInsertSuccess = 0;
					var insertErrorArray = [];
					// check insert success or error
					$.grep(response.results, function(newSchedule){
						if(newSchedule.createdDate == null || newSchedule.createdDate == ''){
							insertErrorArray.push(newSchedule);
						}else{
							countInsertSuccess ++;
						}
					});
					// remove new events in fullcalendar
					$.grep(newDataSchedule, function(newSchedule){ 
			    		 $.grep(response.results, function(insertedSchedule){ 
				    		 if(newSchedule.articleId == insertedSchedule.articleId){
				    		 	$('#calendar').fullCalendar('removeEvents', newSchedule._id);
				    		 }
						}); 
					});
					loadDataSchedule();
					newDataSchedule = [];
					if(countInsertSuccess > 0){
						$.notify({
							title : '<strong>Tin nhắn: </strong>',
							message : 'Thêm mới thành công ' + countInsertSuccess + '/' + response.results.length + ' lịch mới !!!'
						}, {
							// setting
							delay : 5000,
							type : 'success'
						});
					}
					console.log(insertErrorArray.length);
					if(insertErrorArray.length != 0){
						$.notify({
							title : '<strong>Tin nhắn: </strong>',
							message : 'Vị trí quảng cáo của lịch đã tồn tại. Vui lòng chọn vị trí khác !!!'
						}, {
							// setting
							delay : 5000,
							type : 'danger'
						});	
					}
					
					
				}else{
					$.notify({
					title : '<strong>Tin nhắn: </strong>',
					message : 'Thêm mới thất bại !!!'
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

function catchEventTodayButton(){
	$('.fc-today-button').click(function(){
		loadDataSchedule();
	});
}

function catchEventNext(){
	$('.fc-next-button').click(function(){
   		loadDataSchedule();
	});
}

function catchEventPrev(){
	$('.fc-prev-button').click(function(){
   		loadDataSchedule();
	});
}

var lastSelCategory;
function catchEventChangeCategory(){
	$('#category_advertisements').change(function(e){
		if(newDataSchedule.length != 0){
			 if (confirm('Bạn vừa tạo mới lịch ở chuyên mục: ' + lastSelCategory.text() + '. Bạn có chắc muốn chuyển sang chuyên mục khác ?')) {
		            var allEvents = $('#calendar').fullCalendar('clientEvents');
		            for(var i =0; i < allEvents.length; i ++){
		            	$('#calendar').fullCalendar('removeEvents', allEvents[i]._id);
		            }
		            newDataSchedule = [];
		            loadDataSchedule(); 
		     }else{
		     	 lastSelCategory.prop("selected", true);
		     }
	     }else
	     	loadDataSchedule();
	});
	
	$("#category_advertisements").click(function(){
    	lastSelCategory = $("#category_advertisements option:selected");
	});
}

function catchEventEnterSearch(){
	$('#search').keypress(function(e) {
	    if(e.which == 13) {
	    	if($(this).val() == ''){
	    		$('.dropdown-search').html('');
	    		if(!$('.btn-schedule').hasClass('none-display')){
	    			$('.title_panel_article').addClass('none-display');
					$('.source_panel_article').addClass('none-display');
	    			$('.btn-schedule').addClass('none-display');
	    		}
	    		$('.article_title').html('');
				$('.article_source').html('');
				$('#articleId').val('');
				window.history.pushState("", "", '/advertisements');
	    		return;
	    	}
	       loadDataArticle();
	    }
	});
}

function loadDataArticle(){
	var suggestPanel = $('.dropdown-search');
	suggestPanel.html('');
	var data = {
		'page' : 0,
		'size' : 10,
		'fields': 'picture,title,category,websiteName',
		'keyword' : $('#search').val(),
		'categoryId' : 0,
		'websiteName' : '0',
		'type' : 'normal',
		'synthesisType': 2,
		'sort' : '<publicDate'
	};
	$.ajax({
		type: 'GET',
		url:  dns + '/newspapers/search?page=' + data.page + '&size=' + data.size + '&fields='
				 + data.fields + '&keyword=' + data.keyword + '&categoryId=' + data.categoryId 
				 + '&sort=' + data.sort + '&websiteName=' + data.websiteName + '&type=' + data.type
				 + '&synthesisType=' + data.synthesisType,
		error: function(){
			console.log('error');
		},
		success: function(response){
			console.log(response);
			if(response.status == 1){
				for(var i = 0; i < response.results.articles.length; i ++){
					var article = response.results.articles[i];
					var articleSearch = $('.dropdown-article-clone').clone().removeClass('dropdown-article-clone').addClass('dropdown-article');
					if(article.picture == null){
						articleSearch.find('.article_search_img').attr('src', "/img/logo.png");
					}else{
						articleSearch.find('.article_search_img').attr('src', article.picture);
					}
					
					articleSearch.find('.article_search_title').html(article.title);
					articleSearch.find('.article_search_id').val(article.id);
					articleSearch.find('.article_search_source').val(article.websiteName);
					suggestPanel.append(articleSearch);
				}
				catchEventClickDetailSearchArticle(); 
			}else{
				$.notify({
					title : '<strong>Tin nhắn: </strong>',
					message : 'Không tìm thấy kết quả !!!'
					}, {
						// setting
						delay : 3000,
						type : 'danger'
					});
			}
		}
	});
}
function catchEventClickDetailSearchArticle(){
	$('.dropdown-article').click(function(){
		$('.dropdown-search').addClass('none-display');
		var titleArticle = $(this).find('.article_search_title').html();
		var sourceArticle = $(this).find('.article_search_source').val();
		var idArticle = $(this).find('.article_search_id').val();
		$('.article_title').html(titleArticle);
		$('.article_source').html(sourceArticle);
		$('#articleId').val(idArticle);
		if($('.btn-schedule').hasClass('none-display')){
			$('.title_panel_article').removeClass('none-display');
			$('.source_panel_article').removeClass('none-display');
			$('.btn-schedule').removeClass('none-display');
		}
		window.history.pushState("", "", '/advertisements?articleId=' + idArticle);
		loadDataSchedule();
	});
}

function hideshowSearchArticle(){
	$("#search_panel").focusin(function () {
        $('.dropdown-search').removeClass('none-display');       
    });
	$("#search_panel").focusout(function () {
		setTimeout(function(){
			 $('.dropdown-search').addClass('none-display');  
		},500);
    });
}

