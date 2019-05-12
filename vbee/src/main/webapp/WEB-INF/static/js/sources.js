$(document).ready(function(){
	userServicePath = $('#user_service_path').val();
	version = $('#version').val();
//	sortable();
	getSettingDefauls();
	updateSources();
});
var userServicePath ;
var version;
var categories = [];
var websites = [];

function getSettingDefauls(){
	console.log(userServicePath);
	$.ajax({
		url : userServicePath +  '/api/v1/settingDefaults/' + version,
		type: 'GET',
		error: function(jqXHR,error, errorThrown){
			if(jqXHR.status&&jqXHR.status==400){
                console.log(jqXHR.responseText); 
            }else{
                alert("Xảy ra lỗi lấy dữ liệu");
            }
		},
		success: function(response){
			if(response.status == 1){
				categories = response.results.categories;
				websites = response.results.websites;
				for(var i=0; i < websites.length; i ++){
					if(websites[i].isActive){
						$('#sortable-website').append('<li class="ui-state-default"> <input type="checkbox" class="form-check-input check-website" checked/>' + websites[i].name + '</li>');
					}else{
						$('#sortable-website').append('<li class="ui-state-default"> <input type="checkbox" class="form-check-input check-website"/>' + websites[i].name + '</li>');
					}
				}
				for(var j=0; j < categories.length; j ++){
					if(categories[j].isActive){
						$('#sortable-category').append('<li class="ui-state-default"> <input type="checkbox" class="form-check-input check-category" checked/>' + categories[j].name + '</li>');
					}else{
						$('#sortable-category').append('<li class="ui-state-default"> <input type="checkbox" class="form-check-input check-category"/>' + categories[j].name + '</li>');
					}
				}
				handleCheckCategory();
				handleCheckWebsite();
			}
		}
	});
}

function updateSources(){
	$('.btn-update-source').click(function(){
		if(categories.length == 0 || websites.length ==0){
			return false;
		}
		var settingDefaults = {
			'version':  $('#version').val(),
			'categories' : categories,
			'websites' : websites
		};
		$.ajax({
			url : userServicePath + '/api/v1/settingDefaults',
			type : 'PUT',
			data: JSON.stringify(settingDefaults),
			contentType : "application/json",
			dataType : "json",
			error: function(){
				console.log('error');
			},
			success: function(response){
				if(response.status == 1){
					$.notify({
						title : '<strong>Tin nhắn: </strong>',
						message : 'Cập nhật thành công !!!'
					}, {
						// setting
						delay : 5000,
						type : 'success'
					});
					updateUncheckCategorysAndWebsites();
				}else{
					$.notify({
						title : '<strong>Tin nhắn: </strong>',
						message : 'Cập nhật thất bại !!!'
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

function updateUncheckCategorysAndWebsites(){
	var categoryUnActive = [];
	var categoryActive = [];
	var websiteUnActive = [];
	var websiteActive = [];
	var category
	for(var i = 0; i < categories.length; i ++){
		if(categories[i].isActive){
			categoryActive.push(categories[i].id);
		}else{
			categoryUnActive.push(categories[i].id);
		}
	}
	for(var j = 0; j < websites.length; j ++){
		if(websites[j].isActive){
			websiteActive.push(websites[j].id);
		}else{
			websiteUnActive.push(websites[j].id);
		}
	}
	$.ajax({
		type: 'GET',
		url: dns + '/update-categories-websites?unActiveCategoryIds=' + categoryUnActive.toString() + '&unActiveWebsiteIds=' + websiteUnActive.toString() + '&activeCategoryIds=' + categoryActive.toString() + '&activeWebsiteIds=' + websiteActive.toString(),
		error: function(error){
			console.log('error');
		},
		success: function(response){
			console.log(response);			
		}
	});
}

function handleCheckCategory(){
	$('.check-category').change(function(){
		var ischecked= $(this).is(':checked');
		var name = $(this).parents('.ui-state-default').text().trim();
		var category = findPurpose(name,categories)[0];
    	if(ischecked){
			category.isActive = true;    		
    	}else{
    		category.isActive = false;
    	}
	});
}

function handleCheckWebsite(){
	$('.check-website').change(function(){
		var ischecked= $(this).is(':checked');
		var name = $(this).parents('.ui-state-default').text().trim();
		var website = findPurpose(name,websites)[0];
    	if(ischecked){
			website.isActive = true;    		
    	}else{
    		website.isActive = false;
    	}
	});
}

function arrayMove(arr, old_index, new_index){
    if (new_index >= arr.length) {
        var k = new_index - arr.length + 1;
        while (k--) {
            arr.push(undefined);
        }
    }
    arr.splice(new_index, 0, arr.splice(old_index, 1)[0]);
    return arr; 
}

function findPurpose(name, purposeObjects){
    return $.grep(purposeObjects, function(item){
      return item.name == name;
    });
};

function sortable(){
	$("#sortable-category").sortable({
		start: function(event, ui) {
	        ui.item.startPos = ui.item.index();
	    },
		stop: function(event, ui) {
	        categories = arrayMove(categories, ui.item.startPos, ui.item.index());
	        for(var i =0; i<categories.length; i ++){
				if(categories[i].position != i + 1){
					categories[i].position = i + 1;
				}
			}
	    },
		placeholder : "ui-state-highlight"
	});
	$("#sortable-website").sortable({
		start: function(event, ui) {
	        ui.item.startPos = ui.item.index();
	    },
		stop: function(event, ui) {
			websites = arrayMove(websites, ui.item.startPos, ui.item.index());
			for(var i =0; i<websites.length; i ++){
				if(websites[i].position != i + 1){
					websites[i].position = i + 1;
				}
			}
	    },
		placeholder : "ui-state-highlight"
	});
	$("#sortable-category").disableSelection();
	$("#sortable-website").disableSelection();
}