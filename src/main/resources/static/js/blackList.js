const $tableBody = $('#users-table-body')
const $paginationBlock = $('#paginationBlock')
const $searchInput = $('#search-input')
let pageNumber = 0
let totalPages = 1
let numberOfElements = 0
let totalElements = 0

let filterTimeout

initPage()
function initPage(){
    fillTable()
}
function fillTable(){
    const form = {
        search : $searchInput.val(),
        pageNumber
    }
    sendRequest(
        '/users/get-all-blacklist',
        'POST',
        form,
        (data) => {
            pageNumber = data.number
            totalPages = data.totalPages
            numberOfElements = data.numberOfElements
            totalElements = data.totalElements
            addElementsToTable(data.content)
            const onPageClick = (button)=>{
                pageNumber = Number.parseInt($(button).text()) - 1
                fillTable()
            }
            const onNextPageClick = () => {
                if(totalPages - pageNumber > 1){
                    pageNumber++
                }
                fillTable()
            }
            const onPrevPageClick = () => {
                if(pageNumber > 0){
                    pageNumber--
                }
                fillTable()
            }

            reFillPagination($paginationBlock, pageNumber, totalPages, totalElements, null, null, numberOfElements, null, null, onPageClick, onNextPageClick, onPrevPageClick)
            if (totalPages > 0 && (pageNumber + 1 > totalPages)){
                pageNumber = 0
                fillTable()
            }
            setupElements()
        })
}

function setupElements(){
    $tableBody.find('select').each((i, select) => {
        select = $(select)
        setupSimpleSelect2(select, '', select.parent())
    })
}

function addElementsToTable(elements){
    let html = ''

    if (elements.length === 0){
        html = `<tr>
                    <td colspan="6" class="align-middle text-center no-data">NoData</td>
                </tr>`
    }

    $(elements).each((index, element)=> {
        html +=
            `
            <tr>
                <td style="width: 20%"><span class="fw-semibold">${element.firstName + ' ' + element.lastName}</span></td>
                <td style="width: 20%"><span class="fw-semibold">${element.email}</span></td>
                <td style="width: 20%"><span class="fw-semibold">${element.phone}</span></td>
                <td style="width: 20%"><span class="fw-semibold">${element.role}</span></td>
                <td style="width: 20%">
                    ${element.role !== 'ADMIN' ? 
                        `<select onchange="updateUserStatus(${element.id}, this)">
                            <option ${element.isEnabled ? 'selected' : ''} value="true">Active</option>
                            <option ${element.isEnabled ? '' : 'selected'} value="false">Disabled</option>
                        </select>`
                    : ``}
                    
                </td>
            </tr>`

    })
    $tableBody.html(html);
}

function updateUserStatus(userId, select){
    print(userId + ' '+ select.value)
    const form = {
        userId: userId,
        enabled: select.value
    }
    sendRequest(
        '/users/update-status',
        'POST',
        form,
        (data) => {
            fillTable()
        },
        (data) => {
            fillTable()
        })
}

function filterNameInput(){
    clearInterval(filterTimeout)
    filterTimeout = setTimeout(() => {
        pageNumber = 0
        fillTable()
    }, 500)
}