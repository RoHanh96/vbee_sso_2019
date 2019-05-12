$(document).ready(function() {
	catchEventEnterSearch();
	filterDataSearch();
	catchEventChangeStatus();
	catchEventChangeCategory();
	catchEventChangeWebsite();
	catchEventChangeSort();
	catchEventChangeSize();
});

var currentPage = 1;
var size;
var jump = 3;

function catchEventChangeSize(){
	$('#data_article_length').change(function(){
		currentPage = 1;
		filterDataSearch();
	});
}

function catchEventEnterSearch(){
	$('#data_article_search').keypress(function(e) {
	    if(e.which == 13) {
	        currentPage = 1;
			filterDataSearch();
	    }
	});
}

function catchEventChangeStatus(){
	$("#synthesis_filter").change(function(){
		currentPage = 1;
		filterDataSearch();
	});
}

function catchEventChangeCategory(){
	$("#category_filter").change(function(){
		currentPage = 1;
		filterDataSearch();
	});
}

function catchEventChangeWebsite(){
	$('#website_filter').change(function(){
		currentPage = 1;
		filterDataSearch();
	});
}

function catchEventChangeSort(){
	$('#sort').change(function(){
		currentPage = 1;
		filterDataSearch();
	});
}

function filterDataSearch(){
	size = $('#data_article_length').find(':selected').val()
	var data = {
		'page' : currentPage -1,
		'size' :  size,
		'fields': 'picture,title,category,websiteName,type,status,totalChoose,totalListen,listeningRate,publicDate,countAudioSynthesize,url,numDoubt,synthesisType',
		'keyword': $('#data_article_search').val(),
		'categoryId' : $("#category_filter").find(':selected').val(),
		'websiteName' : $('#website_filter').find(':selected').val(),
		'sort' : $('#sort').find(':selected').val(),
		'synthesisType' : $('#synthesis_filter').find(':selected').val()
	}
	search(data);
}

function search(data){
	$('#paging').hide();
	$('#no-results').hide();
	$('.show-results-count').hide();
	$('#loadingSpinner').show();
	var table = $('#table');
	var tableBody = table.find('tbody');
	tableBody.html('');
	$.ajax({
		url: dns + '/newspapers/search?page=' + data.page + '&size=' + data.size + '&fields=' + data.fields + '&keyword=' + data.keyword + '&categoryId=' + data.categoryId + '&sort=' + data.sort + '&websiteName=' + data.websiteName + '&synthesisType=' + data.synthesisType,
		method: 'GET',
		success: function(response){
			console.log(response);
			if(response.status == 1){
				var message = response.message;
				var results = response.results;
				for(var i=0; i<results.articles.length; i ++){
					var article = results.articles[i];
					var row = table.find('.article_col_clone').clone().removeClass('article_col_clone').removeClass('none-display').addClass('article_col');
					//row.find('.article_stt').html((currentPage-1)*size + 1 + i);
					row.find('.article_id').html(article.id);
					if(article.status == 1){
						row.find('.btn-action-active').prop('disabled', true);
						row.addClass('table-primary');
					}else if(article.status == -1){
						row.find('.btn-action-active').html('Hoạt động');
						row.addClass('table-danger');
					}
					if(article.picture == null){
						row.find('.link_picture').attr('src', "/img/logo.png");
					}else{
						row.find('.link_picture').attr('src', article.picture);
					}
					if(article.url == null){
						row.find('.article_title').html(article.title);
					}else{
						row.find('.article_title').html('<a target="_blank" href="' + article.url + '">' + article.title + '</a>');
					}
					
					row.find('.article_category').html(article.category.name);
					row.find('.article_source').html(article.websiteName);
					row.find('.article_publicDate').html(moment(article.publicDate).format('DD/MM/YYYY HH:mm') + ' - ' + moment(article.publicDate).fromNow());
					if(article.synthesisType == 1){
						 row.find('.article_synthesisType').html('<span class="text-secondary">Đang tổng hợp</span>');
					}else if(article.synthesisType == 2){
						row.find('.article_synthesisType').html('<span class="text-success">Đã tổng hợp</span>');
					}else if(article.synthesisType == 3){
						row.find('.article_synthesisType').html('<span class="text-danger">Tổng hợp lỗi</span>');
					}
					
					row.find('.article_totalChoose').html(article.totalChoose);
					var percent = parseFloat(article.listeningRate*100).toFixed(2);
					row.find('.article_listeningRate').html(article.totalListen + ' - ' + percent + '%');
					row.find('.article_id').html(article.id);
					row.find('.article_status').html(article.status);
					if(article.numDoubt == null || article.numDoubt == 0){
						row.find('.num').hide();
						row.find('.fa-comment').hide();
					}else{
						row.find('.num').html(article.numDoubt);
					}
					row.find('.btn-edit').attr('href', '/newspapers/' + article.id);
					if(article.synthesisType == 2){
						row.find('.btn-advertisements').attr('href', '/advertisements?articleId=' + article.id);
					}else{
						row.find('.btn-advertisements').hide();
					}
					tableBody.append(row);
				}
				$('.show-results-count').show();
				$('#loadingSpinner').hide();
				pagning(results.totalPages);
				catchEventActioneActiveArticle();
			}else{
				$('#loadingSpinner').hide();
				$('#no-results').show();
			}
			
		},
		error: function(jqXHR,error, errorThrown) {  
           if(jqXHR.status&&jqXHR.status==400){
                alert(jqXHR.responseText); 
           }else{
               alert("Something went wrong");
           }
        }
	});
}



