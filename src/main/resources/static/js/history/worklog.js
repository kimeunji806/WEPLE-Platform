(function () {
    var DATE_FORMAT = 'Y.m.d';
    if (window.flatpickr && flatpickr.l10ns && flatpickr.l10ns.ko) {
        flatpickr.localize(flatpickr.l10ns.ko);
    }
    function parseFormatted(str) {
        if (!str) return null;
        var parts = str.split('.');
        if (parts.length !== 3) return null;
        var d = new Date(parseInt(parts[0], 10), parseInt(parts[1], 10) - 1, parseInt(parts[2], 10));
        return isNaN(d.getTime()) ? null : d;
    }
    function addDays(date, days) {
        var d = new Date(date.getTime());
        d.setDate(d.getDate() + days);
        return d;
    }

    var startInput = document.getElementById('startDateInput');
    var endInput   = document.getElementById('endDateInput');
    var searchForm = document.getElementById('searchForm');
    var searchFlag = document.getElementById('searchFlag');

    function syncTimePeriod() {
        var periodEl = document.querySelector('.time-period');
        if (periodEl && startInput.value && endInput.value) {
            periodEl.textContent = startInput.value + ' ~ ' + endInput.value;
        }
    }

    var startPicker = flatpickr(startInput, {
        dateFormat: DATE_FORMAT, allowInput: false,
        onChange: function (selectedDates) {
            if (selectedDates[0]) endPicker.set('minDate', selectedDates[0]);
        }
    });
    var endPicker = flatpickr(endInput, {
        dateFormat: DATE_FORMAT, allowInput: false,
        onChange: function (selectedDates) {
            if (selectedDates[0]) startPicker.set('maxDate', selectedDates[0]);
        }
    });

    function setRange(start, end) {
        startPicker.set('maxDate', null);
        endPicker.set('minDate', null);
        startPicker.setDate(start, false);
        endPicker.setDate(end, false);
        startPicker.set('maxDate', end);
        endPicker.set('minDate', start);
    }
    function applyRecentDays(days) {
        var today = new Date();
        setRange(addDays(today, -(days - 1)), today);
    }

    var initStart = parseFormatted(startInput.value);
    var initEnd   = parseFormatted(endInput.value);
    if (initStart && initEnd) {
        setRange(initStart, initEnd);
    } else {
        applyRecentDays(5);
    }
    syncTimePeriod();

    document.getElementById('recentPeriodBtn').addEventListener('click', function () {
        applyRecentDays(5);
        searchFlag.value = 'true';
        setTimeout(function () { syncTimePeriod(); searchForm.submit(); }, 0);
    });

    document.getElementById('dateBackBtn').addEventListener('click', function () {
        var curStart = parseFormatted(startInput.value);
        var curEnd   = parseFormatted(endInput.value);
        if (!curStart || !curEnd) {
            applyRecentDays(5);
        } else {
            var rangeDays = Math.round((curEnd.getTime() - curStart.getTime()) / (24*60*60*1000)) + 1;
            var newEnd    = addDays(curStart, -1);
            var newStart  = addDays(newEnd, -(rangeDays - 1));
            setRange(newStart, newEnd);
        }
        searchFlag.value = 'true';
        setTimeout(function () { syncTimePeriod(); searchForm.submit(); }, 0);
    });

    document.getElementById('resetBtn').addEventListener('click', function () {
        var projectSelect = searchForm.querySelector('select[name="projectId"]');
        if (projectSelect) projectSelect.value = '';
        var userSelect = searchForm.querySelector('select[name="userCode"]');
        if (userSelect) userSelect.value = '';
        searchForm.querySelectorAll('input[type="checkbox"]').forEach(function(cb) {
            cb.checked = false;
        });
        applyRecentDays(5);
        searchFlag.value = 'false';
        setTimeout(function () { syncTimePeriod(); searchForm.submit(); }, 0);
    });
})();