$(document).ready(function () {
    const userId = localStorage.getItem("userId");
    console.log(userId)
    const token = localStorage.getItem("token");

    console.log(token)
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
                                        <p>Rs. ${ad.price}</p>
                                        <p>${ad.location}</p>
                                        <div class="d-flex justify-content-end gap-2 ">
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

    $(document).on("click", ".view-ads", function() {
        const adId = $(this).data("id");
        openUserAdsPopup(adId);
    });

    function openUserAdsPopup(adId){
        $.ajax({
            url:`http://localhost:8080/api/ads/active/${adId}`,
            method:"GET",
            headers: { Authorization: "Bearer " + token },
            success:function (ad){

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
});
