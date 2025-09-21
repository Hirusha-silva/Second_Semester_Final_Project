


$(document).ready(function (){
    const userId = localStorage.getItem("userId");
    const token = localStorage.getItem("token");


    console.log(token)
    console.log(userId)

    if (!userId || !token){
        window.location.href = "../index.html";
    }

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
                const $category = $("#searchCategory")

                $categories.empty().append('<option value="">Select Category</option>');
                $category.empty().append('<option value="">Select Category</option>');

                category.forEach(cat => {
                    $categories.append(`<option value="${cat.categoryId}">${cat.name}</option>`);
                    $category.append(`<option value="${cat.categoryId}">${cat.name}</option>`);
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

                const $sBrand = $("#searchBrand");
                const $sModels = $("#searchModel");


                $brand.empty().append('<option value="">Select Brand</option>');
                $model.empty().append('<option value="">Select Model</option>');

                $sBrand.empty().append('<option value="">Select Brand</option>');
                $sModels.empty().append('<option value="">Select Model</option>');


                const brands = [...new Set(bms.map(bm => bm.brand))];

                brands.forEach(brand => {
                    $brand.append(`<option value="${brand}">${brand}</option>`);
                    $sBrand.append(`<option value="${brand}">${brand}</option>`);

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

                $sBrand.on("change",function (){
                    const selectedBrand = $(this).val();
                    $sModels.empty().append('<option value="">Select Model</option>');

                    const models = bms.filter(bm => bm.brand === selectedBrand);
                    console.log(models);
                    models.forEach(m => {
                        $sModels.append(`<option value="${m.modelId}">${m.model}</option>`);

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
        e.preventDefault();
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
                    type: "success",
                    layout: "topRight",
                    text: " Saved !",
                    timeout: 3000
                }).show();
                $("#adForm")[0].reset();
                $("#postAdModal").modal("hide");
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
                    const isFavorite = ad.isFavorite ? "active" : "";
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
                            <button class="fav-btn ${isFavorite}" data-id="${ad.adId}">❤️</button>
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

    $(document).on("click", ".fav-btn", function () {
        const adId = $(this).data("id");
        const $btn = $(this);

        if ($btn.hasClass("active")) {
            $.ajax({
                url: `http://localhost:8080/api/ads/remove?userId=${userId}&adId=${adId}`,
                method: "DELETE",
                headers: { Authorization: "Bearer " + token },
                success: function () {
                    $btn.removeClass("active");
                    loadFavoriteAds();
                }
            });
        } else {
            $.ajax({
                url: `http://localhost:8080/api/ads/add?userId=${userId}&adId=${adId}`,
                method: "POST",
                headers: { Authorization: "Bearer " + token },
                success: function () {
                    $btn.addClass("active");
                    loadFavoriteAds();
                }
            });
        }
    });

    /* ===================== Load Favorite Ads ===================== */
    // function loadFavoriteAds() {
    //     $.ajax({
    //         url: `http://localhost:8080/api/favorites/${userId}`,
    //         method: "GET",
    //         headers: { Authorization: "Bearer " + token },
    //         success: function (ads) {
    //             const container = $("#favAdsContainer");
    //             container.empty();
    //             ads.forEach(ad => {
    //                 const firstPhoto = ad.photoUrls.length > 0 ? ad.photoUrls[0] : 'placeholder.jpg';
    //                 container.append(`
    //                     <div class="col-md-4 position-relative">
    //                         <div class="ad-card card">
    //                             <img src="${firstPhoto}" class="card-img-top" alt="${ad.title}">
    //                             <div class="card-body">
    //                                 <h5>${ad.title}</h5>
    //                                 <p>Price: Rs. ${ad.price}</p>
    //                                 <p>Location: ${ad.location}</p>
    //                                 <button class="btn btn-primary view-ad" data-id="${ad.adId}">View</button>
    //                             </div>
    //                             <button class="fav-btn active" data-id="${ad.adId}">❤️</button>
    //                         </div>
    //                     </div>
    //                 `);
    //             });
    //         }
    //     });
    // }



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


    //search ads
    $("#searchForm").on("submit", function(e) {
        e.preventDefault();

        let keyword = $("#searchKeyword").val();
        let categoryId = $("#searchCategory").val();
        let brand = $("#searchBrand").val();
        let modelId = $("#searchModel").val();
        let location = $("#searchLocation").val();

        $.ajax({
            url: "http://localhost:8080/api/ads/search",
            method: "GET",
            headers: { Authorization: "Bearer " + token },
            data: {
                keyword, categoryId, brand, modelId, location
            },
            success: function (ads) {
                const container = $("#adsContainer");
                container.empty();
                ads.forEach(ad => {
                    const firstPhoto = ad.photoUrls.length > 0 ? ad.photoUrls[0] : 'placeholder.jpg';
                    const isFavorite = ad.isFavorite ? "active" : "";
                    container.append(`
                    <div class="col-md-4">
                        <div class="ad-card card">
                            <img src="${firstPhoto}" class="card-img-top" alt="${ad.title}">
                            <div class="card-body">
                                <h5>${ad.title}</h5>
                                <p>Price: Rs. ${ad.price}</p>
                                <p>Location: ${ad.location}</p>
                                <button class="btn btn-primary view-ad" data-id="${ad.adId}">View</button>
                            </div>
                            <button class="fav-btn ${isFavorite}" data-id="${ad.adId}">❤️</button>
                        </div>
                    </div>
                `);
                });
            }
        });
    });

    $("#logoutBtnu").on("click", function() {
        if (!confirm("Are you sure you want to logout")) return;
        // Clear localStorage
        localStorage.removeItem("token");
        localStorage.removeItem("userId");

        // Optionally show notification
        new Noty({
            type: "success",
            layout: "topRight",
            text: "Logged out successfully!",
            timeout: 2000
        }).show();

        // Redirect to login page
        setTimeout(() => {
            window.location.href = "../index.html"; // adjust path to your login page
        }, 1000);
    });

})