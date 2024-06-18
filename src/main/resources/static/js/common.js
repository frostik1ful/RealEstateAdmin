function reFillPagination(paginationBlock, pageNumber, totalPages, totalElements, pagingInfo, pagingShowed, numberOfElements, pagingFrom, pagingRecords, onPageClick, onNextPageClick, onPrevPageClick){
    paginationBlock.empty()
    let needFirstDots = false
    let needSecondDots = false
    let countOfPages = 0
    let lastIndex = 0

    const currentPage = pageNumber + 1

    if (totalPages > 7 && currentPage - 3 > 1){
        needFirstDots = true
        countOfPages++
    }
    if (totalPages > 7 && totalPages - 3 > currentPage){
        needSecondDots = true
        countOfPages += 2
    }

    if (pagingInfo && totalElements > 0){
        refillPagingInfoBlock()
    }
    else if (pagingInfo) {
        hide(pagingInfo)
    }

    if (totalPages > 1){
        unHide(paginationBlock)
        if (pageNumber === 0){
            paginationBlock.append($('<li class="page-item prev">' +
                '<a class="page-link waves-effect disabled" role="button">' +
                '<i class="ti ti-chevron-left ti-xs"></i>' +
                '</a>\n' +
                '</li>'))
        }
        else {

            const item = $('<li class="page-item prev">' +
                '<a class="page-link waves-effect" onclick="" role="button">' +
                '<i class="ti ti-chevron-left ti-xs"></i>' +
                '</a>' +
                '</li>')
            item.on('click', onPrevPageClick)
            paginationBlock.append(item)
        }
        if (currentPage === 1){
            countOfPages++
            lastIndex++
            paginationBlock.append(getPageItem(1, true))
        }
        else {
            countOfPages++
            lastIndex++
            paginationBlock.append(getPageItem(1, false))
        }

        //first ...
        if (needFirstDots){
            if (needSecondDots){
                lastIndex = currentPage - 1
            }
            else {
                lastIndex = totalPages - 4
            }
            paginationBlock.append($('<li class="page-item disabled">\n' +
                '<a class="page-link waves-effect" role="button">...</a>\n' +
                '</li>\n'))

            for (let i = 0; i < (7 - countOfPages); i++) {
                if (lastIndex === totalPages){
                    break
                }
                if (currentPage === lastIndex){
                    paginationBlock.append(getPageItem(lastIndex, true))
                }
                else {
                    paginationBlock.append(getPageItem(lastIndex, false))
                }
                lastIndex++
            }
        }
        else if (lastIndex < totalPages){
            for (let i = 0; i < (7 - countOfPages); i++) {
                lastIndex++
                if (lastIndex === totalPages){
                    break
                }
                if (currentPage === lastIndex){
                    paginationBlock.append(getPageItem(lastIndex, true))
                }
                else {
                    paginationBlock.append(getPageItem(lastIndex, false))
                }
            }
        }

        //second...
        if (needSecondDots){
            paginationBlock.append($('<li class="page-item disabled">' +
                '<a class="page-link waves-effect" role="button">...</a>' +
                '</li>'))

        }

        if (currentPage === totalPages){
            paginationBlock.append(getPageItem(totalPages, true))
        }
        else {
            paginationBlock.append(getPageItem(totalPages, false))
        }

        if (currentPage < totalPages){
            const item = $('<li class="page-item next">' +
                '<a class="page-link waves-effect"  role="button">' +
                '<i class="ti ti-chevron-right ti-xs"></i>' +
                '</a>\n' +
                '</li>')
            item.on('click', onNextPageClick)
            paginationBlock.append(item)
        }
        else {
            paginationBlock.append($('<li class="page-item next">' +
                '<a class="page-link waves-effect disabled" role="button">' +
                '<i class="ti ti-chevron-right ti-xs"></i>' +
                '</a>' +
                '</li>'))
        }
    }

    function getPageItem(number, active){
        let item
        if (active){
            item = $(`<li class="page-item active">
                           <a class="page-link waves-effect" role="button">${number}</a>
                      </li>`)
        }
        else {
            const button = $(`<a class="page-link waves-effect"  role="button">${number}</a>`)
            item = $(`<li class="page-item"></li>`)
            item.append(button)
            button.on('click',()=> onPageClick(button))
        }
        return item

    }

    function refillPagingInfoBlock(){
        unHide(pagingInfo)
        pagingInfo.text(pagingShowed + ' ' +((pageNumber * 25) + 1) +' - '+ (numberOfElements + (pageNumber * 25))  +' '+pagingFrom +' '+totalElements+' '+pagingRecords)
    }
}
function sendRequest(url, requestType, data, onSuccess, onFail){
    $.ajax({
        url: url,
        type: requestType,
        contentType: "application/json",
        data: JSON.stringify(data),
        success: (data) => {
            onSuccess(data)},
        error: (data) => {
            if (data.responseJSON === undefined){
                return
            }
            if (onFail){
                onFail(data)
            }}
    }).fail(function (response) {

    })
}
function setupSimpleSelect2(select, placeholder, dropdownParent, allowClear){
    return select.select2({
        placeholder: placeholder,
        minimumResultsForSearch: -1,
        dropdownParent: dropdownParent,
        allowClear: allowClear ? allowClear : false,
        templateResult: function (data, container) {
            if (data.element) {
                $(container).addClass($(data.element).attr("class"));
            }
            return data.text;
        }
    })
}
function hide(element){
    element.attr('hidden', true)
}

function unHide(element){
    element.removeAttr('hidden')
}
function toggleModal(modal){
    modal.modal('toggle')
}
function addErrorClass(input){
    input.addClass('is-invalid')
}

function removeErrorClass(input){
    input.removeClass('is-invalid')
}
function toggleError(key, input, map){
    const errorBlock = input.parent().find('.invalid-feedback')
    if (map.has(key)){
        addErrorClass(input)
        errorBlock.text(map.get(key))
        unHide(errorBlock)
    }
    else {
        removeErrorClass(input)
        hide(errorBlock)
    }
}
function removeErrorClassOnInput(input){
    input.on('input',() => {
        removeErrorClass(input)
        hide(input.parent().find('.invalid-feedback'))
    });
}
function print(e){
    console.log(e)
}