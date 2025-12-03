document.addEventListener("DOMContentLoaded", function () {
    const logoutSwitch = document.getElementById("logoutSwitch");

    if (logoutSwitch) {
        logoutSwitch.addEventListener("change", function () {
            if (this.checked) {
                // Wait for the sliding animation, then logout
                setTimeout(() => {
                    window.location.href = "logout";
                }, 300);

                // Reset switch position
                setTimeout(() => {
                    this.checked = false;
                }, 700);
            }
        });
    }
});
