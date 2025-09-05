$(document).ready(function (){

    $('#postAdModal').on('shown.bs.modal', function () {
        // modal fully visible → set focus
        const firstFocusable = this.querySelector('input, select, textarea, button');
        if (firstFocusable) {
            firstFocusable.focus();
        }
    });
    // Load categories
    function loadCategories() {
        $.ajax({
            url: "http://localhost:8080/api/ads/category",
            method: "GET",
            headers: {
                Authorization: "Bearer " + localStorage.getItem("token")
            },
            success: function (category) {
                const $categories = $("#category");
                $categories.empty().append('<option value="">Select Category</option>');
                category.forEach(cat => {
                    $categories.append(`<option value="${cat.id}">${cat.name}</option>`);
                    console.log(cat.id,cat.name)
                })
            }
        })
    }

    $('#postAD').on('click', function() {
        loadCategories();
        var modalEl = document.getElementById('postAdModal');
        var modal = new bootstrap.Modal(modalEl);
        modal.show();

        // Move focus to first input/button after modal fully visible
        modalEl.addEventListener('shown.bs.modal', function () {
            const firstFocusable = modalEl.querySelector('input, select, textarea, button');
            if(firstFocusable) firstFocusable.focus();
        }, { once: true });
    });
})