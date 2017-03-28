function get(name){
	var obj = document.getElementById(name);
	if(obj != null)
  		return obj;
  obj = document.getElementsByName(name);
	if(obj != null && obj.length > 0)
		return obj[0];
	return null;
}
