$(document).ready(function () {
    $.ajax({
        type: 'GET',
        url: '/admin/stores',
        success: function (response) {
            let dropdownRevenue = $('.dropdown-branch');
            const branchClass = 'branch';
            populateDropdown(branchClass, dropdownRevenue, response);
        },
        error: function (xhr, status, error) {
            console.error('Error fetching data:', error);
        }
    });

    const populateDropdown = function (className, tagName, data) {
        if (!Array.isArray(data)) {
            console.error('Response data is not an array:', data);
            return;
        }
        data.forEach(item => {
            const innerItem = `<li><a class="dropdown-item" onclick="selectOption('${className}', '${item}', '${className}')">${item}</a></li>`;
            tagName.append(innerItem);
        });
    };

    $.ajax({
        url: '/admin/genre',
        method: 'GET',
        success(data) {
            const dropdownGenre = $('.dropdown-genre');
            const genreClass = 'genre';
            populateDropdown(genreClass, dropdownGenre, data);
        },
    })


});
