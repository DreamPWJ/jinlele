// JavaScript Document
function autoHeight(){
	if (window.innerHeight){//FF
		nowHeight = window.innerHeight;
	}else{
		nowHeight = document.documentElement.clientHeight;
	}
	var jianHeight = 50;
	if(nowHeight > jianHeight){
		document.getElementById('fenlei').style.height = nowHeight - jianHeight + 'px';
	}else{
		document.getElementById('fenlei').style.height = jianHeight + 'px';
	}
	var jianHeight = 50;
	if(nowHeight > jianHeight){
		document.getElementById('leibie').style.height = nowHeight - jianHeight + 'px';
	}else{
		document.getElementById('leibie').style.height = jianHeight + 'px';
	}
}
autoHeight();
window.onresize = autoHeight;

