const baseURL = "http://localhost:8080/";
const imgSearchURL = baseURL + "api/v1/img/search/";
const fileDirURL = baseURL + "file/img/";


let myAxios = axios.create({
    baseURL: window.baseURL,
    timeout: 1000,
    headers: {'X-Requested-With': 'XMLHttpRequest'}
});

let vm1 = new Vue({
        el: '#app1',
        data() {
            return {
                content: null,
                info: null
            }
        },

        mounted() {
            myAxios
                .get(baseURL + 'api/img/detail/93')
                .then(response => (this.content = response))
                .catch(error => {
                    console.log(error)
                    this.errored = true
                })
                .finally(() => this.loading = false)
        }
    }
)

let vm2 = new Vue({
    el: '#app2',
    data: {
        content: null,
        imgs: []
    },

    mounted() {
        myAxios
            .get(imgSearchURL,{
                params:
                    {tag: "狗"}
            })
            .then(response => {
                this.content = response
                console.log(this.content)
                for (let item of this.content) {
                    this.imgs.push(fileDirURL + item.subDir + item.name)
                    // console.log(fileDirURL + item.subDir + item.name)
                }
                // console.log(this.imgs)
            })
            .catch(error => {
                console.log(error)
                this.errored = true
            })
            .finally(() => this.loading = false)
    }
})

