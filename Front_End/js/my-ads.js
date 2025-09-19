$(document).ready(function () {
    const userId = localStorage.getItem("userId");
    const token = localStorage.getItem("token");

    // Load My Ads
    function loadMyAds() {
        $.ajax({
            url: `http://localhost:8080/api/ads/my-ads/${userId}`,
            method: "GET",
            headers: { Authorization: "Bearer " + token },
            success: function (ads) {
                const container = $("#myAdsContainer");

                container.empty();
                if (ads.length === 0) {
                    container.append('<p class="text-center mt-3">No ads found</p>');
                } else {
                    ads.forEach(ad => {
                        const firstPhoto = ad.photos.length > 0 ? ad.photos[0] : '/images/default.png';
                        container.append(`
                            <div class="col-md-4">
                                <div class="ad-card position-relative card">
                                    <img src="${firstPhoto}" class="card-img-top" alt="${ad.title}">
                                    <div class="card-body">
                                        <h5>${ad.title}</h5>
                                        <h6>Ad Is - ${ad.status}</h6>          
                                        <p>Rs. ${ad.price}</p>
                                        <p>${ad.location}</p>
                                        <div class="d-flex justify-content-end gap-2">
                                            <button class="btn btn-warning btn-sm edit-btn" data-id="${ad.adId}">Edit</button>
                                            <button class="btn btn-danger btn-sm delete-btn" data-id="${ad.adId}">Delete</button>
                                            <button class="btn btn-primary view-ads" data-id="${ad.adId}">View</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        `);
                    });
                }
            },
            error: function (err) {
                console.error(err);
                alert("Failed to load My Ads");
            }
        });
    }
    loadMyAds();

    // Open View Modal
    $(document).on("click", ".view-ads", function () {
        const adId = $(this).data("id");
        openUserAdsPopup(adId);
    });

    function openUserAdsPopup(adId) {
        $.ajax({
            url: `http://localhost:8080/api/ads/active/${adId}`,
            method: "GET",
            headers: { Authorization: "Bearer " + token },
            success: function (ad) {
                $("#userAdTitle").text(ad.title);
                $("#userAdPrice").text(ad.price);
                $("#userAdLocation").text(ad.location);
                $("#userAdCategory").text(ad.categoryName);
                $("#userAdBrand").text(ad.brand);
                $("#userAdModel").text(ad.model);
                $("#userAdDescription").text(ad.description);
                $("#userAdSellerName").text(ad.name);
                $("#userAdSellerEmail").text(ad.email);
                $("#userAdSellerPhone").text(ad.phone);


                console.log(ad)
                // Carousel
                const carouselInner = $("#userAdCarouselInner");
                carouselInner.empty();
                ad.photos.forEach((photo, index) => {
                    const activeClass = index === 0 ? "active" : "";
                    carouselInner.append(`
                        <div class="carousel-item ${activeClass}">
                            <img src="${photo}" class="d-block w-100" alt="Ad Photo">
                        </div>
                    `);
                });

                // Fill form for edit
                $("#updateAdId").val(ad.adId);
                $("#updateTitle").val(ad.title);
                $("#updateDescription").val(ad.description);
                $("#updatePrice").val(ad.price);
                $("#updateLocation").val(ad.location);
                $("#updateCategory").val(ad.categoryName); // make sure backend returns categoryId
                $("#updateBrand").val(ad.brand);
                $("#updateModel").val(ad.model);

                console.log(ad.adId)

                // Show modal
                const modalEl = document.getElementById('activeAdModal');
                const modal = new bootstrap.Modal(modalEl);
                modal.show();
            },
            error: function (xhr) {
                new Noty({
                    type: "error",
                    layout: "topRight",
                    text: "Failed to load ad details! " + xhr.responseText,
                    timeout: 3000
                }).show();
            }
        });
    }

    // Submit update form
    $("#updateAdForm").submit(function (e) {
        e.preventDefault();

        const adId = $("#updateAdId").val();

        let formData = new FormData();

        const adData = {
            title: $("#updateTitle").val(),
            description: $("#updateDescription").val(),
            price: $("#updatePrice").val(),
            location: $("#updateLocation").val(),
            categoryId:parseInt($("#category").val()) ,
            modelId: parseInt($("#model").val())
        };

        console.log(adData);
        formData.append("ad", new Blob([JSON.stringify(adData)], { type: "application/json" }));

        // Append photos if any
        let files = $("#updatePhotos")[0].files;
        for (let i = 0; i < files.length; i++) {
            formData.append("photos", files[i]);
        }

        $.ajax({
            url: `http://localhost:8080/api/ads/edit/${adId}`,
            method: "POST",
            headers: { Authorization: "Bearer " + token },
            data: formData,
            processData: false,
            contentType: false,
            success: function () {
                new Noty({
                    type: "success",
                    layout: "topRight",
                    text: "Ad updated successfully!",
                    timeout: 3000
                }).show();
                $('#activeAdModal').modal('hide');
                loadMyAds();
            },
            error: function (xhr) {
                new Noty({
                    type: "success",
                    layout: "topRight",
                    text: "Ad updated successfully!",
                    timeout: 3000
                }).show();
                $('#activeAdModal').modal('hide');
                loadMyAds();
            }
        });
    });

    // // Delete ad
    // $(document).on("click", ".delete-btn", function () {
    //     const adId = $(this).data("id");
    //     if (!confirm("Are you sure you want to delete this ad?")) return;
    //
    //     $.ajax({
    //         url: `http://localhost:8080/api/ads/delete/${adId}`,
    //         method: "DELETE",
    //         headers: { Authorization: "Bearer " + token },
    //         success: function () {
    //             new Noty({
    //                 type: "success",
    //                 layout: "topRight",
    //                 text: "Ad deleted successfully!",
    //                 timeout: 3000
    //             }).show();
    //             loadMyAds();
    //         },
    //         error: function (xhr) {
    //             new Noty({
    //                 type: "error",
    //                 layout: "topRight",
    //                 text: "Failed to delete ad! " + xhr.responseText,
    //                 timeout: 3000
    //             }).show();
    //         }
    //     });
    // });

});
