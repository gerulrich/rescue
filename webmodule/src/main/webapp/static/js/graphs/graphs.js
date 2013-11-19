var options = {
		xaxis: { mode: "time", tickLength: 1},
		yaxis: { ticks: 10, min: 0 },
		legend: { 
			show: true,
		    backgroundOpacity: 1
		},
		series:{
	    	lines: { fill: true },
	    	color: "#27850D",
	    	shadowSize: 0
		},
		grid: {
			borderWidth: 2,
			minBorderMargin: 20,
			labelMargin: 10,
			//backgroundColor: {
			//	colors: ["#fff", "#e4f4f4"]
			//},			
		}         
	};

function createGraph(div, url) {
	
	var data = [];
	var placeholder = $(div);

	$.plot(placeholder, data, options);
	    
	$.ajax({
		url: url,
		method: 'GET',
		dataType: 'json',
		success: function(data) {
			onDataReceived(data, placeholder, options);
		}
	});	
}

function updateGraph(placeholder, url) {
	
	$.ajax({
		url: url,
		method: 'GET',
		dataType: 'json',
		success: function(data) {
			onDataReceived(data, placeholder, options);
		}
	});
}

// then fetch the data with jQuery
function onDataReceived(series, placeholder, options) {
	// extract the first coordinate pair so you can see that
	// data is now an ordinary Javascript object
	var data = [];
	// fetch one series, adding to what we got
	var alreadyFetched = {};	
	var firstcoordinate = '(' + series.data[0][0] + ', ' + series.data[0][1] + ')';

	// first correct the timestamps - they are recorded as the daily
	// midnights in UTC+0100, but Flot always displays dates in UTC
	// so we have to add one hour to hit the midnights in the plot
	for (var i = 0; i < series.data.length; ++i) {
		series.data[i][0] -= 60 * 60 * 3000;
		//series.data[i][1]++;
	}

	// let's add it to our current data
	if (!alreadyFetched[series.label]) {
		alreadyFetched[series.label] = true;
		data.push(series);
	} else {
		data = [];
		data.push(series);
	}

	//and plot all we got
	$.plot(placeholder, data, options);
}

function showTooltip(x, y, contents) {
    $('<div id="tooltip">' + contents + '</div>').css( {
        position: 'absolute',
        display: 'none',
        top: y + 5,
        left: x + 5,
        border: '1px solid #fdd',
        padding: '2px',
        'background-color': '#fee',
        opacity: 0.80
    }).appendTo("body").fadeIn(200);
}