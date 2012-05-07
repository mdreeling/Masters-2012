// API implementation

var common = require('./common')
var postmark = require("postmark")("5958c673-8354-44d4-b0de-cdef1b9a7147");

var uuid = common.uuid
var mongodb = common.mongodb
var config = common.config
var twitter = common.twitter
var facebook = common.facebook

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
	return input.name
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
			name : input.name,
			model : input.model,
			description : input.description,
			category : input.category,
			serialnumber : input.serialnumber,
			username : input.username,
			imagedata : input.imagedata,
			serialimagedata : input.serialimagedata,
			creatd : new Date().getTime()
		}
		console.log('inserting...' + input)
		todocoll.insert(todo, res.err$(function(docs) {
			var output = util.fixid(docs[0])
			res.sendjson$(output)
			memcached.set(output.id, todo, lifetime, function(err, result) {
				if(err) {
					return util.memcachedend(err);
				} else {
					console.log('MEMCACHE *** Inserted to memcached')
				}
			})
		}))
		console.log('done inserting!')
	},
	readlogin : function(req, res) {
		var input = req.params

		console.log('Searching for username=' + req.query.id)

		var query = util.fixid({
			username : req.query.id
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
				console.log('MEMCACHE *** Cache hit for ' + todocached.id)
				res.sendjson$(todocached)
			} else {

				todocoll.findOne(query, res.err$(function(doc) {
					if(doc) {
						var output = util.fixid(doc)

						memcached.set(output.id, output, lifetime, function(err, result) {
							if(err) {
								return util.memcachedend(err);
							} else {
								console.log('MEMCACHE *** Inserted to memcached after read')
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
	search : function(req, res) {



		var input = req.query
		var output = []
		var query = null;
		var regexpkey = "/^"+req.params.key+"/i"
		var regexp2 = new RegExp(req.params.key, 'i');
		// Only search by user
		if(req.params.key == null || req.params.key == "null") {
			query = {
				"username" : req.params.userid
			}
		} else {
			query = {
				"username" : req.params.userid,
				//"name" : "/^"+req.params.key+"/i"
				"name" : regexp2
			}
		}

		var options = {
			sort : [['created', 'desc']]
		}
		
		console.log('searching by user '+query.username+' and key '+query.name)
				
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
						console.log('MEMCACHE *** Updating memcached ' + query.id + ' after update')
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

		console.log('Loading inventory collection')

		client.collection('inventory', function(err, collection) {
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
var send_social_msg = {
	twitter : function(user, msg, callback) {
		var conf = {
			consumer_key : config.twitter.key,
			consumer_secret : config.twitter.secret,
			access_token_key : user.key,
			access_token_secret : user.secret
		}
		var twit = new twitter(conf)

		var start = new Date()
		twit.updateStatus(msg, function(data) {
			var end = new Date()
			var dur = end.getTime() - start.getTime()
			console.log('twitter tweet:' + dur + ', ' + JSON.stringify(data))
			callback(data.created_at)
		})
	},

	facebook : function(user, msg, callback) {
		var start = new Date()

		var facebook_client = new facebook.FacebookClient(config.facebook.key, config.facebook.secret)

		facebook_client.getSessionByAccessToken( user.key )(function(facebook_session) {
			facebook_session.graphCall("/me/feed", {message:msg}, 'POST')(function(result) {
				var end = new Date()
				var dur = end.getTime() - start.getTime()
				console.log('facebook post:' + dur + ', ' + JSON.stringify(result))
				callback(!result.error)
			})
		})
	}
}

exports.sendpostmarkmail = function(req, res, next, from, to, subject, body, image) {

	console.log('sending postmark mail from ' + from + ' to ' + to);
	postmark.send({
		"From" : from,
		"To" : to,
		"Subject" : "Tracked Gear",
		"TextBody" : "Here is some gear you tracked",
		"Attachments" : [{
			"Content" : req.body,
			"Name" : "gear.jpg",
			"ContentType" : "image/jpeg"
		}]
	});
	console.log('done sending postmark mail...');
}

exports.get_user = function(req, res, next) {
	var clean_user = {}

	if(req.user) {
		clean_user.id = req.user.id
		clean_user.username = req.user.username
		clean_user.service = req.user.service
	}

	common.util.sendjson(res, clean_user)
}

exports.social_msg = function(req, res, next, when) {
	var user = req.user
	if(!user)
		return common.util.sendcode(400);

	if(user.service) {
		var d = new Date(parseInt(when, 10))

		send_social_msg[user.service](user, 'I just tracked some of my studio gear (my ' + decodeURIComponent(when) + ') on http://trackmygearfor.me! Track your gear! Keep it safe!', function(ok) {
			common.util.sendjson(res, {
				ok : ok
			})
		})
	}
}