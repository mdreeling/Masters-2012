bb.view.LoginPage = Backbone.View.extend({

	initialize : function() {
		console.log('bb.view.LoginPage - initialize')
		var self = this
		_.bindAll(self)
		this.template = _.template(tpl.get('login-page'));
	},

	render : function(eventName) {
		$(this.el).html(this.template());
		return this;
	}
});

bb.view.SearchPage = Backbone.View.extend({

	initialize : function() {
		this.template = _.template(tpl.get('search-page'));
	},

	render : function(eventName) {
		$(this.el).html(this.template(this.model.toJSON()));
		this.listView = new bb.view.GearItemListView({
			el : $('ul', this.el),
			model : this.model
		});
		this.listView.render();
		this.model.findByName(null);
		return this;
	},

	events : {
		"keyup .search-key" : "search"
	},

	search : function(event) {
		var key = $('.search-key').val();
		this.model.findByName(key);
	}
});

bb.view.GearItemListView = Backbone.View.extend({

	initialize : function() {
		this.model.bind("reset", this.render, this);
	},

	render : function(eventName) {
		$(this.el).empty();
		_.each(this.model.models, function(gearitem) {
			console.log('rendering ' + gearitem);
			$(this.el).append(new bb.view.GearItemListItemView({
				model : gearitem
			}).render().el);
		}, this);
		return this;
	}
});

bb.view.GearItemListItemView = Backbone.View.extend({

	tagName : "li",

	initialize : function() {
		this.template = _.template(tpl.get('gearitem-list-item'));
	},

	render : function(eventName) {
		$(this.el).html(this.template(this.model.toJSON()));
		return this;
	}
});

bb.view.GearItemPage = Backbone.View.extend({
	events : {
		'click #tweetbutton' : 'tweet',
		'tap #tweetbutton' : 'tweet',
		'click #postmarkbutton' : 'postmarkit',
		'tap #postmarkbutton' : 'postmarkit',
		'click #editbutton' : 'edit'
	},
	initialize : function() {
		this.template = _.template(tpl.get('gearitem-page'));
	},

	render : function(eventName) {
		$(this.el).html(this.template(this.model.toJSON()));
		return this;
	}, // This toggles location for the current todo.
	tweet : function() {
		console.log('bb.view.GearItemPage - tweeting...'+this.model.attributes.name+' '+this.model.attributes.model)
                                 
                                            window.plugins.twitter.getTwitterUsername(function(r){
                                                                                      console.log("twitter username is " + r);
                                                                                      app.loggedinusername = r;
                                                                                      }); 
                                            
                                            window.plugins.twitter.composeTweet(
                                                                                function(s){ console.log("tweet success"); }, 
                                                                                function(e){ console.log("tweet failure: " + e); }, 
                                                                                "I just tracked some of my studio gear (my "+this.model.attributes.name+" "+this.model.attributes.model+") on http://trackmygearfor.me! Track your gear! Keep it safe!");
                                            
                                            window.plugins.FlurryPlugin.logEvent('GEAR_PAGE_TWEETED')
	},
	postmarkit : function() {
		var email = $('input[id=emailaddr]').val();
		console.log('bb.view.GearItemPage - emailing...'+email)
		var currentTime = new Date();
		
		http.post('http://www.trackmygearfor.me/sendmail/tmg@dreeling.com/'+email, this.model.attributes.imagedata, function(res) {
			console.log(res.ok ? 'mail sent!' : 'Unable to send mail.')
		})
		
		alert('Mail sent!')
	},
	edit : function() {
		app.slidePage(new bb.view.EditGearItemPage({
				model : this.model
			}).render());
	}
});

