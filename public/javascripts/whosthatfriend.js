$(function() {
		$("#fb_image").ready(function() {
            var my_img = document.getElementById("fb_image");
			Pixastic.process(my_img, "blurfast", {amount:4.5},function(){
					$("#fb_image").fadeTo('slow', 0.9, function(){
						//$("#myimg").effect("shake", {times:4}, 1000 );
					});
			});
			
		});
	
	});