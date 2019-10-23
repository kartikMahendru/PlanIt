var url,params,data, tmp;
window.onload = function () {
        url = document.location.href,
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

function save(){
  if(document.getElementById("YesRadio").checked){
    console.log("inside if");
    var database = firebase.database().ref().child("events").child(String(data.name));
    database.child("count").once('value', function(snapshot){
       var text = Number(snapshot.val())+1;
       console.log(text);
      var updates = {};
      updates['/events/' + data.name + "/" + "count"] = text;
      return firebase.database().ref().update(updates);
    });

  }
}
