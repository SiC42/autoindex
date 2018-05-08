$("#btn_submit").click(function(){
	var url = $("#index").val();
	var data = {};
	data["url"] = url;
	data["useLocalDataSource"] = "false";
	data["default_graph"] = "";
	data["requestType"] = "URI";
	data["userId"] = "00000000001";
	data["limit"] = $("#count").val();	
	var file = [];
	data["fileList"] = file;

	$.ajax({
		type : "POST",
		dataType: "text",
		data: JSON.stringify(data),
		url: "/index/create",
		timeout: 100000,
		contentType: "application/json",
		async: true,		
	})
	.done(function(data){
		alert("Indexing done");
	})
	.fail(function(data){
		alert("Error occured");
	});
});

$("#btn_submitfile").click(function(){
	var selectedFile = document.getElementById("inputFile").files[0];
	var myForm = new FormData();
	myForm.append("file", selectedFile);
	$.ajax({
		type : "POST",
		data: myForm,
		url: "/index/uploadFile",
		timeout: 100000,
		processData: false,
		enctype :  "multipart/form-data",
		contentType: false,
		async: false,
		cache: false,
	})
	.done(function(data){ 
		alert("Indexing done");
	})
	.fail(function(data){
		alert("Error occured");
	});
});

$(document).ready(function() {
	$.ajax({
		type : "POST",
		dataType: "text",
		url: "/getEntityQuery",
		timeout: 100000,
		async: false,		
	})
	.done(function(data){
		document.getElementById("entity_textarea").value =data;
	});
	$.ajax({
		type : "POST",
		dataType: "text",
		url: "/getPropertyQuery",
		timeout: 100000,
		async: false,		
	})
	.done(function(data){
		document.getElementById("property_textarea").value =data;
	});
	$.ajax({
		type : "POST",
		dataType: "text",
		url: "/getClassQuery",
		timeout: 100000,
		contentType: "application/json",
		async: false,		
	})
	.done(function(data){
		document.getElementById("class_textarea").value =data;
	});
	var coll = document.getElementsByClassName("collapsible");
	var i;

	for (i = 0; i < coll.length; i++) {
	    coll[i].addEventListener("click", function() {
	        this.classList.toggle("active");
	        var content = this.nextElementSibling;
	        if (content.style.display === "block") {
	            content.style.display = "none";
	        } else {
	            content.style.display = "block";
	        }
	    });
	}

});
