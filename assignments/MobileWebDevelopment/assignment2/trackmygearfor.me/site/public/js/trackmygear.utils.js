// Template loader
tpl = {

	templates : {},

	loadTemplates : function(names, callback) {

		var deferreds = [];

		var self = this;

		$.each(names, function(index, name) {
			deferreds.push($.get('tpl/' + name + '.html', function(data) {
				self.templates[name] = data;
			}));
		});

		$.when.apply(null, deferreds).done(callback);
	},

	// Get template by name from hash of preloaded templates
	get : function(name) {
		return this.templates[name];
	}
}

var http = {
	req : function(method, url, data, win, fail) {
		$.ajax({
			url : url,
			type : method,
			contentType : 'application/json',
			data : data ? JSON.stringify(data) : null,
			dataType : 'json',
			cache : false,
			success : win || logargs,
			error : fail || win || logargs
		})
	},

	post : function(url, data, win, fail) {
		http.req('POST', url, data, win, fail)
	},

	put : function(url, data, win, fail) {
		http.req('PUT', url, data, win, fail)
	},

	get : function(url, win, fail) {
		http.req('GET', url, null, win, fail)
	},

	del : function(url, win, fail) {
		http.req('DELETE', url, null, win, fail)
	}
}