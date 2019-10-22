window.onload = function () {
    var url = document.location.href,
        params = url.split('?')[1].split('&'),
        data = {}, tmp;
    for (var i = 0, l = params.length; i < l; i++) {
         tmp = params[i].split('=');
         data[tmp[0]] = tmp[1];
    }
    console.log(data.name);
    var database = firebase.database().ref().child("events").child(String(data.name));
	database.child("eventName").on('value', function(snapshot) {
  		var text = String(snapshot.val());
    	document.getElementById("eventDescription").innerHTML = text;
	});
	database.child("imageLink").on('value', function(snapshot) {
  		var imageURL = String(snapshot.val());
    	document.getElementById("image").src=imageURL;
	});
}
