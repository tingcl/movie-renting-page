var rad = document.myForm.searchFor;
var prev = null;
for (var i = 0; i < rad.length; i++) {
    rad[i].addEventListener('change', function() {
        if (this !== prev) {
            prev = this;
        }
        if(this.value == 'title'){
            $('.index-bar-query').attr('placeholder', 'Browse by title...')
        }
        else{
            $('.index-bar-query').attr('placeholder', 'Browse by genre...')
        }
    });
}