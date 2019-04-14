import {myAxios,imgBaseUrl}  from "./my.js";


 new Vue({
    el: '#app',
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

