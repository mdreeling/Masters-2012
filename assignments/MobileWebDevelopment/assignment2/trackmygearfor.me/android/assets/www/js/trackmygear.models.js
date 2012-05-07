bb.model.GearItem = Backbone.Model.extend({
	urlRoot : modelurl,
	initialize : function() {
		this.reports = new bb.model.GearItemCollection();
		this.reports.managerId = this.id;
	}
});

bb.model.GearItemCollection = Backbone.Collection.extend({
	url : modelurl,
	model : bb.model.GearItem,
	initialize : function() {
		console.log('bb.model.GearItemCollection - initialize')
		var self = this
		_.bindAll(self)
		self.count = 0

		self.on('reset', function() {
			console.log('bb.model.GearItemCollection - reset')
			self.count = self.length
			// Re-render after a refresh - (required for groupings to work)
			//app.view.list.render();
		})
	},
	findByNameAndUser : function(key, user) {
		var url = (key == '') ? modelurl + '/search/' + user : modelurl + '/search/' + user + '/' + key;
		console.log('findByNameAndUser: ' + key + ', ' + user+' ('+url+')');
		var self = this;
		$.ajax({
			url : url,
			dataType : "json",
			success : function(data) {
				console.log("search success: " + data.length);
				self.reset(data);
			}
		});
	},
	// Adds an item (recording location if desired)
	additem : function(textIn, modelIn, descIn, categoryIn, imageIn, serialIn, serialImageDataIn) {

		console.log('bb.model.GearItemCollection - additem (' + textIn + ',' + descIn + ',' + categoryIn + ',' + imageIn + ')')

		var self = this

		var item = new bb.model.GearItem({
			name : textIn,
			model : modelIn,
			description : descIn,
			category : categoryIn,
			imagedata : imageIn,
			username : app.loggedinusername,
			serialnumber : serialIn,
			serialimagedata : serialImageDataIn
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
});

bb.model.State = Backbone.Model.extend({
	initialize : function(items) {
		var self = this
		_.bindAll(self)
	},
	init : function() {
		var self = this
	}
})
