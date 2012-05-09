$(document).ready(function() {

  $(window).scroll(function () {
    var inview = '#' + $("h3:in-viewport:first").parent().attr('id'),
        $link = $('#nav-02>li.current>ul>li>span>a').filter('[hash=' + inview + ']');

    if ($link.length && !$link.is('.current')) {
      $('#nav-02>li.current>ul>li').removeClass('current');
      $link.parent().parent().addClass('current');
    }
  });

  $('#nav-02 a[href*=#]').bind('click', function(e) {
    e.preventDefault();
    var target = $(this).attr("href");
    $('html, body').stop().animate({ scrollTop: $(target).offset().top }, 500, function() {
      location.hash = target;
    });
    return false;
  });
 
  var top = $('#nav-02').offset().top - parseFloat($('#nav-02').css('marginTop').replace(/auto/, 0));
  $(window).scroll(function (event) {
    // what the y position of the scroll is
    var y = $(this).scrollTop();
    var z = $("#nav-02").hasClass("disableStick");
  
    // whether that's below the form
    if (y >= top && z == false) {
      // if so, add the fixed class
      $('#nav-02').addClass('fixed');
    } else {
      // otherwise remove it
      $('#nav-02').removeClass('fixed');
    }
  });

  $("div.apiSectionCode.XML").hide();
  $(".switchContainer a.toggleSwitch.JSON").addClass("active");
  $("div.apiSectionCode.JSON").show();

  $(".toggleSwitch").click(function (e) {
    e.preventDefault();
    var x = $(this).hasClass("JSON");
    var y = $(this).hasClass("XML");
      
    if (x) {
      $(".toggleSwitch.XML").removeClass("active");
      $(".toggleSwitch.JSON").addClass("active");
      $("div.apiSectionCode.JSON").fadeIn();
      $("div.apiSectionCode.XML").hide();
    }
  
    if (y) {
      $(".toggleSwitch.JSON").removeClass("active");
      $(".toggleSwitch.XML").addClass("active");
      $("div.apiSectionCode.XML").fadeIn();
      $("div.apiSectionCode.JSON").hide();
    }
  });
});