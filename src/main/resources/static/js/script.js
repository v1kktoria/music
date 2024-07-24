document.addEventListener('DOMContentLoaded', function () {
    const usernameElement = document.getElementById('username');
    const username = usernameElement ? usernameElement.textContent : 'Guest';

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    const socket = new SockJS('/ws', null, {
        headers: {
            [csrfHeader]: csrfToken
        }
    });
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/comments', function (comment) {
            const commentData = JSON.parse(comment.body);
            const songId = commentData.songId;
            const commentSection = document.querySelector(`#song-${songId} .comment-content`);

            if (!commentSection) {
                console.error(`No comment section found for albumId: ${songId}`);
                return;
            }
            const commentElement = document.createElement('div');
            commentElement.className = 'comment';
            commentElement.innerHTML = `
                <p>${commentData.username}</p>
                <p>${commentData.content}</p>
                <p class="text-muted">${commentData.createdAt}</p>
            `;

            commentSection.insertBefore(commentElement, commentSection.firstChild);
            const container = document.querySelector(`#song-${songId}`);
            if (container) container.scrollTop = 0;
        });
    });

    document.querySelectorAll('.comment-form').forEach(form => {
        form.addEventListener('submit', function(event) {
            event.preventDefault();

            const formData = new FormData(this);
            stompClient.send("/app/comment", {}, JSON.stringify({
                username: username,
                content: formData.get('content'),
                songId: formData.get('songId')
            }));

            this.querySelector('textarea').value = '';
        });
    });
});
document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.comment-toggle').forEach(button => {
        button.addEventListener('click', function () {
            const commentSection = this.closest('.button-group').nextElementSibling;
            const content = commentSection.querySelector('.comment-content');

            if (content.style.display === 'none' || content.style.display === '') {
                content.style.display = 'block';
                this.textContent = 'Hide Comments';
            } else {
                content.style.display = 'none';
                this.textContent = 'Show Comments';
            }
        });
    });
});