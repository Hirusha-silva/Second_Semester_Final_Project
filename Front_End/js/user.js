$(document).ready(function (){
    loadCategories();
    loadBrandModels();

    const userId = localStorage.getItem("userId");
    console.log(userId);

    const token = localStorage.getItem("token");
    console.log(token)





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
                    $categories.append(`<option value="${cat.categoryId}">${cat.name}</option>`);
                    console.log(cat.id,cat.name)
                })
            }
        })
    }

   // load models and brand
    function loadBrandModels() {
        $.ajax({
            url: 'http://localhost:8080/api/ads/models',
            method: 'GET',
            headers: {
                Authorization: "Bearer " + localStorage.getItem("token")
            },
            success: function(bms) {
                const $brand = $("#brand");
                const $model = $("#model");


                $brand.empty().append('<option value="">Select Brand</option>');
                $model.empty().append('<option value="">Select Model</option>');

                const brands = [...new Set(bms.map(bm => bm.brand))];

                brands.forEach(brand => {
                    $brand.append(`<option value="${brand}">${brand}</option>`);
                });


                $brand.on("change", function() {
                    const selectedBrand = $(this).val();
                    $model.empty().append('<option value="">Select Model</option>');

                    const models = bms.filter(bm => bm.brand === selectedBrand);
                    console.log(models);
                    models.forEach(m => {
                        $model.append(`<option value="${m.modelId}">${m.model}</option>`);

                    });
                });
            },
            error: function() {
                alert("Failed to load brand models");
            }
        });
    }

   // open post ad
    $('#postAD').on('click', function() {
        e.preventDefault();
        console.log(userId);

        var modalEl = document.getElementById('postAdModal');
        var modal = new bootstrap.Modal(modalEl);
        modal.show();

        // Move focus to first input/button after modal fully visible
        modalEl.addEventListener('shown.bs.modal', function () {
            const firstFocusable = modalEl.querySelector('input, select, textarea, button');
            if(firstFocusable) firstFocusable.focus();
        }, { once: true });
    });

   // post add
    $("#postAdData").on("click", function (e) {

        // Prepare DTO
        let adData = {
            title: $("#title").val(),
            description: $("#description").val(),
            price: parseFloat($("#price").val()),
            location: $("#location").val(),
            userId: parseInt(userId),
            categoryId: parseInt($("#category").val()),
            modelId: parseInt($("#model").val())
        };

        let formData = new FormData();
        formData.append("ad", JSON.stringify(adData));

        // Append photos
        const files = $("#photos")[0].files;
        for (let i = 0; i < files.length; i++) {
            formData.append("photos", files[i]);
        }

        $.ajax({
            url: "http://localhost:8080/api/ads",
            method: "POST",
            data: formData,
            processData: false,
            contentType: false,
            headers: { Authorization: "Bearer " + token },
            success: function (res) {
                new Noty({
                    type: "success",
                    layout: "topRight",
                    text: "saved",
                    timeout: 3000
                }).show();
                $("#adForm")[0].reset();
                $("#postAdModal").modal("hide");
            },
            error: function (xhr) {
                new Noty({
                    type: "error",
                    layout: "topRight",
                    text: "Not Save",
                    timeout: 3000
                }).show();
            }
        });
    });





})