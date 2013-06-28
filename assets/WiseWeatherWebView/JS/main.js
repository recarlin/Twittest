// Russell Carlin
window.addEventListener("DOMContentLoaded", function () {
    function ge(id) {
        var e = document.getElementById(id);
        return e;
    };
    function newForecast() {
        var zip = ge("zip").value;
        doIt.getNew(zip);
    };
    function homeForecast() {
        doIt.getHome();
    };
    var nf = ge("new");
    nf.addEventListener("click", newForecast);
    var hf = ge("home");
    hf.addEventListener("click", homeForecast);
});