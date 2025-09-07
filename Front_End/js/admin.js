$(document).ready(function () {

    function showSection(sectionId) {
        document.querySelectorAll("section").forEach(sec => sec.style.display = "none");
        const el = document.getElementById(sectionId);
        if (el) el.style.display = "block";

        document.querySelectorAll(".sidebar a").forEach(link => link.classList.remove("active"));
        const activeLink = document.querySelector(`.sidebar a[data-section="${sectionId}"]`);
        if (activeLink) activeLink.classList.add("active");
    }

    function loadUsers() {
        const token = localStorage.getItem("token");
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
                }
                new Noty({
                    type: "success",
                    layout: "topRight",
                    text: "Load User Detail Success",
                    timeout: 3000
                }).show();
            },
            error: function () {
                new Noty({
                    type: "error",
                    layout: "topRight",
                    text: "Fail Data Load !",
                    timeout: 3000
                }).show();
            }
        });
    }

    function loadStatusCards() {
        const cards = [
            { title: "Total Users", value: 120 },
            { title: "Active Ads", value: 85 },
            { title: "Pending Ads", value: 15 },
            { title: "Total Listings", value: 230 }
        ];
        let html = '';
        cards.forEach(c => {
            html += `<div class="status-card"><h3>${c.value}</h3><p>${c.title}</p></div>`;
        });
        $(".status-cards").html(html);
    }

    document.querySelectorAll(".sidebar a").forEach(link => {
        const sectionId = link.getAttribute("data-section");
        link.addEventListener("click", function (e) {
            e.preventDefault();
            showSection(sectionId);

            if(sectionId === "dashboard") {
                loadUsers();
                loadStatusCards();
            }
        });
    });

    // Dashboard default load
    showSection("dashboard");
    loadUsers();
    loadStatusCards();
});
