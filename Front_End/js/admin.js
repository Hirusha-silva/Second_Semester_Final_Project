$(document).ready(function () {
    const token = localStorage.getItem("token");

    // ===== SHOW SECTION =====
    function showSection(sectionId) {
        $("section").hide();           // hide all sections
        $("#" + sectionId).show();     // show the selected section
        $(".sidebar a").removeClass("active");
        $(`.sidebar a[data-section="${sectionId}"]`).addClass("active");
    }

    // ===== LOAD USERS TABLE =====
    function loadUsers() {
        $.ajax({
            url: "http://localhost:8080/admin/users",
            method: "GET",
            headers: { Authorization: "Bearer " + token },
            success: function (response) {
                if (response && response.status === 200) {
                    const users = response.data;
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
                    new Noty({ type: "success", layout: "topRight", text: "Users loaded", timeout: 2000 }).show();
                }
            },
            error: function () {
                new Noty({ type: "error", layout: "topRight", text: "Failed to load users", timeout: 3000 }).show();
            }
        });
    }

    // ===== LOAD STATUS CARDS =====
    function loadStatusCards() {
        $.ajax({
            url: "http://localhost:8080/admin/status-cards", // optional endpoint if dynamic
            method: "GET",
            headers: { Authorization: "Bearer " + token },
            success: function (response) {
                // example: response = { totalUsers:120, activeAds:85, pendingAds:15, totalListings:230 }
                const data = response?.data || { totalUsers:120, activeAds:85, pendingAds:15, totalListings:230 };
                const cardsHtml = `
                    <div class="status-card"><h3>${data.totalUsers}</h3><p>Total Users</p></div>
                    <div class="status-card"><h3>${data.activeAds}</h3><p>Active Ads</p></div>
                    <div class="status-card"><h3>${data.pendingAds}</h3><p>Pending Ads</p></div>
                    <div class="status-card"><h3>${data.totalListings}</h3><p>Total Listings</p></div>
                `;
                $(".status-cards").html(cardsHtml);
            },
            error: function () {
                console.log("Failed to load status cards, using defaults");
            }
        });
    }

    // ===== LOAD PENDING ADS =====
    function loadPendingAds() {
        $.ajax({
            url: "http://localhost:8080/admin/pending-ads",
            method: "GET",
            headers: { Authorization: "Bearer " + token },
            success: function (response) {
                if (response && response.status === 200) {
                    const ads = response.data;
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
                                    <button class="btn btn-sm btn-mail">Activate</button>
                                    <button class="btn btn-sm btn-delete">Delete</button>
                                </td>
                            </tr>
                        `);
                    });
                    new Noty({ type: "success", layout: "topRight", text: "Pending ads loaded", timeout: 2000 }).show();
                }
            },
            error: function () {
                new Noty({ type: "error", layout: "topRight", text: "Failed to load pending ads", timeout: 3000 }).show();
            }
        });
    }

    // ===== SIDEBAR CLICK EVENTS =====
    $(".sidebar a").click(function (e) {
        e.preventDefault();
        const sectionId = $(this).data("section");
        showSection(sectionId);

        if (sectionId === "dashboard") {
            loadUsers();
            loadStatusCards();
        } else if (sectionId === "pending-ads") {
            loadPendingAds();
        }
    });

    // ===== INITIAL LOAD =====
    showSection("dashboard");
    loadUsers();
    loadStatusCards();
});
