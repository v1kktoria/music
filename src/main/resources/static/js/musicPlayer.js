document.addEventListener('DOMContentLoaded', function () {
    const songElements = Array.from(document.querySelectorAll('.audio-player'));
    const playPauseButton = document.getElementById('play-pause-button');
    const prevButton = document.getElementById('prev-button');
    const nextButton = document.getElementById('next-button');
    const currentSongName = document.getElementById('current-song-name');
    const currentAlbumCover = document.getElementById('current-album-cover');
    const currentSongArtist = document.getElementById('current-song-artist');
    const repeatButton = document.getElementById('repeat-button');
    let isRepeating = false;
    currentAlbumCover.src = songElements[0].getAttribute('data-song-image');
    currentSongName.textContent = songElements[0].getAttribute('data-song-name');
    currentSongArtist.textContent = songElements[0].getAttribute('data-song-artist');
    let currentTrackIndex = -1;
    let isPlaying = false;

    songElements.forEach((audio, i) => {
        audio.addEventListener('play', function () {
            playAudio(this, i);
        });
    });

    songElements.forEach((audio, i) => {
        audio.addEventListener('pause', function () {
            isPlaying = false;
            updatePlayPauseIcon();
        });
    });

    repeatButton.addEventListener('click', function () {
        isRepeating = !isRepeating;
        updateRepeatIcon();
    });

    function playAudio(audio, index) {
        if (currentTrackIndex !== index && currentTrackIndex !== -1) {songElements[currentTrackIndex].pause();songElements[currentTrackIndex].currentTime = 0;}
        currentTrackIndex = index;
        isPlaying = true;
        updatePlayPauseIcon();
        currentAlbumCover.src = audio.getAttribute('data-song-image');
        currentSongName.textContent = audio.getAttribute('data-song-name');
        currentSongArtist.textContent = audio.getAttribute('data-song-artist');
    }

    function updatePlayPauseIcon() {
        const icon = document.getElementById('play-pause-icon');
        if (isPlaying) {
            icon.classList.remove('ph-play');
            icon.classList.add('ph-pause');
        } else {
            icon.classList.remove('ph-pause');
            icon.classList.add('ph-play');
        }
    }

    playPauseButton.addEventListener('click', function () {
        if (isPlaying) {
            songElements[currentTrackIndex].pause();
        } else {
            songElements[currentTrackIndex].play();
        }
    });

    prevButton.addEventListener('click', function () {
        songElements[currentTrackIndex].pause();
        songElements[currentTrackIndex].currentTime = 0;
        currentTrackIndex -= 1;
        if (currentTrackIndex < 0) currentTrackIndex = songElements.length - 1;
        songElements[currentTrackIndex].play();
    });

    nextButton.addEventListener('click', function () {
        songElements[currentTrackIndex].pause();
        songElements[currentTrackIndex].currentTime = 0;
        currentTrackIndex += 1;
        if (currentTrackIndex >= songElements.length) currentTrackIndex = 0;
        songElements[currentTrackIndex].play();
    });

    songElements.forEach((audio, index) => {
        audio.addEventListener('ended', function () {
            if (isRepeating) songElements[currentTrackIndex].play();
            else if (index < songElements.length - 1) {
                songElements[currentTrackIndex].pause();
                songElements[currentTrackIndex].currentTime = 0;
                currentTrackIndex += 1;
                songElements[currentTrackIndex].play();
            } else {
                songElements[currentTrackIndex].pause();
                songElements[currentTrackIndex].currentTime = 0;
                currentTrackIndex = 0;
                songElements[currentTrackIndex].play();
            }
        });
    });

    function updateRepeatIcon() {
        const icon = document.querySelector('#repeat-button .ph-repeat');
        if (isRepeating) {
            icon.classList.add('ph-repeat-on');
        } else {
            icon.classList.remove('ph-repeat-on')
        }
    }
});