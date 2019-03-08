const baseUrl = "http://localhost:8080/";

var app = new Vue({
    el: '#app',
    data :{
        info: null
    },

    mounted () {
        axios
            .get(baseUrl+'api/json/img/34')
            .then(response => (this.info = response.data))
        }
    }
);