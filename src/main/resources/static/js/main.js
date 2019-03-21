const baseURL = "http://localhost:8080/";
const imgSearchURL = baseURL + "api/img/search/";
const fileDirURL = baseURL + "file/img/";
const test = "D:\\tests\\video";

var app1 = new Vue({
        el: '#app1',
        data: {
            info: null
        },

        mounted() {
            axios
                .get(baseURL + 'api/img/detail/93')
                .then(response => (this.info = response.data))
        }
    }
);

var app2 = new Vue({
    el: '#app2',
    data: {
        info: null,
        imgs: []
    },

    mounted() {
        axios
            .get(imgSearchURL, {
                params:
                    {tag: "狗"}
            })
            .then(response => {
                this.info = response.data;
                for (let item of this.info) {
                    this.imgs.push(fileDirURL + item.subDir + item.name)
                    console.log(fileDirURL + item.subDir + item.name)
                }
                console.log(this.imgs)
            })
    }
});

