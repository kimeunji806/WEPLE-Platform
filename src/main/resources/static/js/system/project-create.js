document.addEventListener("DOMContentLoaded", function () {

    flatpickr.localize(flatpickr.l10ns.ko);

    const startPicker = flatpickr("#startDate", {
        dateFormat: "Y-m-d",
        allowInput: false,
        onChange: function(selectedDates) {
            if (selectedDates.length > 0) {
                finishPicker.set("minDate", selectedDates[0]);
            }
        }
    });

    const finishPicker = flatpickr("#finishDate", {
        dateFormat: "Y-m-d",
        allowInput: false
    });

	// 설정 모듈: 클릭해도 해제 안 되도록
    const settingsCheckbox = document.getElementById("moduleSettings");
    settingsCheckbox.addEventListener("click", function (e) {
        e.preventDefault();
        this.checked = true;
    });
    
	// 개요 모듈: 클릭해도 해제 안 되도록
    const overviewCheckbox = document.getElementById("moduleOverview");
    overviewCheckbox.addEventListener("click", function (e) {
        e.preventDefault();
        this.checked = true;
    });

 	// 전체선택
    document.getElementById("selectAll").addEventListener("click", function () {
        document.querySelectorAll('input[name="moduleNames"]')
            .forEach(cb => cb.checked = true);
    });

 	// 전체해제 (개요, 설정 제외)
    document.getElementById("deselectAll").addEventListener("click", function () {
        document.querySelectorAll('input[name="moduleNames"]')
            .forEach(cb => {
                if (cb.value !== "b1" && cb.value !== "b11") cb.checked = false;
            });
    });


    // 토스트 자동 제거
    const toastWrap = document.getElementById("toastWrap");
    if (toastWrap) {
        setTimeout(() => toastWrap.remove(), 3500);
    }

});