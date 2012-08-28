function createNewEvent(email){
	$.post('/createEvent',
		      {'eventName':document.getElementById('eventName').value},
		      function(data) {
		    	  getRegisteredEvents(email);
		      });
}
function getRegisteredEvents(email){
	
	$.getJSON('/getRegisteredEvents',
	          function(events) {
	              // data is a JSON list, so we can iterate over it
				document.getElementById('userEvents').innerHTML = ""; 
				$.each(events, function(key, event) {
	            	  var row1 =  document.createElement('div');
	            	  row1.setAttribute('class', 'row');
	            	  var eventPanel = document.createElement('div');
	            	  eventPanel.setAttribute('class','span10');
	            	  row1.appendChild(eventPanel);
	            	  var eventNamePanel = document.createElement('h3');
	            	  eventNamePanel.innerHTML = event.eventName;
	            	  eventPanel.appendChild(eventNamePanel);
	            	  var addNewPictureButton = document.createElement('button');
	            	  addNewPictureButton.setAttribute('type','submit');
	            	  addNewPictureButton.setAttribute('class','btn btn-small btn-success');
	            	  addNewPictureButton.setAttribute('data-togle','modal');
	            	  addNewPictureButton.setAttribute('href','#addPictures')
	            	  addNewPictureButton.setAttribute('onclick','setPicturePanel(\''+event.eventName+'\')');
	            	  addNewPictureButton.innerHTML = 'Add pictures to this event...';
	            	  eventPanel.appendChild(addNewPictureButton);
	            	  
	            	  var row2 =  document.createElement('div');
	            	  row2.setAttribute('class', 'row');
	            	  eventPanel.appendChild(row2);
	            		 $.each(event.pictures, function(key, picture) {
	            			 var span1 = document.createElement('div');
	            			 span1.setAttribute('class', 'span1');
	            			 row2.appendChild(span1);
	            			 var img = document.createElement('img');
	            			 img.setAttribute('src', picture.url);
	            			 span1.appendChild(img);
	            		 });
        		 document.getElementById('userEvents').appendChild(row1);
	             });
	 });
}

function setPicturePanel(eventName) {
	document.getElementById('picturePanelEventName').innerHTML = eventName;
}