bb.view.AddGearItemPage = Backbone.View.extend({
	events : {
		'click #saveinvbutton' : 'saveItem',
		'click #capturebutton' : 'capture',
		'click #captureserialbutton' : 'captureSerial',
		'tap #saveinvbutton' : 'saveItem',
		'tap #capturebutton' : 'capture',
		'tap #captureserialbutton' : 'captureSerial'
	},
	initialize : function() {
		this.template = _.template(tpl.get('addgearitem-page'));
		console.log('bb.view.AddGearItemPage - initialize')
		var self = this
		_.bindAll(self)
		self.items = tmgapp.model.items
        window.plugins.FlurryPlugin.logEvent('ADDGEAR_PAGE_VISIT')
        window.plugins.FlurryPlugin.pageView();
	},

	render : function(eventName) {
		$(this.el).html(this.template());
		return this;
	}, // This toggles location for the current todo.
	saveItem : function() {
		var self = this
		_.bindAll(self)

		console.log('bb.view.AddGearItemPage - tap #save - starting...')

		self.elem = {
			itemnametext : self.$el.find('#itemmake'),
			itemmodeltext : self.$el.find('#itemmodel'),
			itemdesctext : self.$el.find('#itemdesc'),
			itemcat : self.$el.find('#itemcat'),
			itemserialtext : self.$el.find('#itemserial')
		}

		// Pull the item text out of the input box
		var iname = self.elem.itemnametext.val()
		var imodel = self.elem.itemmodeltext.val()
		var idesc = self.elem.itemdesctext.val()
		var iserial = self.elem.itemserialtext.val()
		var selectedCat = self.elem.itemcat[0].options[self.elem.itemcat[0].selectedIndex];
		///var iimg = app.currentCapturedImageData

		if(0 == iname.length || 0 == imodel.length || 0 == idesc.length) {
			console.log('bb.view.AddGearItemPage - nothing to save yet')
			return
		}

		// scrub the textfield and relinquish focus
		//self.elem.todotext.val('').blur()

		console.log('tap #save - adding item...')
		// Add the item to the master list
		self.items.additem(iname, imodel, idesc, selectedCat.text, app.currentCapturedImageData, iserial, app.currentCapturedSerialNoImageData)
		// Just reverse the previous actions
		console.log('tap #save - done!')
		window.history.back();
        window.plugins.FlurryPlugin.logEvent('ADDGEAR_PAGE_SAVE_DONE')
	},
	capture : function() {
		console.log('tapped capture')
        window.plugins.FlurryPlugin.logEvent('ADDGEAR_PAGE_CAPTURE_IMAGE_STARTED')
		capturePhoto();
	},
	captureSerial : function() {
		console.log('tapped capture serial')
		app.capturingSerial = true;
        window.plugins.FlurryPlugin.logEvent('ADDGEAR_PAGE_CAPTURE_SERIAL_STARTED')
		capturePhoto();
	}
});

bb.view.EditGearItemPage = Backbone.View.extend({
	events : {
		'click #saveinvbutton' : 'saveItem',
		'click #capturebutton' : 'capture',
		'click #captureserialbutton' : 'captureSerial',
		'tap #saveinvbutton' : 'saveItem',
		'tap #capturebutton' : 'capture',
		'tap #captureserialbutton' : 'captureSerial'
	},
	initialize : function() {
		this.template = _.template(tpl.get('editgearitem-page'));
		console.log('bb.view.AddGearItemPage - initialize')
		var self = this
		_.bindAll(self)
		self.items = tmgapp.model.items
	},

	render : function(eventName) {
		$(this.el).html(this.template(this.model.toJSON()));
		return this;
	}, // This toggles location for the current todo.
	saveItem : function() {
		var self = this
		_.bindAll(self)

		console.log('bb.view.AddGearItemPage - tap #save - starting...')

		self.elem = {
			itemnametext : self.$el.find('#itemmake'),
			itemmodeltext : self.$el.find('#itemmodel'),
			itemdesctext : self.$el.find('#itemdesc'),
			itemcat : self.$el.find('#itemcat'),
			itemserialtext : self.$el.find('#itemserial')
		}

		// Pull the item text out of the input box
		var iname = self.elem.itemnametext.val()
		var imodel = self.elem.itemmodeltext.val()
		var idesc = self.elem.itemdesctext.val()
		var iserial = self.elem.itemserialtext.val()
		var selectedCat = self.elem.itemcat[0].options[self.elem.itemcat[0].selectedIndex];
		///var iimg = app.currentCapturedImageData

		if(0 == iname.length || 0 == imodel.length || 0 == idesc.length) {
			console.log('bb.view.AddGearItemPage - nothing to save yet')
			return
		}

		// scrub the textfield and relinquish focus
		//self.elem.todotext.val('').blur()

		console.log('tap #save - adding item...')
		// Add the item to the master list
		self.items.additem(iname, imodel, idesc, selectedCat.text, app.currentCapturedImageData, iserial, app.currentCapturedSerialNoImageData)
		// Just reverse the previous actions
		console.log('tap #save - done!')
		window.history.back();
	},
	capture : function() {
		console.log('tapped capture')
		capturePhoto();
        app.capturingSerial=false;
	},
	captureSerial : function() {
		console.log('tapped capture serial')
		app.capturingSerial=true;
		capturePhoto();
	}
});
