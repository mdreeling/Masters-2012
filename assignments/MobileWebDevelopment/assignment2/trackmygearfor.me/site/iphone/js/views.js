window.SearchPage = Backbone.View.extend({

    initialize:function () {
        this.template = _.template(tpl.get('search-page'));
    },

    render:function (eventName) {
        $(this.el).html(this.template(this.model.toJSON()));
        this.listView = new GearItemListView({el: $('ul', this.el), model: this.model});
        this.listView.render();
        return this;
    },

    events:{
        "keyup .search-key":"search"
    },

    search:function (event) {
        var key = $('.search-key').val();
        this.model.findByName(key);
    }
});

window.DirectReportPage = Backbone.View.extend({

    initialize:function () {
        this.template = _.template(tpl.get('report-page'));
    },

    render:function (eventName) {
        $(this.el).html(this.template(this.model.toJSON()));
        this.listView = new GearItemListView({el: $('ul', this.el), model: this.model});
        this.listView.render();
        return this;
    }

});

window.GearItemListView = Backbone.View.extend({

    initialize:function () {
        this.model.bind("reset", this.render, this);
    },

    render:function (eventName) {
        $(this.el).empty();
        _.each(this.model.models, function (gearitem) {
            console.log('rendering ' + gearitem);
            $(this.el).append(new GearItemListItemView({model:gearitem}).render().el);
        }, this);
        return this;
    }

});

window.GearItemListItemView = Backbone.View.extend({

    tagName:"li",

    initialize:function () {
        this.template = _.template(tpl.get('gearitem-list-item'));
    },

    render:function (eventName) {
        $(this.el).html(this.template(this.model.toJSON()));
        return this;
    }

});

window.GearItemPage = Backbone.View.extend({

    initialize:function () {
        this.template = _.template(tpl.get('gearitem-page'));
    },

    render:function (eventName) {
        $(this.el).html(this.template(this.model.toJSON()));
        return this;
    }

});