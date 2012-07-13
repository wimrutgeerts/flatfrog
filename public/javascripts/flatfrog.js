$(document).ready(function(){
	$("body").css("background-color", function(){
		var colors = new Array("#543534","#343245","#134493","#984732");
		return colors[Math.floor((Math.random()*colors.length))];
	});
});