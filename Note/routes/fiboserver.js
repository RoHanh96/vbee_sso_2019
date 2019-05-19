var math = require('./math');
var express = require('express');
var app = express();
app.get('/fibonaci/:n', function (req, res, next) {
	// body...
	var result = math.fibonaci(req.params.n);
	res.send({
		n: req.params.n,
		res: result
	})
});
app.listen(3002);