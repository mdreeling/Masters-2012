// API implementation

var common = require('./common')

var uuid = common.uuid
var mongodb = common.mongodb

var todocoll = null
var logincoll = null

//memcached
var Memcached = require('memcached')

var memcached = new Memcached("127.0.0.1:11211")
var lifetime = 3600

var util = {}

util.memcachedend = function end(msg) {
	if(msg) {
		console.log(msg)
	}
	memcached.end()
}

util.err = function error(win) {
	return function(err, result) {
		if(err) {
			console.log(err)
		} else {
			win && win(result)
		}
	}
}

util.validate = function(input) {
	return input.text
}

util.fixid = function(doc) {
	if(doc._id) {
		doc.id = doc._id.toString()
		delete doc._id
	} else if(doc.id) {
		doc._id = new mongodb.ObjectID(doc.id)
		delete doc.id
	}
	return doc
}

exports.ping = function(req, res) {
	var output = {
		ok : true,
		time : new Date()
	}
	res.sendjson$(output)
}

exports.echo = function(req, res) {
	var output = req.query

	if('POST' == req.method) {
		output = req.body
	}

	res.sendjson$(output)
}

exports.rest = {

	create : function(req, res) {
		var input = req.body

		if(!util.validate(input)) {
			return res.send$(400, 'invalid')
		}

		var todo = {
			text : input.text,
			created : new Date().getTime(),
			location : input.location,
			parentid : input.parentid
		}
		console.log('inserting...' + input)
		todocoll.insert(todo, res.err$(function(docs) {
			var output = util.fixid(docs[0])
			res.sendjson$(output)
			memcached.set(output.id, todo, lifetime, function(err, result) {
				if(err) {
					return util.memcachedend(err);
				} else {
					console.log('Inserted to memcached')
				}
			})
		}))
		console.log('done inserting!')
	},
	readlogin : function(req, res) {
		var input = req.params

		console.log(req.params)

		var query = util.fixid({
			username : input.id
		})
		logincoll.findOne(query, res.err$(function(doc) {
			if(doc) {
				var output = util.fixid(doc)
				res.sendjson$(output)
			} else {
				res.send$(404, 'not found')
			}
		}))
	},
	read : function(req, res) {
		var input = req.params

		console.log(req.params)

		var query = util.fixid({
			id : input.id
		})

		memcached.get(input.id, util.err(function(todocached) {
			if(todocached) {
				console.log('*** Cache hit for ' + todocached.id)
				res.sendjson$(todocached)
			} else {

				todocoll.findOne(query, res.err$(function(doc) {
					if(doc) {
						var output = util.fixid(doc)

						memcached.set(output.id, todo, lifetime, function(err, result) {
							if(err) {
								return util.memcachedend(err);
							} else {
								console.log('Inserted to memcached after read')
							}
						})

						res.sendjson$(output)
					} else {
						res.send$(404, 'not found')
					}
				}))
			}
		}))
	},
	list : function(req, res) {

		console.log('listing')

		var input = req.query
		var output = []
		var query = null;

		if(req.query.parentid == null) {
			query = {
				"parentid" : null
			}
		} else {
			query = {
				"parentid" : req.query.parentid
			}
		}

		var options = {
			sort : [['created', 'desc']]
		}

		todocoll.find(query, options, res.err$(function(cursor) {
			cursor.toArray(res.err$(function(docs) {
				output = docs
				output.forEach(function(item) {
					util.fixid(item)
				})
				res.sendjson$(output)
			}))
		}))
	},
	update : function(req, res) {
		var id = req.params.id
		var input = req.body

		if(!util.validate(input)) {
			return res.send$(400, 'invalid')
		}

		var query = util.fixid({
			id : id
		})

		console.log('Updating ' + input.id + ' with ' + input.text)

		var todo = {
			text : input.text,
			created : new Date().getTime(),
			location : input.location,
			parentid : input.parentid,
			done : input.done
		}

		todocoll.update(query, todo, function(err) {
			if(err) {
				console.warn(err.message);
			} else {
				console.log('successfully updated');

				memcached.set(input.id, todo, lifetime, function(err, result) {
					if(err) {
						return util.memcachedend(err);
					} else {
						console.log('Updating memcached ' + query.id + ' after update')
					}
				})
			}
		});
	},
	del : function(req, res) {
		var input = req.params

		var query = util.fixid({
			id : input.id
		})
		todocoll.remove(query, res.err$(function() {
			var output = {}
			res.sendjson$(output)
		}))
	}
}

exports.connect = function(options, callback) {
	var client = new mongodb.Db(options.name, new mongodb.Server(options.server, options.port, {}))
	client.open(function(err, client) {
		if(err)
			return callback(err);

		client.collection('todo', function(err, collection) {
			if(err)
				return callback(err);
			todocoll = collection
			callback()
		})

		client.collection('login', function(err, collection) {
			if(err)
				return callback(err);
			logincoll = collection
			callback()
		})
	})
}