import {myAxios,imgBaseUrl}  from "./my.js";

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
                .get('api/v1/img/detail/93')
                .then(response => (this.content = response.data))
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
            .get("api/v1/img/search",{
                params:
                    {tag: "狗"}
            })
            .then(response => {
                this.content = response.data
                console.log(this.content)
                for (let item of this.content) {
                    this.imgs.push(imgBaseUrl + item.subDir + item.sourcename)
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

