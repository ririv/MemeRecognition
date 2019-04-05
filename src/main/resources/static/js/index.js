const baseURL = "http://localhost:8080/";
const imgSearchURL = baseURL + "api/img/search/";
const fileDirURL = baseURL + "file/img/";



new Vue({
    el:"#app1",
    data: {
        name:'123',
        file: '',
        info:null,
        result:{
            msg:null,
            tag:null
        },
        relatedImgs:[],
    },
    methods: {
        getFile(event) {
            this.file = event.target.files[0];
            console.log(this.file);
        },
        submit(event) {
            event.preventDefault();
            let formData = new FormData();
            formData.append('file', this.file);

            let config = {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            };

            abc
                .post("api/img/upload", formData, config)
                .then(response => {
                    console.log(response.data);
                    // this.info = response.data;
                    this.result = response.data;
                    for(let item of this.result.relatedImgs) {
                        this.relatedImgs.push(fileDirURL + item.subDir + item.name)
                    }
            })
        }
    },
})

