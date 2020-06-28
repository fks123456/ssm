
// 判断是否有权限permission
function limit(permission) {
    var flag = false
    $.ajax({
        type: "POST",
        // 同步请求
        async: false,
        url: '/user/isPermission',
        data: {
            permission: permission
        },
        success: function (res) {
            flag = res
        }
    })

    return flag
}