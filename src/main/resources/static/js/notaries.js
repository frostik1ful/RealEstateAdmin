const $tableBody = $('#users-table-body')
const $paginationBlock = $('#paginationBlock')
const $searchInput = $('#search-input')
const $deleteModal = $('#delete-modal')
const $editModal = $('#edit-modal')
const $editFirstNameInput = $('#edit-first-name')
const $editLastNameInput = $('#edit-last-name')
const $editPhoneInput = $('#edit-phone')
const $editEmailInput = $('#edit-email')

const $editFirstError = $('#edit-first-error')
const $editLastError = $('#edit-last-error')
const $editPhoneError = $('#edit-phone-error')
const $editEmailError = $('#edit-email-error')

let pageNumber = 0
let totalPages = 1
let numberOfElements = 0
let totalElements = 0

let filterTimeout
let notaryId

const userIdUserMap = new Map()
initPage()
function initPage(){
    removeErrorClassOnInput($editFirstNameInput)
    removeErrorClassOnInput($editLastNameInput)
    removeErrorClassOnInput($editPhoneInput)
    removeErrorClassOnInput($editEmailInput)
    fillTable()
}
function fillTable(){
    const form = {
        search : $searchInput.val(),
        pageNumber
    }
    sendRequest(
        '/users/get-all-notaries',
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
    userIdUserMap.clear()
    let html = ''

    if (elements.length === 0){
        html = `<tr>
                    <td colspan="6" class="align-middle text-center no-data">NoData</td>
                </tr>`
    }

    $(elements).each((index, element)=> {
        userIdUserMap.set(element.id, element)
        html +=
            `
            <tr>
                <td style="width: 20%"><span class="fw-semibold">${element.firstName + ' ' + element.lastName}</span></td>
                <td style="width: 20%"><span class="fw-semibold">${element.email}</span></td>
                <td style="width: 20%"><span class="fw-semibold">${element.phone}</span></td>
                <td style="width: 20%"><span class="fw-semibold">${element.role}</span></td>
                <td style="width: 20%">
                    <div class="d-flex justify-content-around">
                        <a type="button" style="border: 0; background-color: transparent" class="pr-1" onclick="toggleEditNotaryModal(${element.id})">
                            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-edit m-50 text-warning">
                                <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                                <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                            </svg>
                        </a>
                        <a type="button" style="border: 0; background-color: transparent" onclick="toggleDeleteNotaryModal(${element.id})">
                            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-trash-2 m-50 text-danger">
                                <polyline points="3 6 5 6 21 6"></polyline>
                                <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                                <line x1="10" y1="11" x2="10" y2="17"></line>
                                <line x1="14" y1="11" x2="14" y2="17"></line>
                            </svg>
                        </a>
                    </div>
                    
                </td>
            </tr>`

    })
    $tableBody.html(html);
}

function toggleEditNotaryModal(id){
    notaryId = id
    const notary = userIdUserMap.get(id)
    print(notary)
    removeErrorClass($editFirstNameInput)
    removeErrorClass($editLastNameInput)
    removeErrorClass($editPhoneInput)
    removeErrorClass($editEmailInput)
    hide($editFirstError)
    hide($editLastError)
    hide($editPhoneError)
    hide($editEmailError)
    $editFirstNameInput.val(notary.firstName)
    $editLastNameInput.val(notary.lastName)
    $editPhoneInput.val(notary.phone)
    $editEmailInput.val(notary.email)
    toggleModal($editModal)
}

function toggleDeleteNotaryModal(id){
    notaryId = id
    toggleModal($deleteModal)
}

function tryToDeleteNotary(){
    sendRequest(
        '/users/notary/'+notaryId,
        'DELETE',
        null,
        (data)=>{
            toggleModal($deleteModal)
            fillTable()
        }
    )
}

function tryToUpdateNotary(){
    const form = {
        userId: notaryId,
        firstName: $editFirstNameInput.val(),
        lastName: $editLastNameInput.val(),
        phone: $editPhoneInput.val(),
        email: $editEmailInput.val()
    }
    sendRequest(
        '/users/notary',
        'PUT',
        form,
        (data)=>{
            toggleModal($editModal)
            fillTable()
        },
        (data) => {
            const map = new Map(Object.entries(data.responseJSON))
            print(map)
            toggleError('firstName', $editFirstNameInput, map)
            toggleError('lastName', $editLastNameInput, map)
            toggleError('phone', $editPhoneInput, map)
            toggleError('email', $editEmailInput, map)
        }
    )
}

function filterNameInput(){
    clearInterval(filterTimeout)
    filterTimeout = setTimeout(() => {
        pageNumber = 0
        fillTable()
    }, 500)
}