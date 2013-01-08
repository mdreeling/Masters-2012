define("app/data/frontpage_scribe", ["module", "require", "exports", "core/component", "app/data/with_scribe"], function(module, require, exports) {
	function frontpageScribe() {
		this.frontpageScribes = {
			signin : {
				component : "login_callout",
				element : "form",
				action : "login_click"
			},
			signup : {
				component : "signup_callout",
				element : "form",
				action : "signup_click"
			},
			search : {
				component : "main",
				element : "search_field",
				action : "search"
			},
			language : {
				component : "footer",
				element : "language_selector",
				action : "select"
			}
		}, this.scribeFrontpageForm = function(a, b) {
			var c = a.target.className.match(/signin|signup|language|search/);
			c.length && this.scribe(this.frontpageScribes[c[0]])
		}, this.scribeFrontpageLink = function(a, b) {
			var c = a.target.className;
			c == "signup-welcome-link" ? this.scribe({
				component : "lifeline",
				element : "welcome_text",
				action : "signup_click"
			}) : c.indexOf("app-store") != -1 ? this.scribe({
				component : "mobile_callout",
				element : "app_store_button",
				action : "click"
			}) : c.indexOf("google-play") != -1 && this.scribe({
				component : "mobile_callout",
				element : "google_play_button",
				action : "click"
			})
		}, this.after("initialize", function() {
			this.on("submit", this.scribeFrontpageForm), this.on("click", this.scribeFrontpageLink)
		})
	}

	var defineComponent = require("core/component"), withScribe = require("app/data/with_scribe");
	module.exports = defineComponent(frontpageScribe, withScribe)
});
define("app/ui/cookie_warning", ["module", "require", "exports", "core/component"], function(module, require, exports) {
	function cookieWarning() {
		this.cookiesEnabled = function() {
			var a = !!navigator.cookieEnabled;
			if ( typeof navigator.cookieEnabled == "undefined" || $.browser.msie)
				document.cookie = "cookies_enabled", a = document.cookie.indexOf("cookies_enabled") != -1;
			return a
		}, this.showWarning = function() {
			this.cookiesEnabled() || this.$node.show("fast")
		}, this.after("initialize", function() {
			this.on(document, "uiSwiftLoaded", this.showWarning)
		})
	}

	var component = require("core/component");
	module.exports = component(cookieWarning)
});
define("app/pages/frontpage", ["module", "require", "exports", "app/boot/common", "app/boot/logged_out", "app/ui/forms/input_with_placeholder", "app/data/frontpage_scribe", "app/ui/cookie_warning"], function(module, require, exports) {
	var bootCommon = require("app/boot/common"), bootLoggedOut = require("app/boot/logged_out"), InputWithPlaceholder = require("app/ui/forms/input_with_placeholder"), FrontpageScribe = require("app/data/frontpage_scribe"), CookieWarning = require("app/ui/cookie_warning");
	module.exports = function(a) {
		bootCommon(a), bootLoggedOut(a), InputWithPlaceholder.attachTo(".placeholding-input", {
			hidePlaceholderClassName : "hasome",
			placeholder : ".placeholder",
			input : "input"
		}), $('.front-signin input[name="session[username_or_email]"]').focus(), FrontpageScribe.attachTo(document, a), document.activeElement.tagName.toLowerCase() != "input" && $(".front-signin .email-input").focus(), CookieWarning.attachTo("#front-no-cookies-warn")
	}
});
define("app/pages/login", ["module", "require", "exports", "app/boot/common", "app/ui/forms/input_with_placeholder", "core/i18n"], function(module, require, exports) {
	var boot = require("app/boot/common"), InputWithPlaceholder = require("app/ui/forms/input_with_placeholder"), _ = require("core/i18n");
	module.exports = function(a) {
		boot(a), InputWithPlaceholder.attachTo(".signin-wrapper form .holding"), $(".signin-wrapper").data("login-message") && $(document).trigger("uiShowMessage", {
			message : _('You must log in to do that')
		}), $(".js-username-field").focus()
	}
}); 