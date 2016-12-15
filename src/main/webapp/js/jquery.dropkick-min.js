!
function(t, e, i) {
	"use strict";
	var n = navigator.userAgent.match(/MSIE ([0-9]{1,}[\.0-9]{0,})/),
	a = !!n,
	r = a && parseFloat(n[1]) < 7,
	d = navigator.userAgent.match(/iPad|iPhone|Android|IEMobile|BlackBerry/i),
	o = a ? "mousedown": "click",
	l = "onwheel" in e ? "wheel": "onmousewheel" in i ? "mousewheel": "MouseScrollEvent" in e && "DOMMouseScroll MozMousePixelScroll",
	s = {
		fired: !1,
		fire: function() {
			t(i).on(o, ".dk_options a",
			function() {
				var e = t(this),
				i = e.parent(),
				n = e.parents(".dk_container").first();
				return i.hasClass("disabled") || i.closest(".dk_optgroup", n).hasClass("disabled") || (i.hasClass("dk_option_current") || (b(e, n), $(e.parent(), n)), m(n)),
				!1
			}).on(l, ".dk_options_inner",
			function(t) {
				var e = t.originalEvent.wheelDelta || -t.originalEvent.deltaY || -t.originalEvent.detail;
				return a ? (this.scrollTop -= Math.round(e / 10), !1) : e > 0 && this.scrollTop <= 0 || 0 > e && this.scrollTop >= this.scrollHeight - this.offsetHeight ? !1 : !0
			}).on({
				"keydown.dk_nav": function(t) {
					var e = v || _;
					e && A(t, e)
				},
				click: function(e) {
					var i, n = t(e.target);
					if (v && 0 === n.closest(".dk_container").length) m(v);
					else {
						if (n.is(".dk_toggle, .dk_label")) return i = n.parents(".dk_container").first(),
						i.hasClass("dk_open") ? m(i) : (v && m(v), !i.attr("disabled") && x(i, e)),
						!1;
						n.attr("for") && t("#dk_container_" + n.attr("for"))[0] && t("#dk_container_" + n.attr("for")).trigger("focus.dropkick")
					}
				}
			}),
			this.fired = !0
		}
	},
	p = {},
	c = [],
	f = {
		left: 37,
		up: 38,
		right: 39,
		down: 40,
		enter: 13,
		tab: 9,
		zero: 48,
		z: 90,
		last: 221
	},
	h = ['<div class="dk_container" id="dk_container_{{ id }}" tabindex="{{ tabindex }}" aria-hidden="true">', '<a class="dk_toggle dk_label">{{ label }}</a>', '<div class="dk_options">', '<ul class="dk_options_inner" role="main" aria-hidden="true">', "</ul>", "</div>", "</div>"].join(""),
	u = '<li class="{{ current }}{{ disabled }}"><a data-dk-dropdown-value="{{ value }}">{{ text }}</a></li>',
	k = '<li class="dk_optgroup{{ disabled }}"><span>{{ text }}</span>',
	g = {
		startSpeed: 400,
		theme: !1,
		changes: !1,
		syncReverse: !0,
		nativeMobile: !0,
		autoWidth: !0
	},
	v = null,
	_ = null,
	b = function(t, e, i) {
		var n = e.data("dropkick"),
		a = n.$select,
		r = t.length ? t: n.$original,
		d = r.attr("data-dk-dropdown-value") || t.attr("value"),
		o = r.text();
		e.find(".dk_label").html(o ? o: "&nbsp;"),
		i ? a.val(d) : a.val(d).trigger("change"),
		n.settings.change && !i && !n.settings.syncReverse && n.settings.change.call(a, d, o)
	},
	m = function(t) {
		t.removeClass("dk_open dk_open_top"),
		v = null
	},
	C = function(i) {
		if (i.data("dropkick").settings.fixedMove) return "up" == i.data("dropkick").settings.fixedMove ? !1 : !0;
		var n = i.find(".dk_toggle"),
		a = i.find(".dk_options").outerHeight(),
		r = t(e).height() - n.outerHeight() - n.offset().top + t(e).scrollTop(),
		d = n.offset().top - t(e).scrollTop();
		return d > a ? r > a: !0
	},
	w = function(t, e, i) {
		var n = t.find(".dk_options_inner"),
		a = e.prevAll("li").outerHeight() * e.prevAll("li").length + (e.closest(".dk_optgroup", n).length && e.closest(".dk_optgroup", n).prevAll("li").outerHeight() * e.closest(".dk_optgroup", n).prevAll("li").length),
		r = n.scrollTop(),
		d = n.height() + n.scrollTop() - e.outerHeight(); (i && "keydown" === i.type || r > a || a > d) && n.scrollTop(a)
	},
	x = function(t, e) {
		var i = C(t),
		n = "dk_open" + (i ? "": " dk_open_top");
		t.find(".dk_options").css({
			top: i ? t.find(".dk_toggle").outerHeight() - 1 : "",
			bottom: i ? "": t.find(".dk_toggle").outerHeight() - 1
		}),
		v = t.addClass(n),
		w(t, t.find(".dk_option_current"), e)
	},
	$ = function(t, e, i) {
		e.find(".dk_option_current").removeClass("dk_option_current"),
		t.addClass("dk_option_current"),
		w(e, t, i)
	},
	A = function(e, i) {
		var n, a, r, d, o, l, s, p, c, h = e.keyCode,
		u = i.data("dropkick"),
		k = String.fromCharCode(h),
		g = i.find(".dk_options"),
		v = i.hasClass("dk_open"),
		_ = g.find("li:not(.disabled)"),
		C = i.find(".dk_option_current"),
		w = C.closest(".dk_optgroup", g),
		A = _.first().hasClass("dk_optgroup") ? _.first().find("li:not(.disabled)").first() : _.first(),
		y = _.last().hasClass("dk_optgroup") ? _.last().find("li:not(.disabled)").last() : _.last(),
		T = [];
		switch (h) {
		case f.enter:
			v ? C.hasClass("disabled") || (b(C.find("a"), i), m(i)) : x(i, e),
			e.preventDefault();
			break;
		case f.tab:
			v && (C.length && b(C.find("a"), i), m(i));
			break;
		case f.up:
			v ? (d = C.prevAll("li:not(.disabled)").first(), d.hasClass("dk_optgroup") && (d = d.find("li:not(.disabled)").last()), !d.length && w.length && (d = w.prevAll("li:not(.disabled)").first().hasClass("dk_optgroup") ? w.prevAll("li:not(.disabled)").first().find("li:not(.disabled)").last() : w.prevAll("li:not(.disabled)").first()), $(d.length ? d: y, i, e)) : x(i, e),
			e.preventDefault();
			break;
		case f.down:
			v ? (r = C.nextAll("li:not(.disabled)").first(), r.hasClass("dk_optgroup") && (r = r.find("li:not(.disabled)").first()), !r.length && w.length && (r = w.nextAll("li:not(.disabled)").first().hasClass("dk_optgroup") ? w.nextAll("li:not(.disabled)").first().find("li:not(.disabled)").first() : w.nextAll("li:not(.disabled)").first()), $(r.length ? r: A, i, e)) : x(i, e),
			e.preventDefault()
		}
		if (h >= f.zero && h <= f.z) {
			for (o = (new Date).getTime(), null === u.finder || void 0 === u.finder ? (u.finder = k.toUpperCase(), u.timer = o) : o > parseInt(u.timer, 10) + 1e3 ? (u.finder = k.toUpperCase(), u.timer = o) : (u.finder = u.finder + k.toUpperCase(), u.timer = o), l = _.find("a"), n = t.unique(u.finder.split("")), s = 0, p = l.length; p > s; s++) if (c = t(l[s]), 1 === n.length && c.text()[0].toUpperCase() === n[0] && T.push(l[s]), 0 === c.html().toUpperCase().indexOf(u.finder) && !c.closest(".dk_optgroup", g).hasClass("disabled") && u.finder.length > 1) {
				b(c, i),
				$(c.parent(), i, e);
				break
			} (u.finder.length > 1 && T.length > 1 && 1 === n.length || 1 === u.finder.length) && (a = t.inArray(C.find("a")[0], T), c = t(T).filter(function(t, e) {
				return e !== C[0] && t > a
			}), c = 0 === c.length ? t(T[0]) : c, b(c, i), $(c.parent(), i, e)),
			i.data("dropkick", u)
		}
	},
	y = function(e) {
		return t.trim(e).length > 0 ? e: !1
	},
	T = function(e, i) {
		var n, a, r, d, o, l = function(e) {
			var n = e.val(),
			a = e.html(),
			r = void 0 !== e.attr("disabled");
			return u.replace("{{ value }}", n).replace("{{ current }}", y(n) !== i.value || r ? "": "dk_option_current").replace("{{ disabled }}", r ? "disabled": "").replace("{{ text }}", t.trim(a) ? t.trim(a) : "&nbsp;")
		},
		s = e.replace("{{ id }}", i.id).replace("{{ label }}", i.label).replace("{{ tabindex }}", i.tabindex),
		p = [];
		if (i.options && i.options.length) for (a = 0, r = i.options.length; r > a; a++) {
			if (d = t(i.options[a]), d.is("option")) o = 0 === a && void 0 !== d.attr("selected") && void 0 !== d.attr("disabled") ? null: l(d);
			else if (d.is("optgroup")) {
				if (o = k.replace("{{ text }}", d.attr("label") || "---").replace("{{ disabled }}", void 0 !== d.attr("disabled") ? " disabled": ""), t(i.options[a]).children().length) {
					o += "<ul>";
					for (var c = 0,
					f = t(i.options[a]).children().length; f > c; c++) {
						var h = t(i.options[a]).children().eq(c);
						o += l(h)
					}
					o += "</ul>"
				}
				o += "</li>"
			}
			p[p.length] = o
		}
		return n = t(s),
		n.find(".dk_options_inner").html(p.join("")),
		n
	};
	p.init = function(e) {
		return e = t.extend({},
		g, e),
		h = e.dropdownTemplate ? e.dropdownTemplate: h,
		u = e.optionTemplate ? e.optionTemplate: u,
		!s.fired && s.fire(),
		this.each(function() {
			var i, n = t(this),
			a = n.find(":selected").first(),
			r = n.children(),
			o = n.data("dropkick") || {},
			l = n.attr("id") || n.attr("name"),
			s = n.parent(".dk_wrap"),
			p = e.width || s.length ? s.width() - (n.innerWidth() - n.width()) : n.outerWidth(),
			f = n.attr("tabindex") || "0",
			u = !!n.attr("disabled"),
			k = !1;
			return o.id ? n: (o.settings = e, o.tabindex = f, o.id = l, o.$original = a, o.$select = n, o.value = y(n.val()) || y(a.attr("value")), o.label = a.text() ? a.text() : "&nbsp;", o.options = r, o.theme = e.theme || "default", k = T(h, o), o.settings.autoWidth && k.find(".dk_toggle").css({
				//width: p + "px"
			}), u && k.attr({
				disabled: "disabled",
				tabindex: -1
			}), n.before(k).appendTo(k.addClass("dk_theme_" + o.theme)), s.length ? (s.removeClass("dk_wrap"), k.show()) : k.fadeIn(e.startSpeed), o.$dk = k, n.data("dropkick", o), k.addClass(n.attr("class")), k.data("dropkick", o), c[c.length] = n, k.on({
				"focus.dropkick": function() {
					_ = !k.attr("disabled") && k.addClass("dk_focus")
				},
				"blur.dropkick": function() {
					k.removeClass("dk_focus"),
					_ = null
				}
			}), d && o.settings.nativeMobile && k.addClass("dk_mobile"), o.settings.syncReverse && n.on("change",
			function(e) {
				var i = n.val(),
				a = t('a[data-dk-dropdown-value="' + i + '"]', k),
				r = a.text();
				k.find(".dk_label").html(r ? r: "&nbsp;"),
				$(a.parent(), k, e),
				o.settings.change && o.settings.change.call(n, i, r)
			}), i = n.attr("form") ? t("#" + n.attr("form").replace(" ", ", #")) : n.closest("form"), void(i.length && i.on("reset",
			function() {
				n.dropkick("reset")
			})))
		})
	},
	p.theme = function(e) {
		var i = t(this).data("dropkick"),
		n = i.$dk,
		a = "dk_theme_" + i.theme;
		i.theme = e || i.theme,
		n.removeClass(a).addClass("dk_theme_" + e)
	},
	p.reset = function(e) {
		return this.each(function() {
			var i = t(this).data("dropkick"),
			n = i.$dk,
			a = t('a[data-dk-dropdown-value="' + i.$original.attr("value") + '"]', n);
			i.$original.prop("selected", !0),
			$(a.parent(), n),
			b(a, n, !e)
		})
	},
	p.setValue = function(e) {
		return this.each(function() {
			var i = t(this).data("dropkick").$dk,
			n = t('.dk_options a[data-dk-dropdown-value="' + e + '"]', i);
			n.length && b(n, i) | $(n.parent(), i)
		})
	},
	p.refresh = function(e) {
		return this.each(function() {
			var i, n, a = t(this).data("dropkick"),
			r = a.$select,
			d = a.$dk;
			a.options = r.children(),
			n = T(h, a).find(".dk_options_inner"),
			d.find(".dk_options_inner").replaceWith(n),
			a.$original.parent().length || (a.$original = r.find(":selected").first(), a.label = a.$original.text()),
			i = t('a[data-dk-dropdown-value="' + r.val() + '"]', d),
			$(i.parent(), d),
			b(i, d, !e)
		})
	},
	p.destroy = function() {
		return this.each(function() {
			var e = t(this).data("dropkick");
			e.$dk.before(function() {
				return e.$select.removeData("dropkick")
			}).remove()
		})
	},
	p.clone = function(e, i, n) {
		var a = [];
		t.each(this,
		function(r) {
			var d = t(this).data("dropkick"),
			o = d.settings,
			l = d.$select.clone();
			o.autoWidth && (o.width = d.$dk.find(".dk_label").width()),
			i && l.attr({
				id: i
			}),
			n && l.attr({
				name: n
			}),
			l.removeData("dropkick"),
			e === !1 ? a[r] = l[0] : (l.dropkick(o), a[r] = l.data("dropkick").$dk[0])
		})
	},
	p.disable = function(e) {
		return this.each(function() {
			var i = t(this).data("dropkick"),
			n = i.$select,
			a = i.$dk;
			e === !1 ? (n.removeAttr("disabled"), a.removeAttr("disabled").attr({
				tabindex: i.tabindex
			})) : (a.hasClass("dk_open") && m(a), n.attr({
				disabled: "disabled"
			}), a.attr({
				disabled: "disabled",
				tabindex: -1
			}))
		})
	},
	t.fn.dropkick = function(t) {
		if (!r) {
			if (p[t]) return p[t].apply(this, Array.prototype.slice.call(arguments, 1));
			if ("object" == typeof t || !t) return p.init.apply(this, arguments)
		}
	}
} (jQuery, window, document);