var fibonaci = exports.fibonaci = function (n) {
	// body...
	var fibos = [];
	fibos[0] = 0;
	fibos[1] = 1;
	fibos[2] = 1;
	for (var i = 3; i <= n; i++) {
		fibos[i] = fibos[i-2] + fibos[i-1];
	}
	return fibos[n];
}