window.Employee = Backbone.Model.extend({
	 urlRoot:"/api/rest/inventory",
    initialize:function () {
        this.reports = new EmployeeCollection();
        this.reports.managerId = this.id;
    }

});

window.EmployeeCollection = Backbone.Collection.extend({
	url : '/api/rest/inventory',
    model:Employee,

    findByName:function (key) {
        var url = (key == '') ? 'http://www.trackmygearfor.me/api/rest/inventory' : "http://www.trackmygearfor.me/api/rest/inventory/search/" + key;
        console.log('findByName: ' + key);
        var self = this;
        $.ajax({
            url:'/api/rest/inventory',
            dataType:"json",
            success:function (data) {
                console.log("search success: " + data.length);
                self.reset(data);
            }
        });
    }

});