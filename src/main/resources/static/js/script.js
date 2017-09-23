'use strict'
$(document).ready(
		function() {

			var url = document.location.toString();
			var frag = '';
			if (url.match('#')) {
				frag = '#' + url.split('#')[1];
			} else {
				frag = '#!/data/roles';
			}
			$('.nav-sidebar .nav li a[data-href="' + frag + '"]').parent()
					.addClass('active');
			var style = 'zoomIn';
			//doAnimate(style);

			$(".nav-sidebar .nav li a").on("click", function() {
				$(".nav-sidebar .nav").find("li.active").removeClass("active");
				$(this).parent().addClass("active");
				//doAnimate(style);
			});

		});
