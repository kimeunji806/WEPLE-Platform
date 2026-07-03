window.addEventListener('DOMContentLoaded', function() {
    const navigationEntries = performance.getEntriesByType('navigation');
    if (navigationEntries.length > 0 && navigationEntries[0].type === 'reload') {
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.has('keyword')) {
            window.location.href = '/project';
        }
    }
});