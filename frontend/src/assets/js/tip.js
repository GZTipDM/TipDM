// JavaScript Document
var $ = require("jquery");
function initDiv(){
	var w = document.documentElement.clientWidth;
	var h = document.documentElement.clientHeight;
	$(".mCom").css({"width":w-90,"height":h-87});
	
	//var ddH = $("#sideTree").height() - 38*$("#sideTree dl dt").length-15;
	//$("#treeSor dd").css({"height":ddH/2,"max-height":ddH+8});
	//$(".sideTree dl dd").css({"height":ddH/2,"max-height":ddH-61});

	//$("#eSetting dd").css("max-height",h-90-45*$("#eSetting dt").length);
}
window.onresize=function(){initDiv();}
$(function(){
	initDiv();

//
// $(".msList li").bind("click",function(){
// 	$(".msList li").removeClass("on");
// 	$(this).addClass("on");
// })

//组件模型点击切换事件
	/*
$("#treeSor2 dt").bind("click",function(){
	$("#treeSor2 dt").removeClass("on");
	$(".minSty #treeSor dt").removeClass("on");
	$(this).addClass("on");
	$("#treeSor2 dd").hide();
	$(".minSty #treeSor dd").hide();
	$(this).next("dd").slideDown("slow");
	$(this).next("dd").focusin();
});
*/
//属性设置菜单
$("#eSetting dt").bind("click",function(){
	$("#eSetting dt").removeClass("on");
	$("#eSetting dd").removeClass("on");
	$("#eSetting dd").slideUp();
	$(this).addClass("on");
	$(this).next("dd").addClass("on");
	$(this).next("dd").slideDown();
});

//工程组件侧栏伸缩----小按钮-收缩左侧菜单栏
$("#minstyBtn").toggle(
	function(){
		$("#mCom").addClass("minSty");
		$(".treeSor dt").removeClass("on");
		$(".treeSor dd").hide();
		//$(".treeSor dd").onfocus();
	},
	function(){
		$("#mCom").removeClass("minSty");
		$("#treeSor dt").addClass("on");
		$("#treeSor dd").css("display","block");
		$("#treeSor2 dt.on").next("dd").slideDown();
	}
);

//关闭侧栏弹出菜单
$("#dwArea").bind("click",function(){
	$(".minSty .sideTree dl dd").hide();
});

//$("#sideTree dl dd").css("max-height",ddH);
	

//左侧拖拉事件总纲
	/*
var src_posi_Y = 0, dest_posi_Y = 0, move_Y = 0, is_mouse_down = false, destHeight = 30, destHeights = 30;
$("#udDrag").mousedown(function(e){
	//alert("ddd");
	src_posi_Y = e.pageY;
	is_mouse_down = true;
});
$(document).bind("click mouseup",function(e){
	if(is_mouse_down){
	  is_mouse_down = false;
	}
}).mousemove(function(e){
	dest_posi_Y = e.pageY;
	move_Y = src_posi_Y - dest_posi_Y;
	src_posi_Y = dest_posi_Y;
	destHeight = $("#gcSub").height() - move_Y;
	destHeights = $("#treeSor2 dd").height() + move_Y;

	if(is_mouse_down){
		$("#gcSub").css("height", destHeight > 30 ? destHeight : 30);
		$("#treeSor2 dd").css("height", destHeights > 30 ? destHeights : 30);
	}
});

*/
});



//放大
function zoomIn(){
	var w = Number($("#dwaContent").attr("uWidth"));
	var h = Number($("#dwaContent").attr("uHeight"));
	$("#dwaContent").attr("uWidth",w+10);
	$("#dwaContent").attr("uHeight",h+10);
	$("#dwaContent").css({"width":w + 10 + "%","height":h + 10 + "%" });
	//$("#dwaContent").css({"height":h + 10 + "%" });
	zoomSizeNum(h+10);
}
//缩小
function zoomOut(){
	//var w = Number($("#dwaContent").attr("uWidth"));
	var h = Number($("#dwaContent").attr("uHeight"));
	//$("#dwaContent").attr("uWidth",w-10);
	$("#dwaContent").attr("uHeight",h-10);
	//$("#dwaContent").css({"width":w - 10 + "%","height":h - 10 + "%" });
	$("#dwaContent").css({"height":h - 10 + "%" });
	zoomSizeNum(h-10);
}
//默认大小
function zoomDe(){
	var uu = $("#dwArea").width();
	var kk = $("#dwaContent").width();
	var ss = 1-(1080-uu)/uu;
	var k  = 1080*ss-uu;

	//alert(uu + " " + kk);
	//$("#dwaContent").css({"transform":"scale(" + ss + ")","left":-k+"px"})
	$("#dwaContent").css({"width":"100%","height":"100%" });
	// $("#dwaContent").attr("uWidth",100);
	// $("#dwaContent").attr("uHeight",100);
	// zoomSizeNum(100);
}
//缩放百分比
function zoomSizeNum(n){
	$("#zoomSize").text(n+"%");
}

//
function play(){
	$("#run").children("i").removeClass("fa-play-circle");
	$("#run").addClass("running");
	$("#run").children("i").addClass("fa-pause-circle runing");
	$("#run").attr("onClick","pause()");
}
function pause(){
	$("#run").children("i").removeClass(" fa-pause-circle");
	$("#run").removeClass("running");
	$("#run").children("i").addClass("fa-play-circle");
	$("#run").attr("onClick","play()");
}

//


// function setScroll(){
// var h = document.documentElement.clientHeight;
// $("#eSetting dd").slimScroll({
// height:h-90-45*$("#eSetting dt").length,
// color: '#064bb2',
// railColor: '#0f5659',
// size: '5px',
// railOpacity: 0.2,
// distance: '0px',
// railVisible: true,
// alwaysVisible: false,
// });
// }

// $(window).on("resize",setScroll);