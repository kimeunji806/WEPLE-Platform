function redirectToInsertTask() {
    const selectElement = document.getElementById('modalProjectId');
    const selectedId = selectElement.value;

    if (!selectedId) {
        alert('프로젝트를 선택해주세요.');
        return;
    }

    // HTML에서 글로벌 전역 변수로 할당한 타임리프 컨텍스트 주소를 참조합니다.
    const baseUrl = window.insertTaskBaseUrl || '/project/task/insert';
    location.href = `${baseUrl}?projectId=${selectedId}`;
}



    // 팝업 토글 함수
    function toggleDropdown(button) {
        const currentMenu = button.nextElementSibling;
        
        // 다른 행에 열려있는 팝업이 있다면 모두 닫기
        document.querySelectorAll('.dropdown-menu-box').forEach(menu => {
            if (menu !== currentMenu) {
                menu.classList.remove('show');
            }
        });
        
        // 현재 누른 팝업 켜고 끄기
        currentMenu.classList.toggle('show');
    }

    // 바탕화면이나 다른 곳 클릭 시 팝업 닫기
    window.addEventListener('click', function(e) {
        if (!e.target.matches('.more-options-btn')) {
            document.querySelectorAll('.dropdown-menu-box').forEach(menu => {
                menu.classList.remove('show');
            });
        }
    });

    // 삭제 처리 함수 예시
    function handleDelete(taskId, projectId) {
        if (confirm("정말 이 일감을 삭제하시겠습니까?")) {
            // REST API 혹은 컨트롤러 매핑 주소로 요청 전송
            location.href = `/project/task/delete/${taskId}?projectId=${projectId}`;
        }
    }
	document.addEventListener('DOMContentLoaded', function() {
	    // ==========================================
	    // 1. Choices.js 플러그인 초기화 및 예외 처리
	    // ==========================================
	    const element = document.getElementById('memberChoices');
	    let choices = null; // 블록 밖에서 null로 초기화

	    // 요소가 존재할 때만 플러그인 적용 및 이벤트 등록
	    if (element) {
	        choices = new Choices(element, {
	            removeItemButton: true,
	            searchPlaceholderValue: '담당자 검색...',
	            noResultsText: '검색 결과 없음',
	            itemSelectText: '', // 마우스 올렸을 때 뜨는 무거운 텍스트 제거
	            shouldSort: false,
	            searchFloor: 1,     // 1글자만 쳐도 검색 시작
	            placeholder: true,
	            placeholderValue: '담당자를 선택하세요'
	        });

	        // [이벤트] Choices.js에서 항목 추가/삭제 시 업데이트 연동
	        element.addEventListener('addItem', updateChips);
	        element.addEventListener('removeItem', updateChips);
	    }

	    // ==========================================
	    // 2. 검색 조건 칩 및 필터 접기/펴기 로직
	    // ==========================================
	    const searchForm = document.getElementById('searchForm');
	    const filterWrapper = document.getElementById('filter-rows-wrapper');
	    const toggleBtn = document.getElementById('toggleFilterBtn');
	    
	    const selectedWrap = document.getElementById('selectedConditionsWrap');
	    const selectedCountSpan = document.getElementById('selectedCount');
	    const chipContainer = document.getElementById('chipContainer');
	    const resetAllBtn = document.getElementById('resetAllBtn');

	    // 필수 요소들이 없으면 스크립트 중단 (안전장치)
	    if (!searchForm || !filterWrapper || !toggleBtn || !selectedWrap) return;

	    // [기능] 필터 접기/펴기
	    toggleBtn.addEventListener('click', function() {
	        if (filterWrapper.style.display === 'none') {
	            filterWrapper.style.display = 'block'; 
	            toggleBtn.innerHTML = '▲ 검색 필터 접기';
	        } else {
	            filterWrapper.style.display = 'none';
	            toggleBtn.innerHTML = '▼ 검색 필터 펴기';
	        }
	    });

	    // [기능] 선택된 조건에 맞춰 칩 업데이트
	    function updateChips() {
	        chipContainer.innerHTML = '';
	        let count = 0;

	        // 1) 체크박스 조건 수집 (예외 처리 강화)
	        const checkboxes = document.querySelectorAll('.filter-options input[type="checkbox"]:checked');
	        checkboxes.forEach(cb => {
	            const row = cb.closest('.filter-row');
	            if (row) {
	                const labelElem = row.querySelector('.filter-label');
	                const categoryName = labelElem ? labelElem.innerText.trim() : '';
	                const valueName = cb.nextElementSibling ? cb.nextElementSibling.innerText.trim() : cb.parentElement.innerText.trim();
	                createChip(categoryName, valueName, cb);
	                count++;
	            }
	        });

	        // 2) Choices.js 셀렉트박스 조건 수집 (choices 객체가 있을 때만 실행)
	        if (choices && element) {
	            const selectedMembers = choices.getValue(); 
	            if (Array.isArray(selectedMembers)) {
	                const row = element.closest('.filter-row');
	                const labelElem = row ? row.querySelector('.filter-label') : null;
	                const categoryName = labelElem ? labelElem.innerText.trim() : '담당자';
	                
	                selectedMembers.forEach(member => {
	                    createChip(categoryName, member.label, null, member.value);
	                    count++;
	                });
	            }
	        }

	        // 카운트 및 영역 표시 제어
	        if (selectedCountSpan) {
	            selectedCountSpan.innerText = `선택된 조건 ${count}`;
	        }
	        
	        if (count > 0) {
	            selectedWrap.style.display = 'block';
	        } else {
	            selectedWrap.style.display = 'none';
	        }
	    }

	    // [기능] 개별 칩 생성 및 삭제(X) 이벤트 바인딩
	    function createChip(category, value, inputElement, choicesValue = null) {
	        const chip = document.createElement('div');
	        chip.className = 'filter-chip';
	        chip.innerHTML = `${category}: ${value} <span class="chip-close">×</span>`;

	        const closeBtn = chip.querySelector('.chip-close');
	        if (closeBtn) {
	            closeBtn.addEventListener('click', function() {
	                if (inputElement && inputElement.tagName === 'INPUT') {
	                    // 일반 체크박스 해제
	                    inputElement.checked = false;
	                } else if (choicesValue !== null && choices) {
	                    // choices 객체가 초기화된 상태일 때만 API 호출
	                    choices.removeActiveItemsByValue(choicesValue);
	                }
	                updateChips(); 
	            });
	        }

	        chipContainer.appendChild(chip);
	    }

	    // [이벤트] 일반 체크박스 변경 시 업데이트
	    searchForm.addEventListener('change', updateChips);

	    // [이벤트] 전체 초기화 버튼
	    if (resetAllBtn) {
	        resetAllBtn.addEventListener('click', function() {
	            // 체크박스 일괄 해제
	            const checkboxes = document.querySelectorAll('.filter-options input[type="checkbox"]');
	            checkboxes.forEach(cb => cb.checked = false);

	            // choices 객체가 있을 때만 일괄 해제
	            if (choices) {
	                choices.removeActiveItems();
	            }

	            updateChips();
	        });
	    }

	    // 페이지 로드 시 기존 선택된 파라미터가 있을 수 있으므로 최초 1회 실행
	    updateChips();
	});
	
	function goPage(pageNumber) {
	            document.getElementById('pageInput').value = pageNumber;
	            document.getElementById('searchForm').submit();
	        }
