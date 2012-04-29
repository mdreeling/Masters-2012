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

bb.view.DirectReportPage = Backbone.View.extend({

	initialize : function() {
		this.template = _.template(tpl.get('report-page'));
	},

	render : function(eventName) {
		$(this.el).html(this.template(this.model.toJSON()));
		this.listView = new bb.view.GearItemListView({
			el : $('ul', this.el),
			model : this.model
		});
		this.listView.render();
		return this;
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

	initialize : function() {
		this.template = _.template(tpl.get('gearitem-page'));
	},

	render : function(eventName) {
		$(this.el).html(this.template(this.model.toJSON()));
		return this;
	}
});

bb.view.AddGearItemPage = Backbone.View.extend({
	events : {
		'click #saveinvbutton' : 'saveItem',
		'click #capturebutton' : 'capture',
		'tap #saveinvbutton' : 'saveItem',
		'tap #capturebutton' : 'capture',
	},
	initialize : function() {
		this.template = _.template(tpl.get('addgearitem-page'));
		console.log('bb.view.AddGearItemPage - initialize')
		var self = this
		_.bindAll(self)
		self.items = tmgapp.model.items
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
			itemimagedata : self.$el.find('#itemimage')
		}

		// Pull the item text out of the input box
		var iname = self.elem.itemnametext.val()
		var imodel = self.elem.itemmodeltext.val()
		var idesc = self.elem.itemdesctext.val()
		var selectedCat = self.elem.itemcat[0].options[self.elem.itemcat[0].selectedIndex];
		///var iimg = app.currentCapturedImageData

		if(0 == iname.length) {
			console.log('bb.view.AddGearItemPage - noting to save yet')
			return
		}

		// scrub the textfield and relinquish focus
		//self.elem.todotext.val('').blur()

		console.log('tap #save - adding item...')
		// Add the item to the master list
		self.items.additem(iname, imodel, idesc, selectedCat.text, app.currentCapturedImageData)
		// Just reverse the previous actions
		console.log('tap #save - done!')
		window.history.back();
	},
	capture : function() {
		console.log('tapped capture')
		capturePhoto();
	}
});