function pagning(totalPages){
	//console.log('totalPages: ' + totalPages);
	var pagns = $('.pagns');
	pagns.html('');
	if(currentPage == 1 && totalPages == 1){
		$('#prevPage').html('<span class="pagnPP inline"> <img alt="previous" src="/img/previous-page.png" class="imgPrevious img-page"> <span class="pagnPrevString">Trang trước</span></span>');
		$('#nextPage').html('<span class="pagnNP inline"> <span class="pagnNextString">Trang sau</span> <img alt="next" src="/img/next-page.png" class="imgNext img-page"></span>');
	}else if(currentPage == 1){
		$('#prevPage').html('<span class="pagnPP inline"> <img alt="previous" src="/img/previous-page.png" class="imgPrevious img-page"> <span class="pagnPrevString">Trang trước</span></span>');
		$('#nextPage').html('<span class="pagnNP pagnLink pagnHasNext inline"> <span class="pagnNextString">Trang sau</span> <img alt="next" src="/img/next-page.png" class="imgNext img-page"></span>');
	}else if(currentPage == totalPages){
		$('#prevPage').html('<span class="pagnPP pagnLink pagnHasPrev inline"> <img alt="previous" src="/img/previous-page.png" class="imgPrevious img-page"> <span class="pagnPrevString">Trang trước</span></span>');
		$('#nextPage').html('<span class="pagnNP inline"> <span class="pagnNextString">Trang sau</span> <img alt="next" src="/img/next-page.png" class="imgNext img-page"></span>');
	}else{
		$('#prevPage').html('<span class="pagnPP pagnLink pagnHasPrev inline"> <img alt="previous" src="/img/previous-page.png" class="imgPrevious img-page"> <span class="pagnPrevString">Trang trước</span></span>');
		$('#nextPage').html('<span class="pagnNP pagnLink pagnHasNext inline"> <span class="pagnNextString">Trang sau</span> <img alt="next" src="/img/next-page.png" class="imgNext img-page"></span>');
	}
	var btnRaw = $('.pagn-clone').clone().removeClass('pagn-clone').removeClass('none-display');
	var btnDot = btnRaw.clone().removeClass('pagnLink').removeClass('pagnNumber').addClass('pagnMore');
	btnDot.text('...');
	if(totalPages <=5 || (totalPages == 7 && currentPage == 4) || (totalPages == 6 && currentPage == 3)){
		// Truong hop ca 2 phia khong chua ...
		for(var i = 1; i <= totalPages; i ++){
			var btnPage = btnRaw.clone();
			if(i == currentPage){
				btnPage.addClass('pagnCur').removeClass('pagnLink').removeClass('pagnNumber');
				btnPage.text(i);
			}else{
				btnPage.text(i);
			}
			pagns.append(btnPage);
		}
	}else if(currentPage < 5 && currentPage + jump < totalPages){
		// Truong hop phia sau chua ...
		for(var j = 1; j <= currentPage + 1; j++){
			var btnPage = btnRaw.clone();
			if(j == currentPage){
				btnPage.addClass('pagnCur').removeClass('pagnLink').removeClass('pagnNumber');
				btnPage.text(j);
			}else{
				btnPage.text(j);
			}
			pagns.append(btnPage);
		}
		
		var btnMore = btnDot.clone();
		pagns.append(btnMore);
		
		var btnLast = btnRaw.clone().removeClass('pagnLink').removeClass('pagnNumber');
		btnLast.text(totalPages);
		pagns.append(btnLast);
	}else if(currentPage >= 5 && currentPage + jump <= totalPages){
		// Truong hop ca hai phia deu chua ...
		var btnFirst = btnRaw.clone();
		btnFirst.text(1);
		pagns.append(btnFirst);
		
		var btnMore = btnDot.clone();
		pagns.append(btnMore);
		
		for(var k = currentPage - 1; k <= currentPage + 1; k ++){
			var btnPage = btnRaw.clone();
			if(k == currentPage){
				btnPage.addClass('pagnCur').removeClass('pagnLink').removeClass('pagnNumber');
				btnPage.text(k);
			}else{
				btnPage.text(k);
			}
			pagns.append(btnPage);
		}
		
		var btnMore = btnDot.clone();
		pagns.append(btnMore);
		
		var btnLast = btnRaw.clone().removeClass('pagnLink').removeClass('pagnNumber');
		btnLast.text(totalPages);
		pagns.append(btnLast);
	}else if(currentPage + jump > totalPages){
		//Truong hop phia truoc chua ..
		var btnFirst = btnRaw.clone().remove();
		btnFirst.text(1);
		pagns.append(btnFirst);
		
		var btnMore = btnDot.clone();
		pagns.append(btnMore);
		
		for(var a = currentPage - 1; a <= totalPages; a ++){
			var btnPage = btnRaw.clone();
			if(a == currentPage){
				btnPage.addClass('pagnCur').removeClass('pagnLink').removeClass('pagnNumber');
				btnPage.text(a);
			}else{
				btnPage.text(a);
			}
			pagns.append(btnPage);
		}
	}
	catchEventClickNextPage();
	catchEventClickPrevPage();
	catchEventClickBtnPage();
	$('#paging').show();
}

