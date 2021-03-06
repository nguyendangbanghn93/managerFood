<%@ page import="java.util.ArrayList" %>
<%@ page import="entity.Food" %>
<%@ page import="helper.ConvertHelper" %>
<%@ page import="entity.Category" %><%--
  Created by IntelliJ IDEA.
  User: luuhu
  Date: 4/23/2021
  Time: 7:57 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%

    ArrayList<Food> foods = (ArrayList<Food>) request.getAttribute("foods");
    ArrayList<Category> categories = (ArrayList<Category>) request.getAttribute("categories");
    int numPage = (int) request.getAttribute("numPage");

%>
<html>
<head>
    <title>List Student</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

</head>
<body>
<div class="jumbotron text-center"
     style="background-image: url('https://www.noithatjhome.com/wp-content/uploads/2019/07/thiet-ke-nha-hang-1.jpg'); background-size: cover; background-position: center; background-repeat: no-repeat">
    <div style="display: inline-block; text-align: center">
        <h1 style="background: #f0f8ffa8; padding: 20px 80px">MY RESTAURENT</h1>
    </div>
</div>

<nav class="navbar navbar-expand-sm bg-dark navbar-dark">
    <a class="navbar-brand" href="/">Home</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="collapsibleNavbar">
        <ul class="navbar-nav">
            <li class="nav-item">
                <button type="button" style="border: none; background: none; color: white" id="createFood">
                    Create
                </button>
            </li>
        </ul>
    </div>
</nav>
<!-- Button to Open the Modal -->


<div style="padding: 20px">
    <div class="text-center">
        <a href="/"><img style="width: 400px; height: 200px" src="http://res.cloudinary.com/nguyendangbang/image/upload/v1621745643/img/v4r9a0rkns1nvfkttuaj.jpg" alt=""></a>
    </div>
    <h2 class="text-center" style="padding: 50px">Danh s??ch m??n ??n</h2>
    <ul class="pagination MyPagination" style="justify-content: center">
        <%
            for (int i = 1; i <= numPage; i++) {
                int p = Integer.parseInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));
                String active = p == i ? "active" : "";
        %>
        <li class="page-item <%=active%>" page="<%=i%>"><a class="page-link" href="?page=<%=i%>&num=5"><%=i%>
        </a></li>
        <%}%>
    </ul>
    <table class="table table-hover">
        <thead class=" table-dark   ">
        <tr class="text-center">
            <th scope="col">M?? m??n ??n</th>
            <th scope="col">T??n m??n ??n</th>
            <th scope="col">Danh m???c m??n ??n</th>
            <th scope="col">M?? t???</th>
            <th scope="col">???nh ?????i di???n</th>
            <th scope="col">Gi??</th>
            <th scope="col">Ng??y b???t ?????u b??n</th>
            <th scope="col">Ng??y s???a th??ng tin</th>
            <th scope="col">Tr???ng th??i</th>
            <th scope="col">Thao t??c</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (int i = 0; i < foods.size(); i++) {
                Food food = foods.get(i);
        %>
        <tr>
            <td scope="col" style="text-align: center"><%=food.getId()%>
            </td>
            <td scope="col" style="text-align: center"><%=food.getName()%>
            </td>
            <td scope="col" id="categoryId" style="text-align: center"><%=food.getCategoryId()%>
            </td>
            <td scope="col" style="text-align: center"><%=food.getDescription()%>
            </td>
            <td scope="col" style="text-align: center">
                <div style="width: 100px; height: 100px; background-repeat: no-repeat; background-position: center; background-size: cover; background-image: url('<%=food.getThumbnail()%>')"></div>
            </td>
            <td scope="col" style="text-align: center"><%=food.getPrice()%>
            </td>
            <td scope="col"
                style="text-align: center"><%= ConvertHelper.convertJavaDateToSqlDateTime(food.getSaleStartDate())%>
            </td>
            <td scope="col"
                style="text-align: center"><%=ConvertHelper.convertJavaDateToSqlDateTime(food.getUpdatedAt())%>
            </td>
            <td scope="col" style="text-align: center"><%=food.toStatus(food.getStatus())%>
            </td>
            <td scope="col" style="text-align: center">
                <div class="btn-group btn-cn" id="<%=food.getId()%>">
                    <button class="btn btn-success detail">Chi ti???t</button>
                    <button class="btn btn-Warning edit">S???a</button>
                    <button class="btn btn-danger delete">X??a</button>
                </div>
            </td>
        </tr>
        <%}%>
        </tbody>
    </table>
    <ul class="pagination MyPagination" style="justify-content: center">
        <%
            for (int i = 1; i <= numPage; i++) {
                int p = Integer.parseInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));
                String active = p == i ? "active" : "";
        %>
        <li class="page-item <%=active%>" page="<%=i%>"><a class="page-link" href="?page=<%=i%>&num=5"><%=i%>
        </a></li>
        <%}%>
    </ul>
