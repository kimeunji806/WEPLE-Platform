document.addEventListener("DOMContentLoaded", function () {

    flatpickr.localize(flatpickr.l10ns.ko);

    const startPicker = flatpickr("#startDate", {
        dateFormat: "Y-m-d",
        allowInput: false,
        onChange: function(selectedDates) {
            if (selectedDates[0]) {
                finishPicker.set('minDate', selectedDates[0]);
            }
        }
    });

    const finishPicker = flatpickr("#finishDate", {
        dateFormat: "Y-m-d",
        allowInput: false,
        onChange: function(selectedDates) {
            if (selectedDates[0]) {
                startPicker.set('maxDate', selectedDates[0]);
            }
        }
    });

    // 전체선택
    document.getElementById("selectAll").addEventListener("click", function () {
        document.querySelectorAll('input[name="moduleNames"]')
            .forEach(cb => cb.checked = true);
    });

    // 전체해제
    document.getElementById("deselectAll")
	.addEventListener("click", function () {
	    document.querySelectorAll('input[name="moduleNames"]')
	        .forEach(cb => {
	            if(cb.value !== "b1" && cb.value !== "b11"){
	                cb.checked = false;
	            }
	        });
	});

    // 폼 제출 시 최소 1개 체크 검증
    document.getElementById("projectForm").addEventListener("submit", function (e) {
        const checked = document.querySelectorAll('input[name="moduleNames"]:checked');
        if (checked.length === 0) {
            e.preventDefault();
            const wrap = document.createElement("div");
            wrap.className = "toast-wrap";
            wrap.innerHTML = '<div class="toast-msg toast-error">최소 1개 이상의 모듈을 선택해야 합니다.</div>';
            document.querySelector(".project-page").prepend(wrap);
            setTimeout(() => wrap.remove(), 3500);
        }
    });

    // 토스트 자동 제거
    const toastWrap = document.getElementById("toastWrap");
    if (toastWrap) {
        setTimeout(() => toastWrap.remove(), 3500);
    }

});