function catchEventClickNextPage(){
	$('.pagnHasNext').click(function(){
		currentPage ++;
		filterDataSearch();
	});	
}

function catchEventClickPrevPage(){
	$('.pagnHasPrev').click(function(){
		currentPage --;
		filterDataSearch(); 
	});
}

function catchEventClickBtnPage(){
	$('.pagnNumber').click(function(){
		var number = parseInt($(this).text());
		currentPage = number;
		filterDataSearch();
	});
}

function catchEventActioneActiveArticle(){
	$('.btn-action-active').click(function(){
		var button = $(this);
		var rowParent = $(this).parents('.article_col');
		var articleId = rowParent.find('.article_id').html();
		var status = parseInt(rowParent.find('.article_status').html());
		if(status == 0){
			rowParent.find('.article_status').html('-1');
			status = -1;
		}else{
			status = 0;
			rowParent.find('.article_status').html('0');
		}
		console.log('articleId: ' + articleId + ' ----- ' + status);
		$.ajax({
			type : 'POST',
			url : dns + '/newspapers/updateActive?articleId=' + articleId + '&status=' + status,
			error: function(){
				console.log('error');
			},
			success: function(response){
				if(response.status == 1){
					$.notify({
						title : '<strong>Tin nhắn: </strong>',
						message : 'Update thành công !!!'
					}, {
						// setting
						delay : 5000,
						type : 'success'
					});
					console.log('status: ' + status);
					
					if(status == 0){
						rowParent.removeClass('table-danger');
						button.html('Tắt hoạt động');	
					}else{
						rowParent.addClass('table-danger');
						button.html('Hoạt động');	
					}
					
				}else{
					$.notify({
						title : '<strong>Tin nhắn: </strong>',
						message : 'Update thất bại !!!'
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