</div>

<script src="https://widget.cloudinary.com/v2.0/global/all.js" type="text/javascript"></script>
<script>
    $.get("/category/list", {}, function (categories) {
        $("table td#categoryId").each(function () {
            var t = $(this), id = t.text();
            $.map(categories, function (d) {
                d.id == t.text() && t.text(d.name);
            })
        })
    });
    $.isDom = function (dom) {
        return dom && typeof dom == "object" && dom.jquery && has("0.nodeType", dom)
    };
    $.isPlainObj = function (o) {
        return typeof o == 'object' && o.constructor == Object;
    };

    function has(a, b) {
        a = String(a).split(".");
        try {
            a.map(function (i) {
                b = b[i];
            })
        } catch (e) {
            b = undefined;
        }
        return b;
    };

    function popup() {
        var div = $(), id = "idModel", o = {
            title: ""
        },
            url = location.pathname,
            titlePage = document.title;
        Array.from(arguments).map(function (a) {
            if ($.isDom(a)) {
                div = a;
            } else if (typeof a === "string") {
                id = a;
            } else if ($.isPlainObj(a)) {
                o = $.extend(o, a);
            }
        });
        id = o.id || id;
        div = o.div || div;
        document.title = o.title;
        window.history.pushState(o.title, o.title, '/'+id);
        $("body").append(
            $('<div>', {id: id, class: 'modal', text: ''}).each(function () {
                var t = $(this);
                setTimeout(function () {
                    t.css({
                        display: "block",
                        overflow: "auto"
                    })
                }, 1)
            }).css({background: "#00000047"}).append(
                $('<div>', {id: '', class: 'modal-dialog', text: ''}).append(
                    $('<div>', {id: '', class: 'modal-content', text: ''}).append(
                        $('<div>', {id: '', class: 'modal-header', text: ''}).append(
                            $('<h4>', {id: '', class: 'modal-title', text: o.title}),
                            $("<button>", {type: "button", class: "close", text: 'x'}).click(function () {
                                $(this).closest("#" + id).remove();
                                document.title = titlePage;
                                window.history.pushState(titlePage, titlePage, url);

                            })
                        ),
                        $('<div>', {id: '', class: 'modal-body', text: ''}).append(
                            div
                        ),
                    ),
                ),
            ),
        )
    };

    function form(i, id, o) {
        o = $.extend({
            title: 'T???o m??n ??n m???i'
        }, o);
        var div = $('<div>', {id: '', class: '', text: ''}).append(
            $("<div>", {id: "StatusServer", class: "", style: "color:red; margin:15;"}),
            $('<div>', {id: '', class: 'form-group', text: ''}).append(
                $('<label>', {id: '', class: '', text: 'T??n m??n ??n'}),
                $('<input>', {
                    id: 'name',
                    class: 'form-control getVal',
                    placeholder: 'Nh???p t??n m??n ??n',
                    text: ''
                }).on("change", function () {
                    var t = $(this), v = t.val();
                    if (v.length <= 7) {
                        t.closest(".form-group").find(".statusPrice").remove()
                        t.closest(".form-group").append(
                            $("<div>", {class: "statusPrice", style: "color:red;", text: "T??n m??n ??n ph???i l???n h??n 7 k?? t???"})
                        )
                        div.find("#btnSubmit").removeClass("btn-primary").addClass("btn-secondary").css("pointer-events", "none");
                    } else {
                        t.closest(".form-group").find(".statusPrice").remove();
                        div.find("#btnSubmit").addClass("btn-primary").removeClass("btn-secondary").css("pointer-events", "all");
                    }
                }),
            ),
            $('<div>', {id: '', class: 'form-group', text: ''}).append(
                $('<label>', {id: '', class: '', text: 'Ch???n danh m???c'}),
                $('<select>', {id: 'categoryId', class: 'form-control getVal', text: ''}).each(function () {
                    var t = $(this);
                    $.get("/category/list", {}, function (categories) {
                        t.append(
                            $.map(categories, function (d, i) {
                                return $('<option>', {id: '', value: d.id, class: '', text: d.name});
                            })
                        );
                    });
                })
            ),
            $('<div>', {id: '', class: 'form-group', text: ''}).append(
                $('<label>', {id: '', class: '', text: 'M?? t???'}),
                $('<textarea>', {
                    type: 'text',
                    id: 'description',
                    class: 'form-control getVal',
                    placeholder: 'Nh???p m?? t???',
                    text: ''
                }),
            ),
            $('<div>', {id: '', class: 'form-group', text: ''}).append(
                $('<label>', {id: '', class: '', text: '???nh ?????i di???n'}),
                $('<div>', {id: 'uploadImg', class: '', style: "textAlign: center", text: ''}),
                $('<input>', {
                    type: 'hidden',
                    id: 'thumbnail',
                    class: 'form-control getVal',
                    placeholder: '',
                    text: ''
                }),
                $('<button>', {
                    id: 'btnUploadImg',
                    class: 'btn btn-success',
                    text: 'Upload ???nh',
                    style: 'marginTop:15px'
                }).click(function () {
                    cloudinary.openUploadWidget({
                        unsigned: true,
                        upload_preset: "default",
                        folder: 'img',
                        cloudName: "nguyendangbang",
                        sources: ['local', 'url', 'dropbox', 'image_search'],
                    }, function (e, v) {
                        var url = has("info.files.0.uploadInfo.url", v);
                        if (url) {
                            div.find("#uploadImg").css({textAlign: "center"}).empty().append(
                                $("<img>", {src: url, class: "", style: "width:60%; margin:0 auto"})
                            )
                            $("#thumbnail").val(url);
                        }
                    })
                }),
            ),
            $('<div>', {id: '', class: 'form-group', text: ''}).append(
                $('<label>', {id: '', class: '', text: 'Gi??'}),
                $('<input>', {
                    type: 'text',
                    id: 'price',
                    class: 'form-control getVal',
                    placeholder: 'Nh???p gi??',
                    text: ''
                }).on("change", function () {
                    var t = $(this), v = t.val();
                    if (v < 1) {
                        t.closest(".form-group").find(".statusPrice").remove()
                        t.closest(".form-group").append(
                            $("<div>", {class: "statusPrice", style: "color:red;", text: "Gi?? ph???i l???n h??n 0"})
                        )
                        div.find("#btnSubmit").removeClass("btn-primary").addClass("btn-secondary").css("pointer-events", "none");
                    } else {
                        t.closest(".form-group").find(".statusPrice").remove();
                        div.find("#btnSubmit").addClass("btn-primary").removeClass("btn-secondary").css("pointer-events", "all");
                    }
                }),
            ),
            $('<div>', {id: '', class: 'form-group', text: ''}).append(
                $('<label>', {id: '', class: '', text: 'Tr???ng th??i'}),
                $('<select>', {id: 'status', class: 'form-control getVal', text: ''}).append(
                    $.map({1: "??ang b??n", 2: "D???ng b??n"}, function (i, v) {
                        return $('<option>', {id: '', value: v, class: '', text: i})
                    })
                ),
            ),
            $('<button>', {id: 'btnSubmit', class: 'btn btn-primary', text: o.button}).click(function () {
                var data = {};
                div.find(".getVal").each(function () {
                    var t = $(this);
                    data[t.attr("id")] = t.val();
                });
                if (i) {
                    data = $.extend(div.data("data"), data);
                    console.log(i, data);
                    $.ajax({
                        method: "post",
                        url: "/food/update",
                        data: data,
                        dataType: "text",
                        success: function (v) {
                            console.log(v)
                            if (v != 1) {
                                div.find("#StatusServer").text(v);
                            } else {
                                console.log(data)
                                window.location.href = '/?page=1';
                            }
                        }
                    })
                } else {
                    $.ajax({
                        method: "post",
                        url: "/food/create",
                        data: data,
                        dataType: "text",
                        success: function (v) {
                            console.log(v)
                            if (v != 1) {
                                div.find("#StatusServer").text(v);
                            } else {
                                window.location.href = '/?page=1';
                            }
                        }
                    })
                }
            }),
        ).each(function () {
            if (i) {
                $.get("/food/detail", {id: i}, function (d) {
                    if (d) {
                        div.data({data: d}).find(".getVal").each(function () {
                            var t = $(this), key = t.attr("id");
                            t.val(d[key]).change();
                            if (key == "thumbnail" && d[key]) {
                                div.find("#uploadImg").css({textAlign: "center"}).empty().append(
                                    $("<img>", {src: d[key], class: "", style: "width:60%; margin:0 auto"})
                                )
                            }
                        });
                    } else {
                        $("#formFood #status").text("Kh??ng t???n t???i m??n ??n n??y");
                    }
                })
            }
        });
        popup(div, id, o);
    };

    function toStatusFood(status) {
        if (status == 0) {
            return "???? x??a";
        } else if (status == 1) {
            return "??ang B??n";
        } else if (status == 2) {
            return "D???ng b??n";
        } else {
            return "...";
        }
    }

    $("#createFood").on("click", function () {
        form(null, "formFood", {
            title: "T???o m??n ??n m???i",
            button: "T???o m??n ??n"
        })
    });
    //Xem chi ti???t
    $(".btn-cn").on("click", ".btn", function () {
        var t = $(this),
            id = t.parent().attr("id");
        //Xem chi ti???t
        if (t.hasClass("detail")) {
            $.get("/food/detail", {id: id}, function (d) {
                $.get("/category/list", {}, function (categories) {
                    var doi = {};
                    $.map(categories, function (d, i) {
                        doi[d.id] = d.name;
                    })
                    d = d || {};
                    popup({
                        id: "food_" + d.id,
                        title: "Name: " + d.name,
                        div: $("<div>", {class: "warp"}).append(
                            $("<div>", {style: "width: 100%; padding-bottom: 50%; background-repeat: no-repeat; background-position: center; background-size: cover; background-image: url('" + (d.thumbnail || "https://www.noithatjhome.com/wp-content/uploads/2019/07/thiet-ke-nha-hang-1.jpg") + "')"}),
                            $("<h4>", {class: "card-title", text: "Category: "}),
                            $("<p>", {class: "card-text", text: doi[d.categoryId] || "..."}),
                            $("<h4>", {class: "card-title", text: "Description: "}),
                            $("<p>", {class: "card-text", text: d.description || "..."}),
                            $("<h4>", {class: "card-title", text: "Price: "}),
                            $("<p>", {class: "card-text", text: d.price || "..."}),
                            $("<h4>", {class: "card-title", text: "Status: "}),
                            $("<p>", {class: "card-text", text: toStatusFood(d.status)}),
                            $("<h4>", {class: "card-title", text: "Updated At: "}),
                            $("<p>", {class: "card-text", text: new Date(d.updatedAt).toLocaleString()}),
                        )
                    })

                })
            })

        } else if (t.hasClass("edit")) {
            form(id, "formFood", {
                title: "C???p nh???t m??n ??n",
                button: "C???p nh???t m??n ??n"
            })
        } else if (t.hasClass("delete")) {
            if(confirm("B???n c?? ch???c ch???n mu???n x??a m??n ??n n??y kh??ng?")){
                $.post("/food/delete", {id: id}, function (status) {
                    if (status == 1) {
                        alert("X??a m??n ??n th??nh c??ng");
                        t.closest("tr").remove()
                    } else {
                        alert("X??a m??n ??n th???t b???i");
                    }
                })
            }
        }
    })
</script>
</body>
</html>

