function pd(func) {
	return function(event) {
		event.preventDefault()
		func && func(event)
	}
}

document.ontouchmove = pd()

var app = {}

app.init = function() {
	console.log('start init')

	var items = []
	var saveon = false
	var swipeon = false

	var elem = {
		text : $('#text'),
		save : $('#save'),
		add : $('#add'),
		cancel : $('#cancel'),
		todolist : $('#todolist'),
		newitem : $('#newitem'),
		item_tm : $('#item_tm'),
		delete_tm : $('#delete_tm')
	}

	app.items = function() {
		return items
	}

	app.activatesave = function() {
		var textlen = elem.text.val().length
		if(!saveon && 0 < textlen) {
			elem.save.css('opacity', 1)
			saveon = true
		} else if(0 == textlen) {
			elem.save.css('opacity', 0.3)
			saveon = false
		}
	}

	app.markitem = function(item, done) {
		item.find('span.check').html( done ? '&#10003;' : '&nbsp;')
		item.find('span.text').css({
			'text-decoration' : done ? 'line-through' : 'none'
		})
		app.saveitems(items)
	}

	app.saveitems = function(items) {
		localStorage.items = JSON.stringify(items)
	}

	app.loaditems = function() {
		return JSON.parse(localStorage.items || '[]')
	}

	app.addaction = function() {
		elem.add.hide()
		elem.cancel.show()
		elem.newitem.slideDown()
		saveon = false
		app.activatesave()
	}

	app.cancelaction = function() {
		elem.add.show()
		elem.cancel.hide()
		elem.newitem.slideUp()
		$('div.delete').hide()
		swipeon = false
	}

	app.saveaction = function() {
		var text = elem.text.val()
		if(0 == text.length) {
			return
		}
		elem.text.val('').blur()

		var id = new Date().getTime()
		var itemdata = {
			id : id,
			text : text,
			done : false
		}
		items.push(itemdata)
		app.additem(itemdata)

		elem.newitem.slideUp()
		elem.add.show()
		elem.cancel.hide()

		app.saveitems(items)
	}

	elem.add.tap(pd(app.addaction))
	elem.cancel.tap(pd(app.cancelaction))
	elem.save.tap(pd(app.saveaction))

	elem.text.keyup(app.activatesave)

	app.additem = function(itemdata) {
		var item = elem.item_tm.clone()
		item.attr({
			id : itemdata.id
		})
		item.find('span.text').text(itemdata.text)

		var delbutton = elem.delete_tm.clone().hide()
		item.append(delbutton)

		itemdata.delaction = function() {
			for(var i = 0; i < items.length; i++) {
				if(itemdata.id == items[i].id) {
					items.splice(i, 1)
				}
			}
			item.remove()
			elem.add.show()
			elem.cancel.hide()
			app.saveitems(items)
			return false
		}

		delbutton.attr('id', 'delete_' + itemdata.id).tap(pd(itemdata.delaction))

		app.markitem(item, itemdata.done)
		item.data('itemdata', itemdata)

		itemdata.tapaction = function() {
			if(!swipeon) {
				var itemdata = item.data('itemdata')
				app.markitem(item, itemdata.done = !itemdata.done)
			}
		}

		item.tap(pd(itemdata.tapaction))

		itemdata.swipeaction = function() {
			var itemdata = item.data('itemdata')

			if(!swipeon) {
				$('#delete_' + itemdata.id).show()
				elem.add.hide()
				elem.cancel.show()
				swipeon = true
			} else {
				elem.add.show()
				elem.cancel.hide()
				$('div.delete').hide()
				swipeon = false
			}
		}

		item.swipe(pd(itemdata.swipeaction))

		elem.todolist.append(item).listview('refresh')
	}
	items = app.loaditems()
	for(var i = 0; i < items.length; i++) {
		app.additem(items[i])
	}

	console.log('end init')
}
$(app.init)