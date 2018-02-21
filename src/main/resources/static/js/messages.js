$(function () {

  $("#add_message_form").submit(function (e) {
    e.preventDefault();
    $.ajax({
      type: 'POST',
      url: '/api/messages/add',
      dataType: "json",
      //contentType: 'application/json',
      data: $("#add_message_form").serialize(),
      async: true,
      success: function (data) {
        console.log("dddd");
        window.location.reload();
      }
    });
  })
});

function setButtonStrip() {
  var urlParams = new URLSearchParams(window.location.search);

  var orderValue = urlParams.get('order');

  if(orderValue === 'asc') {
    $('#btn_order_asc').addClass('active');
    $('#btn_order_desc').removeClass('active')
  }
  else {
    $('#btn_order_asc').removeClass('active');
    $('#btn_order_desc').addClass('active')
  }

  var sortValue = urlParams.get('sort');

  if(sortValue === 'score') {
    $('#btn_sort_by_score').addClass('active');
    $('#btn_sort_by_date').removeClass('active')
  }
  else {
    $('#btn_sort_by_score').removeClass('active');
    $('#btn_sort_by_date').addClass('active')
  }

}

function getPathFromUrl(url) {
  return url.split("?")[0];
}


function clearTopic() {
  var urlSearch = window.location.search;
  var path = window.location.pathname.split("/")[1];
  window.location.href = window.location.protocol + "//" + window.location.host + "/" + path + urlSearch;
}

function clearHashtags() {
  var urlParams = new URLSearchParams(window.location.search);
  urlParams.delete('hashtag');
  var newUrl = window.location.protocol + "//" + window.location.host + window.location.pathname + '?' + urlParams.toString();
  window.location.href = newUrl;
}


$(function () {
  setButtonStrip();

  $("form").on('submit', function (e) {
    e.preventDefault();
  });
  $("#clear_topic").click(function () {
    clearTopic();
  });
  $("#clear_hashtags").click(function () {
    clearHashtags();
  });

  $("#btn_sort_by_score").click(function () {
    var urlParams = new URLSearchParams(window.location.search);
    urlParams.set('sort', 'score');
    window.location.search = urlParams;
  });

  $("#btn_sort_by_date").click(function () {
    var urlParams = new URLSearchParams(window.location.search);
    urlParams.set('sort', 'date');
    window.location.search = urlParams;
  });

  $("#btn_order_asc").click(function () {
    var urlParams = new URLSearchParams(window.location.search);
    urlParams.set('order', 'asc');
    window.location.search = urlParams;
  });

  $("#btn_order_desc").click(function () {
    var urlParams = new URLSearchParams(window.location.search);
    urlParams.set('order', 'desc');
    window.location.search = urlParams;
  });

  $('a[data-toggle="tab"]').on('shown.bs.tab', function(e){
    var target = $(e.target).attr("href") // activated tab
    alert(target);
  });

  $("#set_topic_modal_button").click(function () {
    var topic = $("#set_topic_modal_topictext").val();
    var urlSearch = window.location.search;
    var path = window.location.pathname.split("/")[1];
    window.location.href = window.location.protocol + "//" + window.location.host + "/" + path  + "/" + topic + urlSearch;

    $('#modalwindow').modal('hide');
  });
});