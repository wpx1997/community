
function question_comment() {
    var questionId = $("#question-id").val();
    var content = $("#question-comment").val();
    commenttarget(questionId,1,content);
}

function comment_comment(e) {
    var commentId = $("#comment_id").val();
    var content = $("#comment_comment").val();
    commenttarget(commentId,2,content);
}

function commenttarget(targetId,type,content) {

    if (!content){
        alert("不能回复空内容！")
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType:'application/json',
        data: JSON.stringify({
            "parentId":targetId,
            "content":content,
            "type":type
        }),
        success: function (response) {
            if (response.code == 200){
                window.location.reload();
            }else {
                if (response.code == 2003){
                    var isAccepted = confirm(response.message);
                    if (isAccepted){
                        // window.open("https://github.com/login/oauth/authorize?client_id=a3d69bb3ea4d7ac6eae4&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.open("https://github.com/login/oauth/authorize?client_id=a4acac6694521f1c22a6&redirect_uri=http://106.75.29.98/callback&scope=user&state=1");
                        window.localStorage.setItem("closable",true);
                    }
                }else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}

/*
* 展开和关闭二级评论
*/

function collapseComments(e) {

    var id = e.getAttribute("data-id");
    var comments = $("#comment-"+id);
    var collapse = e.getAttribute("data-collapse");

    if (collapse){
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    }else {

        var subCommentContainer = $("#comment-"+id);

        if (subCommentContainer.children().length != 1){
            comments.addClass("in");
            e.setAttribute("data-collapse","in");
            e.classList.add("active");
        }else {
            $.getJSON("/comment/"+id,function (data) {
                $.each(data.data.reverse(),function (index,comment) {
                    var mediaLeftElement = $("<div/>",{
                        "class":"media-left"
                    }).append($("<img/>",{
                        "class":"user-avatar img-rounded",
                        "src":comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>",{
                        "class":"media-body"
                    }).append($("<a/>",{
                        "href":'/message/'+comment.user.id,
                        "html":comment.user.name
                    })).append($("<div/>",{
                        "class":"media-heading comment-body",
                        "html":comment.content
                    })).append($("<div/>",{
                        "class":"comment-menu"
                    }).append($("<span/>",{
                        "class":"pull-right",
                        "html":moment(comment.gmtCreate).format('YYYY-MM-DD HH:mm')
                    })));

                    var mediaElement = $("<div/>",{
                        "class":"media"
                    }).append(mediaLeftElement)
                        .append(mediaBodyElement);

                    var commentElement = $("<div/>",{
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
            });

            comments.addClass("in");
            e.setAttribute("data-collapse","in");
            e.classList.add("active");
        }

    }

}

function addTag(e) {
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    if (previous.indexOf(value) == -1){
        if (previous){
            $("#tag").val(previous+"，"+value);
        }else {
            $("#tag").val(value);
        }
    }
}

function showAddTag() {
    $("#add-tag").show();
}

