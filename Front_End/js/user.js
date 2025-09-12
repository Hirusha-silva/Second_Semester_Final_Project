$(document).ready(function (){
    const userId = localStorage.getItem("userId");
    console.log(userId);

    const token = localStorage.getItem("token");
    console.log(token)

    loadCategories();
    loadBrandModels();
    loadAllActiveAds();

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

    //all active ads load
    function loadAllActiveAds() {
        $.ajax({
            url: "http://localhost:8080/api/ads/active",
            method: "GET",
            headers: { Authorization: "Bearer " + token },
            success: function (ads) {
                const container = $("#adsContainer");
                container.empty();

                ads.forEach(ad => {
                    const firstPhoto = ad.photoUrls.length > 0 ? ad.photoUrls[0] : 'placeholder.jpg';
                    const adCard = `
                    <div class="col-md-4">
                        <div class="ad-card card">
                            <img src="${firstPhoto}" class="card-img-top" alt="${ad.title}">
                            <div class="card-body">
                                <h5>${ad.title}</h5>
                                <p>Price: Rs. ${ad.price}</p>
                                <p>Location: ${ad.location}</p>
                                <button class="btn btn-primary view-ad" data-id="${ad.adId}">View</button>
                            </div>
                        </div>
                    </div>
                `;
                    container.append(adCard);
                });
            },
            error: function () {
                console.error("Failed to load active ads");
            }
        });
    }

    $(document).on("click", ".view-ad", function() {
        const adId = $(this).data("id");
        openActivePopup(adId);
    });

 //open active ads popup window
    function openActivePopup(adId){
        $.ajax({
            url:`http://localhost:8080/api/ads/active/${adId}`,
            method:"GET",
            headers: { Authorization: "Bearer " + token },
            success:function (ad){

                $("#activeAdTitle").text(ad.title);
                $("#activeAdPrice").text(ad.price);
                $("#activeAdLocation").text(ad.location);
                $("#activeAdCategory").text(ad.categoryName);
                $("#activeAdBrand").text(ad.brand);
                $("#activeAdModel").text(ad.model);
                $("#activeAdDescription").text(ad.description);
                $("#activeAdSellerName").text(ad.name);
                $("#activeAdSellerEmail").text(ad.email);
                $("#activeAdSellerPhone").text(ad.phone);

                // Carousel
                const carouselInner = $("#activeAdCarouselInner");
                carouselInner.empty();
                ad.photos.forEach((photo, index) => {
                    const activeClass = index === 0 ? "active" : "";
                    carouselInner.append(`
                    <div class="carousel-item ${activeClass}">
                        <img src="${photo}" class="d-block w-100" alt="Ad Photo">
                    </div>
                `);
                });

                // Show modal
                const modalEl = document.getElementById('activeAdModal');
                const modal = new bootstrap.Modal(modalEl);
                modal.show();
            },
            error:function (xhr){
                new Noty({
                    type: "error",
                    layout: "topRight",
                    text: "Failed to load ads details ! " + xhr.responseText,
                    timeout: 3000
                }).show();
            }
        })
    }

})