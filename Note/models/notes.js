var notes = [];
exports.update = exports.create = function (key, title, body) {
	// body...
	notes[key] = {title: title, body: body};
}
exports.read = function (key) {
	// body...
	return notes[key];
}

exports.destroy = function(key){
	delete notes[key];
}

exports.keys = function () {
	// body...
	return Object.keys(notes);
}