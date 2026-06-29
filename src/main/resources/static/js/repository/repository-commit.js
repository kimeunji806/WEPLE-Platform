(() => {
    const searchInput = document.getElementById('commitFileSearch');
    const filterButtons = document.querySelectorAll('[data-commit-filter]');
    const fileLinks = document.querySelectorAll('[data-commit-tree-file]');
    const folderRows = document.querySelectorAll('[data-commit-tree-folder]');
    const diffFiles = document.querySelectorAll('[data-commit-diff-file]');
    const emptyMessage = document.getElementById('commitFileEmpty');

    let selectedFilter = 'all';
    const collapsedFolders = new Set();

    const normalize = (value) => (value || '').toLowerCase();
    const hasActiveSearch = () => {
        const keyword = searchInput ? searchInput.value.trim() : '';
        return Boolean(keyword) || selectedFilter !== 'all';
    };

    const isHiddenByCollapsedParent = (filePath) => {
        if (hasActiveSearch()) {
            return false;
        }
        return [...collapsedFolders].some((folderPath) => filePath.startsWith(`${folderPath}/`));
    };

    const isHiddenByCollapsedAncestor = (folderPath) => {
        if (hasActiveSearch()) {
            return false;
        }
        return [...collapsedFolders].some((collapsedPath) =>
            folderPath !== collapsedPath && folderPath.startsWith(`${collapsedPath}/`)
        );
    };

    const matchesFileFilter = (fileLink, keyword) => {
        const path = normalize(fileLink.dataset.filePath);
        const name = normalize(fileLink.dataset.fileName);
        const status = fileLink.dataset.fileStatus || '';
        const matchesKeyword = !keyword || path.includes(keyword) || name.includes(keyword);
        const matchesStatus = selectedFilter === 'all' || status === selectedFilter;
        return matchesKeyword && matchesStatus;
    };

    const hasMatchedDescendant = (folderPath, keyword) => {
        for (const fileLink of fileLinks) {
            const filePath = fileLink.dataset.filePath || '';
            if (filePath.startsWith(`${folderPath}/`) && matchesFileFilter(fileLink, keyword)) {
                return true;
            }
        }
        return false;
    };

    const updateFolderVisibility = (keyword) => {
        folderRows.forEach((folder) => {
            const folderPath = folder.dataset.folderPath || '';
            folder.hidden = !hasMatchedDescendant(folderPath, keyword) || isHiddenByCollapsedAncestor(folderPath);
            folder.setAttribute('aria-expanded', String(!collapsedFolders.has(folderPath) || hasActiveSearch()));
        });
    };

    const applyFilter = () => {
        const keyword = normalize(searchInput ? searchInput.value.trim() : '');
        let matchedCount = 0;

        fileLinks.forEach((link) => {
            const isMatched = matchesFileFilter(link, keyword);
            const isVisible = isMatched && !isHiddenByCollapsedParent(link.dataset.filePath || '');

            link.hidden = !isVisible;
            if (isMatched) {
                matchedCount += 1;
            }
        });

        updateFolderVisibility(keyword);
        if (emptyMessage) {
            emptyMessage.hidden = matchedCount > 0;
        }
    };

    const clearSelected = () => {
        fileLinks.forEach((link) => link.classList.remove('is-selected'));
        diffFiles.forEach((diff) => diff.classList.remove('is-selected'));
    };

    fileLinks.forEach((link) => {
        link.addEventListener('click', () => {
            clearSelected();
            link.classList.add('is-selected');

            const target = document.getElementById(link.dataset.targetId);
            if (target) {
                target.classList.add('is-selected');
            }
        });
    });

    folderRows.forEach((folder) => {
        folder.addEventListener('click', () => {
            const folderPath = folder.dataset.folderPath || '';
            if (collapsedFolders.has(folderPath)) {
                collapsedFolders.delete(folderPath);
            } else {
                collapsedFolders.add(folderPath);
            }
            applyFilter();
        });
    });

    filterButtons.forEach((button) => {
        button.addEventListener('click', () => {
            selectedFilter = button.dataset.commitFilter;
            filterButtons.forEach((item) => item.classList.toggle('active', item === button));
            applyFilter();
        });
    });

    if (searchInput) {
        searchInput.addEventListener('input', applyFilter);
    }

    applyFilter();
})();
