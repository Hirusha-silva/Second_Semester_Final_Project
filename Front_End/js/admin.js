$(document).ready(function () {
    const token = localStorage.getItem("token");
    let selectedAdId = null;

    function showSection(sectionId) {
        $("section").hide();
        $("#" + sectionId).show();
        $(".sidebar a").removeClass("active");
        $(`.sidebar a[data-section="${sectionId}"]`).addClass("active");
    }

    //load users
    function loadUsers() {
        $.ajax({
            url: "http://localhost:8080/admin/users",
            method: "GET",
            headers: { Authorization: "Bearer " + token },
            success: function (res) {
                new Noty({
                    type: "success",
                    layout: "topRight",
                    text: "Load users successfully",
                    timeout: 2000
                }).show();
                if (res.status === 200) {
                    const users = res.data;
                    $("#userTable tbody").empty();
                    users.forEach(user => {
                        $("#userTable tbody").append(`
                            <tr>
                                <td>${user.name}</td>
                                <td>${user.address || "-"}</td>
                                <td>${user.email}</td>
                                <td>${user.phone}</td>
                                <td>
                                    <button class="btn btn-sm btn-mail">Send Mail</button>
                                    <button class="btn btn-sm btn-delete">Delete</button>
                                </td>
                            </tr>
                        `);
                    });
                }
            },
            error:function (xhr){
                new Noty({
                    type: "error",
                    layout: "topRight",
                    text: "Failed to load users: " + xhr.responseText,
                    timeout: 2000
                }).show();
            }

        });
    }

   // load status cards
   //  function loadStatusCards() {
   //      const data = { totalUsers:120, activeAds:85, pendingAds:15, totalListings:230 };
   //      $(".status-cards").html(`
   //          <div class="status-card"><h3>${data.totalUsers}</h3><p>Total Users</p></div>
   //          <div class="status-card"><h3>${data.activeAds}</h3><p>Active Ads</p></div>
   //          <div class="status-card"><h3>${data.pendingAds}</h3><p>Pending Ads</p></div>
   //          <div class="status-card"><h3>${data.totalListings}</h3><p>Total Listings</p></div>
   //      `);
   //  }

   // load pending ads
    function loadPendingAds() {
        $.ajax({
            url: "http://localhost:8080/admin/pending-ads",
            method: "GET",
            headers: { Authorization: "Bearer " + token },
            success: function (res) {
                new Noty({
                    type: "success",
                    layout: "topRight",
                    text: "Load pending ads successfully!",
                    timeout: 2000
                }).show();
                if (res.status === 200) {
                    const ads = res.data;
                    $("#pendingAd tbody").empty();
                    ads.forEach(ad => {
                        $("#pendingAd tbody").append(`
                            <tr>
                                <td>${ad.id}</td>
                                <td>${ad.title}</td>
                                <td>${ad.description}</td>
                                <td>${ad.location}</td>
                                <td>${ad.price}</td>
                                <td>${ad.status}</td>
                                <td>${ad.username}</td>
                                <td>
                                    <button class="btn btn-sm btn-primary view-details" data-id="${ad.id}">View</button>
                                </td>
                            </tr>
                        `);
                    });
                }
            },
            error:function (xhr){
                new Noty({
                    type: "error",
                    layout: "topRight",
                    text: "Load pending ads fail: " + xhr.responseText,
                    timeout: 2000
                }).show();
            }

        });
    }

    //side bar
    $(".sidebar a").click(function (e) {
        e.preventDefault();
        const sectionId = $(this).data("section");
        showSection(sectionId);
        if (sectionId === "dashboard") { loadUsers(); loadStatusCards(); }
        if (sectionId === "pending-ads") loadPendingAds();
        if (sectionId === "active-ads") loadActiveAds();
    });

    //open pending Ad details model
    $(document).on("click", ".view-details", function () {
        selectedAdId = $(this).data("id");
        $.ajax({
            url: `http://localhost:8080/admin/pending-ads/${selectedAdId}`,
            method: "GET",
            headers: { Authorization: "Bearer " + token },
            success: function (res) {
                const ad = res.data;
                $("#modalTitle").text(ad.title);
                $("#modalDescription").text(ad.description);
                $("#modalLocation").text(ad.location);
                $("#modalPrice").text(ad.price + " LKR");
                $("#modalUser").text(ad.username);
                $("#modalEmail").text(ad.email);
                $("#modalPhone").text(ad.phone);

                // Carousel
                let html = "";
                let thumbs = "";
                ad.photos.forEach((url, i) => {
                    html += `<div class="carousel-item ${i===0?'active':''}"><img src="${url}" class="d-block w-100" alt="Ad Photo"></div>`;
                    thumbs += `<img src="${url}" class="${i===0?'active-thumb':''}" data-bs-slide-to="${i}" data-bs-target="#adPhotosCarousel">`;
                });
                $("#adPhotosCarouselInner").html(html);
                $("#carouselThumbnails").html(thumbs);

                // Thumbnail click
                $("#carouselThumbnails img").click(function(){
                    $("#carouselThumbnails img").removeClass("active-thumb");
                    $(this).addClass("active-thumb");
                    bootstrap.Carousel.getOrCreateInstance(document.getElementById('adPhotosCarousel')).to($(this).data("bs-slide-to"));
                });

                // Zoom on click
                $("#adPhotosCarouselInner img").off("click").on("click", function(){
                    const src = $(this).attr("src");
                    $("#zoomImg").attr("src", src);
                    var zoomModal = new bootstrap.Modal(document.getElementById('zoomModal'));
                    zoomModal.show();
                });

                bootstrap.Modal.getOrCreateInstance(document.getElementById('adDetailModal')).show();
            }
        });
    });


   // load active ads
    function loadActiveAds(){
        $.ajax({
            url:"http://localhost:8080/admin/active-ads",
            method:"GET",
            headers:{
                Authorization: "Bearer " + token
            },
            success:function (res){
                new Noty({
                    type: "success",
                    layout: "topRight",
                    text: "Load Active ads successfully!",
                    timeout: 2000
                }).show();

                if (res.status === 200){
                    const ads = res.data;
                    $("#activeAd tbody").empty();
                    ads.forEach(ad => {
                        $("#activeAd tbody").append(
                            `
                            <tr>
                                <td>${ad.id}</td>
                                <td>${ad.title}</td>
                                <td>${ad.description}</td>
                                <td>${ad.location}</td>
                                <td>${ad.price}</td>
                                <td>${ad.status}</td>
                                <td>${ad.username}</td>
                                <td>
                                    <button type="button" class="btn btn-outline-danger ActiveAd-view-details" data-id="${ad.id}">View</button>
                                </td>
                            </tr>
                            `
                        );
                    });
                }
            },
            error:function (xhr){
                new Noty({
                    type: "error",
                    layout: "topRight",
                    text: "Load active ads fail: " + xhr.responseText,
                    timeout: 2000
                }).show();
            }
        })
    }

    //active ad popup window
    $(document).on("click", ".ActiveAd-view-details", function() {
        selectedAdId = $(this).data("id");

        $.ajax({
            url: `http://localhost:8080/admin/active-ads/${selectedAdId}`,
            method: "GET",
            headers: { Authorization: "Bearer " + token },
            success: function(res) {
                const ad = res.data;

                $("#activeModalTitle").text(ad.title);
                $("#activeModalDescription").text(ad.description);
                $("#activeModalLocation").text(ad.location);
                $("#activeModalPrice").text(ad.price + " LKR");
                $("#activeModalUser").text(ad.username);
                $("#activeModalEmail").text(ad.email);
                $("#activeModalPhone").text(ad.phone);

                // Append new carousel items
                let html = "";
                let thumbs = "";
                ad.photos.forEach((url, i) => {
                    html += `<div class="carousel-item ${i===0 ? 'active' : ''}">
                            <img src="${url}" class="d-block w-100" alt="Ad Photo">
                         </div>`;
                    thumbs += `<img src="${url}" class="${i===0 ? 'active-thumb' : ''}"
                                data-bs-slide-to="${i}" data-bs-target="#activeAdPhotosCarousel">`;
                });
                $("#adPhotosCarouselInnerA").html(html);
                // $("#carouselThumbnailsA").html(thumbs);

                //Thumbnail click
                $("#carouselThumbnailsA img").click(function() {
                    $("#carouselThumbnailsA img").removeClass("active-thumb");
                    $(this).addClass("active-thumb");
                    bootstrap.Carousel.getOrCreateInstance(carouselEl).to($(this).data("bs-slide-to"));
                });

                 bootstrap.Modal.getOrCreateInstance(document.getElementById('activeAdDetailModal')).show();
            }
        });
    });

    //delete active ads
    $(document).on("click",".activeAdDelete-ad",function (){
        const adId = $(this).data("id") || selectedAdId;
        $.ajax({
            url: `http://localhost:8080/admin/active/delete/${adId}`,
            method: "DELETE",
            headers: { Authorization: "Bearer " + token },
            success:function (){
                const  modalEl = document.getElementById('activeAdDetailModal');
                const modalInstance = bootstrap.Modal.getInstance(modalEl);
                if (modalInstance) modalInstance.hide();
                new Noty({ type:"success", text:"Ad deleted!", timeout:2000 }).show();
                loadActiveAds();
            },
            error:function (xhr){
                new Noty({
                    type: "error",
                    layout: "topRight",
                    text: "Failed AD delete !" + xhr.responseText,
                    timeout: 2000
                }).show();
            }
        });
    });



    //ads active
    $(document).on("click", ".activate-ad", function () {
        const adId = $(this).data("id") || selectedAdId;
        $.ajax({
            url: `http://localhost:8080/admin/pending-ads/${adId}/activate`,
            method: "PUT",
            headers: { Authorization: "Bearer " + token },
            success: function () {
                new Noty({ type:"success", text:"Ad activated!", timeout:2000 }).show();

                const modalEl = document.getElementById('adDetailModal');
                const modalInstance = bootstrap.Modal.getOrCreateInstance(modalEl);
                modalInstance.hide();

                loadPendingAds();
            },
            error:function (xhr){
                new Noty({
                    type: "error",
                    layout: "topRight",
                    text: "Ad active fail !" + xhr.responseText,
                    timeout: 2000
                }).show();
            }
        });
    });

    //delete pending ads
    $(document).on("click", ".delete-ad", function () {
        const adId = $(this).data("id") || selectedAdId;
        $.ajax({
            url: `http://localhost:8080/admin/delete/${adId}`,
            method: "DELETE",
            headers: { Authorization: "Bearer " + token },
            success: function () {

                const modalEl = document.getElementById('adDetailModal');
                const modalInstance = bootstrap.Modal.getInstance(modalEl);
                if (modalInstance) modalInstance.hide();

                new Noty({ type:"success", text:"Ad deleted!", timeout:2000 }).show();
                loadPendingAds();
            },
            error:function (xhr){
                new Noty({
                    type: "error",
                    layout: "topRight",
                    text: "Failed AD delete !" + xhr.responseText,
                    timeout: 2000
                }).show();
            }
        });
    });

   // custom email send to user
    $(document).on("click",".btn-mail",function (){
        const userEmail = $(this).closest("tr").find("td:nth-child(3)").text();
        $("#mailTo").val(userEmail);
        $("#mailSubject").val("");
        $("#mailBody").val("");
        bootstrap.Modal.getOrCreateInstance(document.getElementById('sendMailModal')).show();
    });

    // Handle form submit
    $("#sendMailForm").submit(function (e) {
        e.preventDefault();

        const requestData = {
            to: $("#mailTo").val(),
            subject: $("#mailSubject").val(),
            body: $("#mailBody").val()
        };

        $.ajax({
            url: "http://localhost:8080/admin/send-mail",
            method: "POST",
            headers: { Authorization: "Bearer " + token },
            contentType: "application/json",
            data: JSON.stringify(requestData),
            success: function (res) {
                new Noty({
                    type: "success",
                    layout: "topRight",
                    text: "Mail sent successfully!",
                    timeout: 2000
                }).show();

                bootstrap.Modal.getInstance(document.getElementById('sendMailModal')).hide();
            },
            error: function (xhr) {
                new Noty({
                    type: "error",
                    layout: "topRight",
                    text: "Failed to send mail: " + xhr.responseText,
                    timeout: 3000
                }).show();
            }
        });
    });

    function loadStatusCards() {
        $.ajax({
            url: "http://localhost:8080/admin/status-cards",
            method: "GET",
            headers: { Authorization: "Bearer " + token },
            success: function (res) {
                if (res.status === 200) {
                    const data = res.data;
                    $(".status-cards").html(`
                    <div class="status-card" style="background: linear-gradient(135deg, #4facfe, #00f2fe);">
                        <div class="card-icon">👥</div>
                        <h3>${data.totalUsers}</h3>
                        <p>Total Users</p>
                    </div>
                    <div class="status-card" style="background: linear-gradient(135deg, #43e97b, #38f9d7);">
                        <div class="card-icon">📢</div>
                        <h3>${data.activeAds}</h3>
                        <p>Active Ads</p>
                    </div>
                    <div class="status-card" style="background: linear-gradient(135deg, #fa709a, #fee140);">
                        <div class="card-icon">⏳</div>
                        <h3>${data.pendingAds}</h3>
                        <p>Pending Ads</p>
                    </div>
                    <div class="status-card" style="background: linear-gradient(135deg, #a18cd1, #fbc2eb);">
                        <div class="card-icon">📄</div>
                        <h3>${data.totalListings}</h3>
                        <p>Total Listings</p>
                    </div>
                `);
                }
            },
            error: function (xhr) {
                new Noty({
                    type: "error",
                    layout: "topRight",
                    text: "Status cards load කිරීම අසාර්ථකයි: " + xhr.responseText,
                    timeout: 2000
                }).show();
            }
        });
    }





    // Initial load
    showSection("dashboard");
    loadUsers();
    loadStatusCards();
});


