var common = require('./common')
var api = require('./json-api')

var connect = common.connect

function init() {
	var server = connect.createServer()
	server.use(connect.logger())
	server.use(connect.bodyParser())
	server.use(connect.query())

	server.use(function(req, res, next) {
		res.sendjson$ = function(obj) {
			common.sendjson(res, obj)
		}
		next()
	})
	var router = connect.router(function(app) {
		app.get('/api/ping', api.ping)
		app.get('/api/echo', api.echo)
		app.post('/api/echo', api.echo)
	})
	server.use(router)

	server.use(connect.static(__dirname + '/../../site/public'))

	server.listen(8180)
}



init()