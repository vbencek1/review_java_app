//W3 schools -> sliders

//Popular books slider
var slideIndex = 1;
showSlides(slideIndex);

function plusSlides(n) {
    showSlides(slideIndex += n);
}

function currentSlide(n) {
    showSlides(slideIndex = n);
}

function showSlides(n) {
    var i;
    var slides = document.getElementsByClassName("div-home-slide");
    var dots = document.getElementsByClassName("dot");
    if (n > slides.length) {
        slideIndex = 1;
    }
    if (n < 1) {
        slideIndex = slides.length;
    }
    for (i = 0; i < slides.length; i++) {
        slides[i].style.display = "none";
    }
    for (i = 0; i < dots.length; i++) {
        dots[i].className = dots[i].className.replace(" active", "");
    }
    slides[slideIndex - 1].style.display = "block";
    dots[slideIndex - 1].className += " active";
}

//Latest book review slider
var slideIndexLatest = 1;
showSlidesLatest(slideIndexLatest);

function plusSlidesLatest(n) {
    showSlidesLatest(slideIndexLatest += n);
}

function currentSlideLatest(n) {
    showSlidesLatest(slideIndexLatest = n);
}

function showSlidesLatest(n) {
    var i;
    var slides = document.getElementsByClassName("div-home-slide-latest");
    var dots = document.getElementsByClassName("dot-latest");
    if (n > slides.length) {
        slideIndexLatest = 1;
    }
    if (n < 1) {
        slideIndexLatest = slides.length;
    }
    for (i = 0; i < slides.length; i++) {
        slides[i].style.display = "none";
    }
    for (i = 0; i < dots.length; i++) {
        dots[i].className = dots[i].className.replace(" active", "");
    }
    slides[slideIndexLatest - 1].style.display = "block";
    dots[slideIndexLatest - 1].className += " active";
}

function showComments() {
    var x = document.getElementById("divComments");
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}

function showReviewForm() {
    var x = document.getElementById("divReviewForm");
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}
