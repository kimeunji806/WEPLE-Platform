const toast = document.getElementById("toast");
if (toast) {
    toast.style.opacity = "1";
    toast.style.visibility = "visible";
    setTimeout(() => {
        toast.style.opacity = "0";
        toast.style.visibility = "hidden";
    }, 3000);
}