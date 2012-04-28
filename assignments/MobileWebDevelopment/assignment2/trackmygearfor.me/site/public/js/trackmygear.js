// Mobile Web Development
// [Michael Dreeling](http://wordpress.dreeling.com/).

function pd(func) {
	return function(event) {
		event.preventDefault()
		func && func(event)
	}
}

document.ontouchmove = pd()

// Underscore template settings
_.templateSettings = {
	interpolate : /\{\{(.+?)\}\}/g,
	escape : /\{\{-(.+?)\}\}/g,
	evaluate : /\{\{=(.+?)\}\}/g
};

// User Agent tester for Android/iPhone
var browser = {
	android : /Android/.test(navigator.userAgent)
}
browser.iphone = !browser.android

// Main application variables scope
var app = {
	model : {},
	view : {},
	social : [{
		name : 'twitter'
	}, {
		name : 'facebook'
	}]
}

// Backbone application variables scope
var bb = {
	model : {},
	view : {}
}

// Backbone initialization
// ----------
bb.init = function() {

	var swipeon = false
	// Indicates that we are adding a todo item that will contain nested tasks.
	var groupedmode = false
	// Indicates that we have the map open in the app
	var mapopen = false

	// User location co-ordinates holder
	var userlocation = 'Unknown'

	var currentCapturedImageData = 'None'

	// Main router - it serves 3 functions, to allow you
	// to get to settings, the main div and back to the login if necessary
	var Router = Backbone.Router.extend({
		routes : {
			'settings' : 'showSettings',
			'login' : 'showLogin',
			'main' : 'showMain'
		},
		showSettings : function() {
		},
		showLogin : function() {
			console.log('showing login')
		},
		showMain : function() {
			console.log('showing main')
			//$('div.app-page').hide();
			//app.view.list.render();
		}
	});

	// Main application router - referenced elswhere.
	app_router = new Router();
	Backbone.history.start();

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

	var scrollForm = {
		scroll : function() {
			var self = this
			setTimeout(function() {
				if(self.scroller) {
					self.scroller.refresh()
				} else {
					self.scroller = new iScroll('scrollwrapper')
				}
			}, 1)
		}
	}

	bb.model.State = Backbone.Model.extend(_.extend({
		defaults : {
			items : 'loading'
		}
	}))

	// bb.model.Item
	// ----------
	// Contains additional attribute 'parentid' which is used for grouping
	bb.model.Item = Backbone.Model.extend(_.extend({
		defaults : {
			name : '',
			description : false,
			category : '',
			imagedata : ''
		},

		initialize : function() {
			console.log('bb.model.Item - initialize')
			var self = this
			_.bindAll(self)
		},
		// Toggle the `done` state of this todo item.
		toggle : function() {
			this.save({
				done : !this.get("done")
			});
		}
	}))

	// bb.model.LoginData
	// ----------
	// Represents login data sitting inside a collection
	bb.model.LoginData = Backbone.Model.extend(_.extend({
		defaults : {
			username : '',
			password : '',
		},

		initialize : function() {
			console.log('bb.model.LoginData - initialize')
			var self = this
			_.bindAll(self)
		}
	}))

	// bb.model.Logins
	// ----------
	// Represents a collection of login data pulled via REST from the server
	bb.model.Logins = Backbone.Collection.extend(_.extend({
		model : bb.model.LoginData,
		url : '/api/rest/login',

		initialize : function() {
			console.log('bb.model.Logins - initialize')
			var self = this
			_.bindAll(self)
		}
	}))

	// bb.model.Items
	// ----------
	// Main items Collection for the todo's
	bb.model.Items = Backbone.Collection.extend(_.extend({
		model : bb.model.Item,
		url : '/api/rest/inventory',

		initialize : function() {
			console.log('bb.model.Items - initialize')
			var self = this
			_.bindAll(self)
			self.count = 0

			self.on('reset', function() {
				console.log('bb.model.Items - reset')
				self.count = self.length
				// Re-render after a refresh - (required for groupings to work)
				app.view.list.render();
			})
		},
		// Adds an item (recording location if desired)
		additem : function(textIn, descIn, categoryIn, imageIn) {

			console.log('bb.model.Items - additem (' + textIn + ',' + descIn + ',' + categoryIn + ',' + imageIn + ')')

			var self = this

			var item = new bb.model.Item({
				name : textIn,
				description : descIn,
				category : categoryIn,
				imagedata : imageIn
			})

			console.log('bb.model.Items - adding item.')
			self.add(item)
			console.log('bb.model.Items - item added.')
			self.count++
			item.save({
				success : function() {
					addNewRow();
				}
			});
			console.log('bb.model.Items - done save.')
		}
	}))

	// bb.view.SocialMsg
	// ----------
	// For social messages
	bb.view.LoadImage = Backbone.View.extend({
		initialize : function(items) {
			var self = this
			_.bindAll(self)

			self.elem = {
				cap : {}
			}

			self.elem.cap = $('#capturebutton')
			self.elem.cap.tap(function() {
				self.capture()
			})
		},
		render : function() {
			var self = this
		},
		capture : function() {
			console.log('tapped capture')
			capturePhoto();
		}
	})

	// bb.view.SocialMsg
	// ----------
	// For social messages
	bb.view.SocialMsg = Backbone.View.extend({
		initialize : function(items) {
			var self = this
			_.bindAll(self)

			self.elem = {
				msg : {}
			}
			app.social.forEach(function(service) {
				self.elem.msg[service.name] = $('#social_msg_' + service.name)
				self.elem.msg[service.name].tap(function() {
					self.socialmsg(service)
				})
			})

			app.model.state.on('change:user', self.render)

		},
		render : function() {
			var self = this

			var user = app.model.state.get('user')
			console.log('user is ' + user)
		},
		socialmsg : function(service) {
			console.log('tapped ' + service.name)

			var currentTime = new Date();

			http.post('/user/socialmsg/' + currentTime, {}, function(res) {
				alert(res.ok ? 'Message sent!' : 'Unable to send message.')
			})
		}
	})

	// bb.view.Head
	// ----------
	// This view defines common behaviour for all headers but not particular one specifically (as it did before)
	// I moved most of the functionality out to the Main view once i realized that it did not really make sense once
	// you had more than one view.
	bb.view.Head = Backbone.View.extend(_.extend({
		initialize : function(items) {
			console.log('bb.view.Head - initialize')
			var self = this
			_.bindAll(self)
		},
		render : function() {
			console.log('bb.view.Head - render')
			var self = this
		}
	}))

	// bb.view.Login
	// ----------
	// The login view is mainly here to allow me to work with the login action button
	// to verify the user via the REST api. I was unable to get the main div to properly reload
	// after a successful login - so the only way to very this is working is to go to
	// http://todo.dreeling.com/#login and enter test and test, which queries the database
	// via REST and checks the password. It only moves the div forward if the password is correct
	bb.view.Login = Backbone.View.extend(_.extend({
		events : {
			'tap #Submit1' : 'tapLogin',
			'tap #username' : 'tapUser',
			'tap #password' : 'tapPass'
		},
		initialize : function() {
			console.log('bb.view.Settings - initialize')
			var self = this
			_.bindAll(self)
			self.setElement("div[id='login']")
		},
		render : function() {
			console.log('bb.view.Settings - render')
			var self = this
			_.bindAll(self)
			self.elem = {
				title : self.$el.find('#titlebar'),
			}

			self.tm = {
				title : _.template(self.elem.title.html())
			}

			self.elem.title.html(self.tm.title({
				title : 'Todo App Login'
			}))
		},
		tapLogin : function() {
			console.log('In tap Login...')
			var self = this
			_.bindAll(self);

			self.elem = {
				user : self.$el.find('#username'),
				pass : self.$el.find('#password')
			}

			// Go to the server and pull the login data via REST.
			// The users are already pre-loaded in the database
			app.model.logins.fetch({
				data : {
					id : self.elem.user.val()
				},
				success : function() {

					app.model.logins.each(function(item) {
						if(item.attributes.username == self.elem.user.val()) {
							if(self.elem.pass.val() == item.attributes.password) {
								app_router.navigate("main", {
									trigger : true
								});
								//app.listinit()
							} else {
								alert('Bad password');
							}
						}
					})
				},
			});
		},
		tapUser : function() {
			console.log('In tap User...')
			var self = this
			_.bindAll(self)
			self.$el.find('#username').focus();
		},
		tapPass : function() {
			console.log('In tap Pass...')
			var self = this
			_.bindAll(self)
			self.$el.find('#password').focus();
		}
	}))

	// bb.view.Main
	// ----------
	// The main view. This is hte view that should render the list items - and only this view.
	// Previously the header was in charge of everything.
	bb.view.Main = Backbone.View.extend(_.extend({
		events : {
			'tap #text' : 'enterText',
			'tap #settings' : 'goToSettings',
			// FIX FOR IPAD - tap event does NOT work. Triggers some erroneous junk events.
			'click #backtotoplevel' : 'goToMain',
			// FIX FOR IPAD - tap event does NOT work. Triggers some erroneous junk events.
			// 'click #add' : 'tapAdd', REMOVE for now - not needed.
			'tap #cancel' : 'cancelTodoEntry',
			'tap #save' : 'saveTodoEntry',
			'keyup #text' : 'keyupTodoText'
		},
		initialize : function(items) {
			console.log('bb.view.Main - initialize')
			var self = this
			_.bindAll(self)
			self.items = items
			self.setElement("div[id='main']")

			self.elem = {
				add : self.$el.find('#add'),
				title : self.$el.find('#titlebar')
			}

			self.tm = {
				title : _.template(self.elem.title.html())
			}

			self.elem.add.hide()

			app.model.state.on('change:items', self.render)
			app.model.state.on('reset:items', self.render)
			// I added this because there were some issues with the save callback.
			// This is in replacement of addNewRow
			self.items.on('sync', self.render)
		},
		render : function() {
			console.log('bb.view.Main - render')
			var self = this

			self.elem = {
				add : self.$el.find('#add'),
				title : self.$el.find('#titlebar'),
				todotext : self.$el.find('#text'),
				cancel : self.$el.find('#cancel'),
				newitem : self.$el.find('#newitem')
			}

			var loaded = 'loaded' == app.model.state.get('items')

			console.log('bb.view.Main - loaded=' + loaded)

			self.elem.title.html(self.tm.title({
				title : loaded ? self.items.length + ' Items' : 'Loading...'
			}))

			console.log('bb.view.Main - title=' + loaded ? self.items.length + ' Items' : 'Loading...')

			if(loaded) {
				self.elem.add.show()
			}
		},
		// This method is here to allow main to refresh 'itself'
		// When you use groupings - they are also displayed in the main div.
		goToMain : function() {

			var self = this
			_.bindAll(self)
			groupedmode = false;
			app.activegroupingparent = null;
			console.log('tap #showgroups - Showing main tasks ');

			app.model.items.fetch({
				success : function() {
					app.model.state.set({
						items : 'loaded'
					})
					app.view.list.render()
				}
			});

			self.setElement("div[data-role='header']")

			self.elem = {
				back : self.$el.find('#backtotoplevel'),
				settings : self.$el.find('#settings')
			}

			self.elem.back.hide()
			self.elem.settings.show()
		},
		goToSettings : function() {
			app_router.navigate("settings");
		},
		cancelTodoEntry : function() {
			var self = this
			_.bindAll(self)

			self.elem = {
				add : self.$el.find('#add'),
				cancel : self.$el.find('#cancel'),
				newitem : self.$el.find('#newitem')
			}
			// Just reverse the previous actions
			self.elem.add.show()
			self.elem.cancel.hide()
			self.elem.newitem.slideUp()
		},
		saveTodoEntry : function() {// Add the SAVE button event
			console.log('tap #save - saving...')
			var self = this

			_.bindAll(self)

			self.elem = {
				todotext : self.$el.find('#text'),
				add : self.$el.find('#add'),
				cancel : self.$el.find('#cancel'),
				newitem : self.$el.find('#newitem')
			}

			// Pull the item text out of the input box
			var text = self.elem.todotext.val()

			if(0 == text.length) {
				return
			}

			// scrub the textfield and relinquish focus
			self.elem.todotext.val('').blur()

			console.log('tap #save - adding item...')
			// Add the item to the master list
			self.items.additem(text)
			console.log('tap #save - Reversing buttons and sliding up...')
			// Just reverse the previous actions
			self.elem.cancel.hide()
			self.elem.add.show()
			self.elem.newitem.slideUp()
			console.log('tap #save - done!')
		},
		keyupTodoText : function() {// Add the KEYUP handler to enable and disbale the save button
			var self = this

			_.bindAll(self)

			self.elem = {
				save : self.$el.find('#save'),
				todotext : self.$el.find('#text')
			}

			// Toggle a save check on each keyup
			app.activatesave(self.elem.todotext.val(), self.elem.save)
		},
		tapAdd : function() {
			var self = this

			_.bindAll(self)

			self.elem = {
				add : self.$el.find('#add'),
				cancel : self.$el.find('#cancel'),
				newitem : self.$el.find('#newitem'),
				todotext : self.$el.find('#text'),
				save : self.$el.find('#save')
			}

			self.elem.cancel.show()
			self.elem.add.hide()
			self.elem.newitem.slideDown()
			self.elem.todotext.focus()

			// Disable the save button until they type something
			// Pass the text and also the save element itslef so it can be disabled
			saveon = false
			app.activatesave(self.elem.todotext.val(), self.elem.save)
		},
		enterText : function() {
			var self = this

			_.bindAll(self)

			self.elem = {
				todotext : self.$el.find('#text')
			}

			self.elem.todotext.focus()
		}
	}))

	// bb.view.Settings
	// ----------
	// The settings view. This allows you to change the DOM to a new theme.
	// I used some GitHub code here which was not setup for Backbone.
	// I switched it up to be used in the style of my own app.
	// https://gist.github.com/1117707
	bb.view.Settings = Backbone.View.extend(_.extend({
		events : {
			'tap #first-content a' : 'tapTheme',
			'tap #back' : 'goBack'
		},
		initialize : function() {
			console.log('bb.view.Settings - initialize')
			var self = this
			_.bindAll(self)
			self.setElement("div[id='settings']")

			self.elem = {
				title : self.$el.find('#titlebar')
			}

			self.tm = {
				title : _.template(self.elem.title.html())
			}

			self.on('refresh', function() {
				console.log('Rebinding theme...')
				// Default to the "a" theme
				var oldTheme = self.$el.attr('data-theme') || 'a';
				var newTheme = 'b';

				app.elemthemerefresh(self.el, oldTheme, newTheme);

				self.$el.find('*').each(function() {
					app.elemthemerefresh($(this), oldTheme, newTheme);
				});
			})
		},
		render : function() {
			console.log('bb.view.Settings - render')
			var self = this
			_.bindAll(self)

			self.elem = {
				title : self.$el.find('#titlebar'),
			}

			self.elem.title.html(self.tm.title({
				title : 'Settings'
			}))
		},
		tapTheme : function() {
			console.log('In tap Theme...')
			var newTheme = $(this).attr('theme');

			var self = this
			_.bindAll(self)
			self.trigger('refresh', newTheme);
			console.log('Switchng theme...')

			self.setElement("div[id='main']")
			self.trigger('refresh', newTheme);
			console.log('Switchng main theme...')
		},
		goBack : function() {
			window.history.back()
		}
	}))

	// bb.view.NewItem
	// ----------
	// The new item view. This is a seperate view to handle only the NewItem div.
	// I switched this DIV back up to the Header View because i had problems with iScroll.
	bb.view.NewItem = Backbone.View.extend(_.extend({
		events : {
			'change #locate-slider' : 'locateSlider'
		},
		initialize : function() {
			console.log('bb.view.NewItem - initialize')
			var self = this
			_.bindAll(self)
			self.setElement("div[id='newitem']")
		},
		render : function() {
			console.log('bb.view.NewItem - render')
			var self = this
			_.bindAll(self)
		},
		// This toggles location for the current todo.
		locateSlider : function() {
			var self = this
			_.bindAll(self)
			self.setElement("div[id='newitem']")
			self.elem = {
				ls : self.$el.find('#locate-slider')
			}

			var selectedOption = self.elem.ls[0].options[self.elem.ls[0].selectedIndex];

			if(selectedOption.value == "on") {
				console.log('tap #locateSlider - locating...')
				app.lastselectedgeo = "on";
				app.initgeo();
			} else {
				app.lastselectedgeo = "off";
			}
		}
	}))

	// bb.view.NewInventoryItem
	// ----------
	// The new item inventory item view
	bb.view.NewInventoryItem = Backbone.View.extend(_.extend({
		events : {
			'tap #saveinvbutton' : 'locateSlider'
		},
		initialize : function() {
			console.log('bb.view.NewInventoryItem - initialize')
			var self = this
			_.bindAll(self)
			self.setElement("div[id='additem']")
			self.items = app.model.items

			self.scroller
		},
		render : function() {
			console.log('bb.view.NewInventoryItem - render')
			var self = this
			_.bindAll(self)

			self.scroller = new iScroll('scrollwrapper', {
				useTransform : false,
				onBeforeScrollStart : function(e) {
					var target = e.target;
					while(target.nodeType != 1)
					target = target.parentNode;

					if(target.tagName != 'SELECT' && target.tagName != 'INPUT' && target.tagName != 'TEXTAREA')
						e.preventDefault();
				}
			});

			app.scrollheight = window.innerHeight - 80;

			var content = $("#scrollwrapper")
			content.height(app.scrollheight)

			setTimeout(function() {
				self.scroller.refresh()
			}, 300)
		},
		// This toggles location for the current todo.
		locateSlider : function() {
			var self = this
			_.bindAll(self)

			self.elem = {
				itemnametext : self.$el.find('#itemname'),
				itemdesctext : self.$el.find('#itemdesc'),
				itemcat : self.$el.find('#itemcat'),
				itemimagedata : self.$el.find('#itemimage')
			}

			// Pull the item text out of the input box
			var iname = self.elem.itemnametext.val()
			var idesc = self.elem.itemdesctext.val()
			var selectedCat = self.elem.itemcat[0].options[self.elem.itemcat[0].selectedIndex];
			///var iimg = app.currentCapturedImageData

			if(0 == text.length) {
				return
			}

			// scrub the textfield and relinquish focus
			//self.elem.todotext.val('').blur()

			console.log('tap #save - adding item...')
			// Add the item to the master list
			self.items.additem(iname, idesc, selectedCat.text, app.currentCapturedImageData)
			// Just reverse the previous actions
			console.log('tap #save - done!')
		}
	}))

	// bb.view.List
	// ----------
	// The main list view which is scrollable. I initially had problems with iScroll
	// where if the list was included AFTER a new item DIV - it simply would not scroll.
	// I resolved this by leaving the list by itself inside the content pane.
	bb.view.List = Backbone.View.extend(_.extend({
		initialize : function(items) {
			console.log('bb.view.List - initialize')
			var self = this
			_.bindAll(self)

			self.setElement('#list')

			self.items = items
			// Again - i used sync here due to bugs on callbacks.
			self.items.on('sync', self.appenditem)
			app.model.state.on('change:items', self.render)

		},
		render : function() {
			console.log('bb.view.List - render')
			var self = this

			self.$el.empty()

			// Here - we already have the HTML inside a template variable and we are going to replace an attribute of the template before
			// appending the whole HTML fragment.
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

			// MAJOR BUG HERE that took hours to fix.
			// self.$el.append(itemview.el$.html())
			// Events are lost and never fire for an item if you use above code.
			self.$el.append(itemview.el)
			self.scroll()

		}
	}, scrollContent))

	// bb.view.List
	// ----------
	// The per item view.
	bb.view.Item = Backbone.View.extend(_.extend({
		events : {
			"tap .check" : "markItem",
			"swipe .tm" : "swipeItem",
			"tap .delete" : "deleteItem",
			"tap .showgroupings" : "showGroupedView",
			"tap .showmap" : "showMapView"
		},
		initialize : function() {
			console.log('bb.view.Item - initialize')
			var self = this
			_.bindAll(self)
			self.render()
		},
		render : function() {
			console.log('bb.view.Item - render')
			var self = this
			console.log('bb.view.Item - appending id -> ' + self.model.attributes.id)

			var html = self.tm.item(self.model.toJSON())

			self.$el.append(html)

			// Mark the item per its database model value
			app.markitem(self.$el, self.model.attributes.done)
			if(self.model.attributes.parentid != null) {
				self.elem = {
					grp : self.$el.find('.showgroupings'),
				}
				self.elem.grp.hide()
			} else {

			}

			// Hide show map if there are no co-ordinates stored with the todo
			if(self.model.attributes.location == null) {
				self.elem = {
					grp : self.$el.find('.showmap'),
				}
				self.elem.grp.hide()
			}
		},
		// showMapView
		// ----------
		// Uses google maps API to generate an interactive map.
		// The map is specific to the item.
		showMapView : function() {// Add the CHECKBOX button event
			var self = this
			_.bindAll(self)

			// Map button toggle code.
			if(!mapopen) {
				console.log('tap #showmaps - Showing map for task ' + self.model.attributes.text)

				self.elem = {
					map : self.$el.find('#mapContainer_' + self.model.attributes.id),
				}
				app.generateMap(self.model.attributes.id, self.model.attributes.location)
				$('#showmap_' + self.model.attributes.id).text('Close Map');
				mapopen = true;
			} else {
				$('#mapContainer_' + self.model.attributes.id).hide();
				mapopen = false
				$('#showmap_' + self.model.attributes.id).text('Show Map');
			}

		},
		// showGroupedView
		// ----------
		// This view is to try satisfy the lists of lists requirement. It basically
		// reloads the main div with a new model - the previous items children.
		// This allows for infinite nesting. I only allow 1 level.
		// This could also be solved i am assuming - by using a Collection inside
		// a Collection but i did not explorer this opton due to time constraints.
		showGroupedView : function() {// Add the CHECKBOX button event
			var self = this
			_.bindAll(self)
			groupedmode = true;
			app.activegroupingparent = self.model.attributes.id;
			console.log('tap #showgroups - Showing groupings for task ' + self.model.attributes.text + '.' + app.activegroupingparent)

			app.model.items.fetch({
				data : {
					parentid : app.activegroupingparent
				}
			});

			self.setElement("div[data-role='header']")

			self.elem = {
				// This action just 'returns' the user to the main div
				back : self.$el.find('#backtotoplevel'),
				settings : self.$el.find('#settings')
			}

			self.elem.back.show()
			self.elem.settings.hide()
		},
		// Add the delete button event
		deleteItem : function() {
			console.log('tap #delete - deleting...')
			var self = this

			_.bindAll(self)
			self.setElement("li[id='" + self.model.attributes.id + "']")

			self.remove()
			self.model.destroy();
			self.unbind();

			self.setElement("div[data-role='header']")

			self.elem = {
				add : self.$el.find('#add'),
				cancel : self.$el.find('#cancel')
			}

			self.elem.add.show()
			self.elem.cancel.hide()
			console.log('tap #delete - done!')
		},
		tapItem : function() {
			console.log('tap #tapItem...')
			var self = this
			_.bindAll(self);
		},
		markItem : function() {
			console.log('tap #check - marking...')
			var self = this

			_.bindAll(self)
			self.model.toggle()
			app.markitem(self.$el, self.model.attributes.done)
			console.log('tap #check - done!')
		},
		swipeItem : function() {// Add the CHECKBOX button event
			console.log('swipe #item - iniating delete...')
			var self = this
			_.bindAll(self)

			var itemdata = self.model.attributes

			self.setElement("div[data-role='header']")

			self.elem = {
				add : self.$el.find('#add'),
				cancel : self.$el.find('#cancel')
			}

			if(!swipeon) {
				console.log('swipe #item - showing delete button ' + itemdata.id)
				$('#delete_' + itemdata.id).show()
				self.elem.add.hide()
				self.elem.cancel.show()
				swipeon = true
			} else {
				self.elem.add.show()
				self.elem.cancel.hide()
				$('div.delete').hide()
				swipeon = false
			}
		}
	}, {
		tm : {
			item : _.template($('#list').html())
		}
	}))

}
// init_browser
// ----------
// Snips the bottom off for android
app.init_browser = function() {
	if(browser.android) {
		$("#main div[data-role='content']").css({
			bottom : 0
		})
	}
}
// markitem
// ----------
// Modifies the css styling of the checkbox
app.markitem = function(item, done) {
	item.find('span.check').html( done ? '&#10003;' : '&nbsp;')
	item.find('span.text').css({
		'text-decoration' : done ? 'line-through' : 'none'
	})
}
// activatesave
// ----------
// Fades in and out the save box
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
// init
// ----------
// Main init
app.init = function() {
	console.log('start init')

	bb.init()
	app.init_browser()

	app.model.state = new bb.model.State()
	app.model.items = new bb.model.Items()
	app.model.logins = new bb.model.Logins()
	app.view.head = new bb.view.Head(app.model.items)
	app.view.head.render()

	app.view.login = new bb.view.Login({
		el : $("#login")
	})
	app.view.login.render()

	app.listinit()
}
// listinit
// ----------
// I seperated this out when i started working on login because i thought it might make sense.
app.listinit = function() {

	app.view.loadimage = new bb.view.LoadImage({
		el : $("#capturebutton")
	})
	app.view.loadimage.render()

	app.view.socialmsg = new bb.view.SocialMsg({
		el : $("#social_msg_twitter")
	})
	app.view.socialmsg.render()

	app.view.additemview = new bb.view.NewInventoryItem({
		el : $("#additem")
	})
	app.view.additemview.render()

	app.view.newitemview = new bb.view.NewItem({
		el : $("#newitem")
	})
	app.view.newitemview.render()

	app.view.main = new bb.view.Main(app.model.items, {
		el : $("#main")
	})
	app.view.main.render()

	app.view.sett = new bb.view.Settings({
		el : $("#settings")
	})
	app.view.sett.render()

	app.view.list = new bb.view.List(app.model.items)
	app.view.list.render()

	app.model.items.fetch({
		success : function() {
			app.model.state.set({
				items : 'loaded'
			})
			app.view.list.render()
		}
	});
	console.log('end init')
}
// elemthemerefresh
// ----------
// GitHub ripped code to refresh the theme.
app.elemthemerefresh = function element_theme_refresh(element, oldTheme, newTheme) {
	/* Update the page's new data theme. */
	if($(element).attr('data-theme')) {
		$(element).attr('data-theme', newTheme);
	}
	console.log('Changing theme...')
	if($(element).attr('class')) {
		/* Theme classes end in "-[a-z]$", so match that */
		var classPattern = new RegExp('-' + oldTheme + '$');
		newTheme = '-' + newTheme;

		var classes = $(element).attr('class').split(' ');

		for(var key in classes) {
			if(classPattern.test(classes[key])) {
				classes[key] = classes[key].replace(classPattern, newTheme);
			}
		}

		$(element).attr('class', classes.join(' '));
	}
}
// initgeo
// ----------
// Gets the current location
app.initgeo = function initiate_geolocation() {

	if(navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {
			// Set location for Backbone to see it.
			app.userlocation = position
		});
		console.log('done locating!')
	} else {
		alert("Geolocation API is not supported in your browser.");
	}
}
// initgeo
// ----------
// Google ripped and modified.... Creates a really nice map on a per item basis.
app.generateMap = function genMapforLocation(id, position) {

	// Set location for Backbone to see it.
	app.userlocation = position

	var latitude = position.coords.latitude;
	var longitude = position.coords.longitude;
	var coords = new google.maps.LatLng(latitude, longitude);
	var mapOptions = {
		zoom : 15,
		center : coords,
		mapTypeControl : true,
		navigationControlOptions : {
			style : google.maps.NavigationControlStyle.SMALL
		},
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("mapContainer_" + id), mapOptions);
	var marker = new google.maps.Marker({
		position : coords,
		map : map,
		title : "Where you created this todo!"
	});

	$('#mapContainer_' + id).show();

	console.log('done genmapping!')
}
// Called when a photo is successfully retrieved
//
function onPhotoDataSuccess(imageData) {
	// Uncomment to view the base64 encoded image data
	// console.log(imageData);

	// Get image handle
	//
	var smallImage = document.getElementById('smallImage');

	// Unhide image elements
	//
	smallImage.style.display = 'block';

	// Show the captured photo
	// The inline CSS rules are used to resize the image
	//
	smallImage.src = "data:image/jpeg;base64," + imageData;
	app.currentCapturedImageData = imageData;
}

