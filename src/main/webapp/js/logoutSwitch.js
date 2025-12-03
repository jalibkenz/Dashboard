document.addEventListener('DOMContentLoaded', function() {
    const logoutSwitch = document.getElementById('logoutSwitch');
    if (logoutSwitch) {
        logoutSwitch.addEventListener('change', function() {
            if (this.checked) {
                const wrapper = this.closest('.logout-wrapper');
                wrapper.classList.add('logout-active');

                setTimeout(() => {
                    const form = document.createElement('form');
                    form.method = 'POST';
                    form.action = 'logout';  // Your servlet URL
                    document.body.appendChild(form);
                    form.submit();
                }, 500);
            }
        });
    }
});
