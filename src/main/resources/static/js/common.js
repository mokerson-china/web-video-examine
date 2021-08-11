/*
 * document.writeln(" 是否为移动终端: " + browser.versions.mobile); document.writeln("
 * ios终端: " + browser.versions.ios); document.writeln(" android终端: " +
 * browser.versions.android); document.writeln(" 是否为iPhone: " +
 * browser.versions.iPhone); document.writeln(" 是否iPad: " +
 * browser.versions.iPad); document.writeln(navigator.userAgent);
 */
isEmpty = function(anyType) {
    var is = false;
    if (anyType != undefined && anyType != null) {
        switch (Object.prototype.toString.apply(anyType)) {
            case "[object String]":
                anyType = $.trim(anyType.toLocaleLowerCase());
                is = (anyType == "undefined" || anyType == "null" || anyType.length < 1);
                break;
            case "[object Number]":
                break;
            case "[object Boolean]":
                break;
            case "[object Object]":
                is = $.isEmptyObject(anyType);
                break;
            case "[object Array]":
                is = (anyType.length < 1);
                break;
        }
    } else {
        is = true;
    }
    return is;
}