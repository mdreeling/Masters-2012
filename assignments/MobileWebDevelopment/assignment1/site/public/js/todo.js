function pd(func) {
	return function(event) {
		event.preventDefault()
		func && func(event)
	}
}

document.ontouchmove = pd()

_.templateSettings = {
	interpolate : /\{\{(.+?)\}\}/g,
	escape : /\{\{-(.+?)\}\}/g,
	evaluate : /\{\{=(.+?)\}\}/g
};
var browser = {
	android : /Android/.test(navigator.userAgent)
}
browser.iphone = !browser.android

var app = {
	model : {},
	view : {}
}

var bb = {
	model : {},
	view : {}
}

bb.init = function() {

	var scrollContent = {
		scroll : function() {
			var self = this
			setTimeout(function() {
				if(self.scroller) {
					self.scroller.refresh()
				} else {
					self.scroller = new iScroll($("div[data-role='content']")[0])
				}
			}, 1)
		}
	}

	bb.model.State = Backbone.Model.extend(_.extend({
		defaults : {
			items : 'loading'
		}
	}))

	bb.model.Item = Backbone.Model.extend(_.extend({
		defaults : {
			text : ''
		},

		initialize : function() {
            console.log('bb.model.Item - initialize')
			var self = this
			_.bindAll(self)
		}
	}))

	bb.model.Items = Backbone.Collection.extend(_.extend({
		model : bb.model.Item,
		url : '/api/rest/todo',

		initialize : function() {
            console.log('bb.model.Items - initialize')
			var self = this
			_.bindAll(self)
			self.count = 0

			self.on('reset', function() {
                console.log('bb.model.Items - reset')
				self.count = self.length
			})
		},
		additem : function(textIn) {
            console.log('bb.model.Items - additem')
			var self = this
			var item = new bb.model.Item({
				text : textIn //'item ' + self.count
			})
			self.add(item)
			self.count++
			item.save()
		}
	}))

	bb.view.Head = Backbone.View.extend(_.extend({
		events : {
			"click #text" : function() {
				var self = this

				_.bindAll(self)

				// mdreeling - Find the main div and start playing with it
				// We can find everything if we start from here instead of the header div
				self.setElement("div[id='main']")

				self.elem = {
					todotext : self.$el.find('#text')
				}
				self.elem.todotext.focus()
			},
			'tap #add' : function() {
				var self = this

				_.bindAll(self)

				// mdreeling - Find the main div and start playing with it
				// We can find everything if we start from here instead of the header div
				self.setElement("div[id='main']")

				self.elem = {
					add : self.$el.find('#add'),
					cancel : self.$el.find('#cancel'),
					newitem : self.$el.find('#newitem'),
					todotext : self.$el.find('#text'),
					save : self.$el.find('#save')
				}

				self.elem.add.hide()
				self.elem.cancel.show()
				self.elem.newitem.slideDown()

				// mdreeling - Disable the save button until they type something
				// Pass the text and also the save element itslef so it can be disabled
				saveon = false
				app.activatesave(self.elem.todotext.val(), self.elem.save)
			},
			'tap #cancel' : function() {// mdreeling - Add the CANCEL button event
				var self = this

				_.bindAll(self)

				// mdreeling - Find the main div and start playing with it
				// We can find everything if we start from here instead of the header div
				self.setElement("div[id='main']")

				self.elem = {
					add : self.$el.find('#add'),
					cancel : self.$el.find('#cancel'),
					newitem : self.$el.find('#newitem')
				}

				// mdreeling - Just reverse the previous actions
				self.elem.add.show()
				self.elem.cancel.hide()
				self.elem.newitem.slideUp()
			},
			'tap #save' : function() {// mdreeling - Add the SAVE button event
				console.log('tap #save - saving...')
				var self = this

				_.bindAll(self)

				// mdreeling - Find the main div and start playing with it
				// We can find everything if we start from here instead of the header div
				self.setElement("div[id='main']")

				self.elem = {
					todotext : self.$el.find('#text'),
					add : self.$el.find('#add'),
					cancel : self.$el.find('#cancel'),
					newitem : self.$el.find('#newitem')
				}

				// mdreeling - Pull the item text out of the input box
				var text = self.elem.todotext.val()

				if(0 == text.length) {
					return
				}

				// mdreeling - scrub the textfield and relinquish focus
				self.elem.todotext.val('').blur()
				
				console.log('tap #save - adding item...')
				// mdreeling - Add the item to the master list
				self.items.additem(text)
				console.log('tap #save - Reversing buttons and sliding up...')
				// mdreeling - Just reverse the previous actions
				self.elem.cancel.hide()
				self.elem.add.show()
				self.elem.newitem.slideUp()
				console.log('tap #save - done!')
			},
			'keyup #text' : function() {// mdreeling - Add the KEYUP handler to enable and disbale the save button
				var self = this

				_.bindAll(self)

				// mdreeling - Find the main div and start playing with it
				// We can find everything if we start from here instead of the header div
				self.setElement("div[id='main']")

				self.elem = {
					save : self.$el.find('#save'),
					todotext : self.$el.find('#text')
				}

				// mdreeling - Toggle a save check on each keyup
				app.activatesave(self.elem.todotext.val(), self.elem.save)
			}
		},
		initialize : function(items) {
            console.log('bb.view.Head - initialize')
			var self = this
			_.bindAll(self)
			self.items = items
			self.setElement("div[data-role='header']")

			self.elem = {
				add : self.$el.find('#add'),
				title : self.$el.find('#titlebar')
			}

			self.tm = {
				title : _.template(self.elem.title.html())
			}

			self.elem.add.hide()

			app.model.state.on('change:items', self.render)
			self.items.on('add', self.render)

		},
		render : function() {
            console.log('bb.view.Head - render')
			var self = this
			// mdreeling - CHange this guy to operate on main instead of just the header div 
			// as methods which use self later - need newitem, todotext and others to actually to be there!
			self.setElement("div[id='main']")

			self.elem = {
				add : self.$el.find('#add'),
				title : self.$el.find('#titlebar'),
                todotext : self.$el.find('#text'),
                cancel : self.$el.find('#cancel'),
                newitem : self.$el.find('#newitem')
			}
			
			var loaded = 'loaded' == app.model.state.get('items')

			self.elem.title.html(self.tm.title({
				title : loaded ? self.items.length + ' Items' : 'Loading...'
			}))

			if(loaded) {
				self.elem.add.show()
			}
		}
	}))

	bb.view.List = Backbone.View.extend(_.extend({

		initialize : function(items) {
            console.log('bb.view.List - initialize')
			var self = this
			_.bindAll(self)

			self.setElement('#list')

			self.items = items
			self.items.on('add', self.appenditem)

		},
		render : function() {
            console.log('bb.view.List - render')
			var self = this

			self.$el.empty()

			// Here - we already have the HTML inside a template variable and we are going to replace an attribute of the template before appending the whole HTML fragment.
			self.items.each(function(item) {
				self.appenditem(item)
			})
		},
		appenditem : function(item) {
            console.log('bb.view.List - appenditem')
			var self = this

			var itemview = new bb.view.Item({
				model : item
			})

			self.$el.append(itemview.$el.html())
			self.scroll()

		}
	}, scrollContent))

	bb.view.Item = Backbone.View.extend(_.extend({
		initialize : function() {
            console.log('bb.view.Item - initialize')
			var self = this
			_.bindAll(self)
			self.render()
		},
		render : function() {
            console.log('bb.view.Item - render')
			var self = this
			var html = self.tm.item(self.model.toJSON())
			self.$el.append(html)
		}
	}, {
		tm : {
			item : _.template($('#list').html())
		}
	}))

}

app.init_browser = function() {
	if(browser.android) {
		$("#main div[data-role='content']").css({
			bottom : 0
		})
	}
}

app.activatesave = function(currentTextIn, save) {
    console.log('app.activatesave')
	var textlen = currentTextIn.length

	if(!saveon && 0 < textlen) {
		save.css('opacity', 1)
		saveon = true
	} else if(0 == textlen) {
		save.css('opacity', 0.3)
		saveon = false
	}
}

app.init = function() {
	console.log('start init')

	bb.init()
	app.init_browser()

	app.model.state = new bb.model.State()
	app.model.items = new bb.model.Items()
	app.view.head = new bb.view.Head(app.model.items)
	app.view.head.render()

	app.view.list = new bb.view.List(app.model.items)
	app.view.list.render()

	app.model.items.fetch({
		success : function() {
			app.model.state.set({
				items : 'loaded'
			})
			app.view.list.render()
		}
	})

	console.log('end init')
}
$(app.init)