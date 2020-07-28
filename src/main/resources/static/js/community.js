

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
                        window.open("https://github.com/login/oauth/authorize?client_id=a3d69bb3ea4d7ac6eae4&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        // window.open("https://github.com/login/oauth/authorize?client_id=a4acac6694521f1c22a6&redirect_uri=http://106.75.29.98/callback&scope=user&state=1");
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
        comments.addClass("in");
        e.setAttribute("data-collapse","in");
        e.classList.add("active");
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
    var flag = $("#add-tag").is(":hidden");
    if (flag){
        $("#add-tag").show();
    }else {
        $("#add-tag").hide();
    }
}

function saveQuestion() {

    var id = $("#id").val();
    var title = $("#title").val();
    var description = $("#description").val();
    var tag = $("#tag").val();
    if (null == title || "" == title){
        alert("标题不能为空！");
        return;
    }
    if (null == description || "" == description){
        alert("内容不能为空！");
        return;
    }
    if (null == tag || "" == tag){
        alert("标签不能为空！");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/publish",
        contentType:'application/json',
        data: JSON.stringify({
            "id":id,
            "title":title,
            "description":description,
            "tag":tag
        }),
        success: function (response){
            if(response.code == 200){
                window.location.href="/question/"+ id;
            }else {
                alert(response.message);
            }
        },
        dataType: "json"
    })

}

function deleteQuestion(e) {

    var id = e.id;
    $.ajax({
        type: "POST",
        url: "/question/delete/",
        contentType:'application/json',
        data: JSON.stringify(id),
        success: function (response){
            if(response.code == 200){
                window.location.reload();
            }else {
                alert(response.message);
            }
        },
        dataType: "json"
    })

}

function deleteComment(e) {

    var id = e.id;
    $.ajax({
        type: "POST",
        url: "/comment/delete/",
        contentType:'application/json',
        data: JSON.stringify(id),
        success: function (response){
            if(response.code == 200){
                window.location.reload();
            }else {
                alert(response.message);
            }
        },
        dataType: "json"
    })

}

function starQuestion(e) {
    var status = e.getAttribute("like");
    var parentId = $("#question-id").val();
    var likeCount = document.getElementById("question-like-count").innerText;
    if (status == 1){
        $.ajax({
            type: "POST",
            url: "/like/deleteLike/",
            contentType:'application/json',
            data: JSON.stringify({
                "parentId":parentId,
                "type":1
            }),
            success: function (response){
                if(response.code == 200){
                    e.setAttribute("class","glyphicon glyphicon-star-empty like-count comment-right")
                    e.setAttribute("like",2);
                    likeCount = likeCount - 1;
                    document.getElementById("question-like-count").innerText = likeCount;
                }else {
                    alert(response.message);
                }
            },
            dataType: "json"
        });
    }
    if (status == 2){
        $.ajax({
            type: "POST",
            url: "/like/addLike/",
            contentType:'application/json',
            data: JSON.stringify({
                "parentId":parentId,
                "type":1
            }),
            success: function (response){
                if(response.code == 200){
                    e.setAttribute("class","glyphicon glyphicon-star-empty like-count comment-right active");
                    likeCount = likeCount - (-1);
                    document.getElementById("question-like-count").innerText = likeCount;
                    e.setAttribute("like",1);
                }else {
                    alert(response.message);
                }
            },
            dataType: "json"
        })
    }
}

function commentLike(e) {

    var status = e.getAttribute("like");
    var parentId = e.id;
    var likeCount = e.innerText;
    if (status == 1){
        $.ajax({
            type: "POST",
            url: "/like/deleteLike/",
            contentType:'application/json',
            data: JSON.stringify({
                "parentId":parentId,
                "type":2
            }),
            success: function (response){
                if(response.code == 200){
                    e.setAttribute("class","glyphicon glyphicon-thumbs-up like-count")
                    e.setAttribute("like",2);
                    likeCount = likeCount - 1;
                    e.innerText = likeCount;
                }else {
                    alert(response.message);
                }
            },
            dataType: "json"
        });
    }
    if (status == 2){
        $.ajax({
            type: "POST",
            url: "/like/addLike/",
            contentType:'application/json',
            data: JSON.stringify({
                "parentId":parentId,
                "type":2
            }),
            success: function (response){
                if(response.code == 200){
                    e.setAttribute("class","glyphicon glyphicon-thumbs-up like-count active");
                    likeCount = likeCount - (-1);
                    e.innerText = likeCount;
                    e.setAttribute("like",1);
                }else {
                    alert(response.message);
                }
            },
            dataType: "json"
        })
    }

}