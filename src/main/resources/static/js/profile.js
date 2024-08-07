document.addEventListener('DOMContentLoaded', function () {
    const editProfileButton = document.getElementById('editProfileButton');
    const editProfileForm = document.getElementById('editProfileForm');

    function toggleEditForm(){
        if (editProfileForm.style.display === "none") {
            editProfileForm.style.display = 'block';
        } else {
            editProfileForm.style.display = 'none';
        }
    }
    if (editProfileButton != null) editProfileButton.addEventListener("click", toggleEditForm);

    const showFollowersButton = document.getElementById('showFollowersButton');
    const showFollowingButton = document.getElementById('showFollowingButton');
    const followersForm = document.getElementById('followersForm');
    const followingForm = document.getElementById('followingForm');

    function showFollowers(){
        if (followersForm.style.display === 'block') {
            followersForm.style.display = 'none';
        } else {
            followersForm.style.display = 'block';
            followingForm.style.display = 'none';
        }
    }
    function showFollowing(){
        if (followingForm.style.display === 'block') {
            followingForm.style.display = 'none';
        } else {
            followingForm.style.display = 'block';
            followersForm.style.display = 'none';
        }
    }
    showFollowersButton.addEventListener('click', showFollowers);
    showFollowingButton.addEventListener('click', showFollowing);
});