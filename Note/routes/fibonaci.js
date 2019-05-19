var math = require('../math');
exports.index = function(req, res){
	if (req.query.fibonum) {
		res.render('fibonaci', {
			title: "Calculate fibonaci numbers",
			fibonum: req.query.fibonum,
			fiboval: math.fibonaci(req.query.fibonum)
		});
	} else {
		res.render('fibonaci', {
			title: "Calculate fibonaci number",
			fiboval: undefined
		});
	}
}
