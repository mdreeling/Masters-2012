bb.model.GearItem = Backbone.Model.extend({
	urlRoot : "/api/rest/inventory",
	initialize : function() {
		this.reports = new bb.model.GearItemCollection();
		this.reports.managerId = this.id;
	}
});

bb.model.GearItemCollection = Backbone.Collection.extend({
	url : '/api/rest/inventory',
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
	findByName : function(key) {
		var url = (key == '') ? 'http://www.trackmygearfor.me/api/rest/inventory' : "http://www.trackmygearfor.me/api/rest/inventory/search/" + key;
		console.log('findByName: ' + key);
		var self = this;
		$.ajax({
			url : '/api/rest/inventory',
			dataType : "json",
			success : function(data) {
				console.log("search success: " + data.length);
				self.reset(data);
			}
		});
	},
	// Adds an item (recording location if desired)
	additem : function(textIn, modelIn, descIn, categoryIn, imageIn) {

		console.log('bb.model.GearItemCollection - additem (' + textIn + ',' + descIn + ',' + categoryIn + ',' + imageIn + ')')

		var self = this

		var item = new bb.model.GearItem({
			name : textIn,
			model : modelIn,
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
});
