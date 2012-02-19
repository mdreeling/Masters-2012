var dbutil = require('./db-util')
//var dbutil = require('./db-auth')

var client

function err(win) {
	return function(err, data) {
		if(err) {
			console.log(err);
		} else if(win) {
			win(data)
		}
	}
}

function query() {
	client.collection('color', err(function(color) {
		color.find({}, {}, err(function(cursor) {
			cursor.toArray(err(function(docs) {
				console.log('\n' + new Date())
				docs.forEach(function(doc) {
					console.log(JSON.stringify(doc))
				})
			}))
		}))
	}))
}

function init() {
	dbutil.connect({
		name : 'lab05',

		server : 'flame.mongohq.com',
		port : 27107,
		username : 'test',
		password : 'mwd05'
	}, function(err, db_client) {
		if(err)
			return console.log(err);
		client = db_client
		setInterval(query, 1000)
	})
}

init()