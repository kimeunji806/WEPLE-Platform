function initProjectList() {
    let deleteForm = null;

    document.querySelectorAll(".delete-project-btn").forEach(function (btn) {
        btn.addEventListener("click", function () {

            deleteForm = this.closest("td").querySelector(".delete-form");
            const title = this.dataset.projectTitle || "";
            document.getElementById("modalProjectTitle").textContent = "[" + title + "]";
            document.getElementById("confirmModal").style.display = "flex";
        });
    });
    window.closeModal = function () {
        document.getElementById("confirmModal").style.display = "none";
    };

    window.submitDelete = function () {
        if (deleteForm) {
            document.getElementById("confirmModal").style.display = "none";
            deleteForm.submit();
        }
    };

    // 토스트
    const toast = document.getElementById("toast");
    const toastText = toast ? toast.textContent.trim() : "";
    if (toast && toastText !== "") {
        toast.style.opacity = "1";
        toast.style.visibility = "visible";
        setTimeout(function () {
            toast.style.opacity = "0";
            toast.style.visibility = "hidden";
        }, 3000);
    }
}

// scriptFragment 로드 완료 후 실행 보장
if (document.readyState === "complete") {
    initProjectList();
} else {
    window.addEventListener("load", initProjectList);
}

function toggleDropdown(el) {
    document.querySelectorAll('.dropdown-menu-box').forEach(menu => {
        if (menu !== el.nextElementSibling) {
            menu.classList.remove('show');
        }
    });

    el.nextElementSibling.classList.toggle('show');
}

document.addEventListener('click', function () {
    document.querySelectorAll('.dropdown-menu-box').forEach(menu => {
        menu.classList.remove('show');
    });
});