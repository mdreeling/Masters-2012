// Main application variables scope
var tmgapp = {
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
    
    console.log("bb.init");
    
	var swipeon = false
	// Indicates that we are adding a todo item that will contain nested tasks.
	var groupedmode = false
	// Indicates that we have the map open in the app
	var mapopen = false

	// User location co-ordinates holder
	var userlocation = 'Unknown'

	var currentCapturedImageData = 'None'
	var currentCapturedSerialNoImageData = 'None'
	var loggedin = false

	var loggedinusername = 'notloggedin'

	var AppRouter = Backbone.Router.extend({

		routes : {
			"" : "list",
			"list" : "list",
			"gearitems/:id" : "gearitemDetails",
			"addgear" : "addGear",
			"gearitems/:id/reports" : "directReports"
		},

		initialize : function() {

			var self = this;

			tmgapp.model.state.on('change:user', self.userChanged)

			// Keep track of the history of pages (we only store the page URL). Used to identify the direction
			// (left or right) of the sliding transition between pages.
			this.pageHistory = [];

			// Register event listener for back button troughout the app
			$('#content').on('click', '.header-back-button', function(event) {
				window.history.back();
				return false;
			});

			// Check of browser supports touch events...
			if('ontouchstart' in document.documentElement) {
				// ... if yes: register touch event listener to change the "selected" state of the item
				$('#content').on('touchstart', 'a', function(event) {
					self.selectItem(event);
				});
				$('#content').on('touchend', 'a', function(event) {
					self.deselectItem(event);
				});
			} else {
				// ... if not: register mouse events instead
				$('#content').on('mousedown', 'a', function(event) {
					self.selectItem(event);
				});
				$('#content').on('mouseup', 'a', function(event) {
					self.deselectItem(event);
				});
			}
                                           
			// Make a call out to the OAuth in order to set the user variable.
			http.get('/user', function(user) {
				if(user.id) {
					tmgapp.model.state.set({
						user : user
					})
				}
			})
			if(loggedin) {
				renderSearch();
			} else {
				this.loginPage = new bb.view.LoginPage({
					model : '{ "social": "twitter"}'
				});
				this.loginPage.render();
			}

		},
		renderSearch : function() {
			// We keep a single instance of the SearchPage and its associated GearItem collection throughout the app
			tmgapp.model.items = new bb.model.GearItemCollection();
			this.searchPage = new bb.view.SearchPage({
				model : tmgapp.model.items
			});
			this.searchPage.render();

		},

		selectItem : function(event) {
			$(event.target).addClass('tappable-active');
		},

		deselectItem : function(event) {
			$(event.target).removeClass('tappable-active');
		},

		list : function() {
			this.renderSearch();
			var self = this;
			this.slidePage(this.searchPage);
		},
		login : function() {
			var self = this;
			this.slidePage(this.loginPage);
		},
		addGear : function(id) {
			var gearitem = new bb.model.GearItem({
				id : id
			});
			var self = this;

			self.slidePage(new bb.view.AddGearItemPage({
				model : '{ "category": "speaker"}'
			}).render());
		},

		gearitemDetails : function(id) {
			var gearitem = new bb.model.GearItem({
				id : id
			});
			var self = this;
			gearitem.fetch({
				success : function(data) {
					self.slidePage(new bb.view.GearItemPage({
						model : data
					}).render());
				}
			});
		},
		slidePage : function(page) {

			if(!this.currentPage) {
				// If there is no current page (app just started) -> No transition: Position new page in the view port
				$(page.el).attr('class', 'page stage-center');
				$('#content').append(page.el);
				this.pageHistory = [window.location.hash];
				this.currentPage = page;
				return;
			}

			// Cleaning up: remove old pages that were moved out of the viewport
			$('.stage-right, .stage-left').remove();

			var slideFrom;
			if(page == this.searchPage) {
				// Always apply a Back (slide from left) transition when we go back to the search page
				slideFrom = "left";
				$(page.el).attr('class', 'page stage-left');
				// Reinitialize page history
				this.pageHistory = [window.location.hash];
			} else if(this.pageHistory.length > 1 && window.location.hash === this.pageHistory[this.pageHistory.length - 2]) {
				// The new page is the same as the previous page -> Back transition
				slideFrom = "left";
				$(page.el).attr('class', 'page stage-left');
				this.pageHistory.pop();
			} else {
				// Forward transition (slide from right)
				slideFrom = "right";
				$(page.el).attr('class', 'page stage-right');
				this.pageHistory.push(window.location.hash);
			}

			$('#content').append(page.el);

			var self = this;

			// Wait until the new page has been added to the DOM...
			setTimeout(function() {
				// Slide out the current page: If new page slides from the right -> slide current page to the left, and vice versa
				$(self.currentPage.el).attr('class', 'page transition ' + (slideFrom == "right" ? 'stage-left' : 'stage-right'));
				// Slide in the new page
				$(page.el).attr('class', 'page stage-center transition');
				self.currentPage = page;
			});

		},
		userChanged : function() {
			var self = this;
			var user = tmgapp.model.state.get('user')
			console.log('user ' + user.username + ' is logged on via ' + user.service)

			app.loggedinusername = user.username;

			if(app.loggedinusername.length > 0) {
				window.location.href = "#list"
			}

		},
	});

	tpl.loadTemplates(['search-page', 'login-page', 'gearitem-page', 'editgearitem-page','addgearitem-page', 'gearitem-list-item'], function() {
		app = new AppRouter();
		Backbone.history = Backbone.history || new Backbone.History({});
		Backbone.history.start();
	});

}
// init
// ----------
// Main init
tmgapp.listitems = function() {
	this.slidePage(this.searchPage);
}
// init
// ----------
// Main init
tmgapp.init = function() {

	console.log('start init')
	bb.init()

	tmgapp.model.state = new bb.model.State()
	tmgapp.model.state.init()
}

$(tmgapp.init)