// Called when a photo is successfully retrieved
//
function onPhotoURISuccess(imageURI) {
	// Uncomment to view the image file URI
	// console.log(imageURI);

	// Get image handle
	//
	var largeImage = document.getElementById('largeImage');

	// Unhide image elements
	//
	largeImage.style.display = 'block';

	// Show the captured photo
	// The inline CSS rules are used to resize the image
	//
	largeImage.src = imageURI;
}

// A button will call this function
//
function capturePhoto() {
	// Take picture using device camera and retrieve image as base64-encoded string
	navigator.camera.getPicture(onPhotoDataSuccess, onFail, {
		quality : 50,
		destinationType : destinationType.DATA_URL
	});
}

// A button will call this function
//
function capturePhotoEdit() {
	// Take picture using device camera, allow edit, and retrieve image as base64-encoded string
	navigator.camera.getPicture(onPhotoDataSuccess, onFail, {
		quality : 20,
		allowEdit : true,
		destinationType : destinationType.DATA_URL
	});
}

// A button will call this function
//
function getPhoto(source) {
	// Retrieve image file location from specified source
	navigator.camera.getPicture(onPhotoURISuccess, onFail, {
		quality : 50,
		destinationType : destinationType.FILE_URI,
		sourceType : source
	});
}

// Called if something bad happens.
//
function onFail(message) {
	alert('Failed because: ' + message);
}

$(app.init)