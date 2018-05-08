var entity_input = "";
var property_input = "";
var class_input = "";
var changeEntity = function(){
	entity_input = document.getElementById("entity_textarea").value;
	document.getElementById("collapsible_entity").click(); 
	//TODO: Don't think this is a good fix. Need to probably find better solution,ask.
};
var changeProperty = function(){
	property_input = document.getElementById("property_textarea").value;
	document.getElementById("collapsible_property").click(); 
};
var changeClass = function(){
	class_input = document.getElementById("class_textarea").value;
	document.getElementById("collapsible_class").click(); 
};
var handleEntityCancel = function(){
	document.getElementById("collapsible_entity").click(); 
	document.getElementById("entity_textarea").value = entity_input; //Putback the old value
};
var handlePropertyCancel = function(){
	document.getElementById("collapsible_property").click(); 
	document.getElementById("property_textarea").value = property_input;
};
var handleClassCancel = function(){
	document.getElementById("collapsible_class").click(); 
	document.getElementById("class_textarea").value = class_input;
};
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
var getEntityQuery = function(){
	$.ajax({
		type : "POST",
		dataType: "text",
		url: "/getEntityQuery",
		timeout: 100000,
		async: false,		
	})
	.done(function(data){
		document.getElementById("entity_textarea").value=data;
		entity_input=data;
	});
};
var getPropertyQuery = function(){
	$.ajax({
		type : "POST",
		dataType: "text",
		url: "/getPropertyQuery",
		timeout: 100000,
		async: false,		
	})
	.done(function(data){
		document.getElementById("property_textarea").value=data;
		property_input = data;
	});
};
var getClassQuery = function(){
	$.ajax({
		type : "POST",
		dataType: "text",
		url: "/getClassQuery",
		timeout: 100000,
		contentType: "application/json",
		async: false,		
	})
	.done(function(data){
		document.getElementById("class_textarea").value=data;
		class_input=data;
	});
};
var addEventListeners = function(){
	document.getElementById("entity_save").addEventListener("click", changeEntity);
	document.getElementById("prop_save").addEventListener("click", changeProperty);
	document.getElementById("class_save").addEventListener("click", changeClass);
	document.getElementById("entity_cancel").addEventListener("click", handleEntityCancel);
	document.getElementById("prop_cancel").addEventListener("click", changeProperty);
	document.getElementById("class_cancel").addEventListener("click", changeClass);
};
var toggleButtonHandler = function(){
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
};
$(document).ready(function() {
	getEntityQuery();
	getPropertyQuery();
	getClassQuery();
	toggleButtonHandler();
	addEventListeners();

});
