import { myAxios, imgBaseUrl } from "./my.js";

new Vue({
  el: "#app1",
  data: {
    name: "123",
    file: null,
    filename: null,
    info: null,
    result: {
      msg: null,
      tag: null
    },
    relatedImgs: []
  },
  methods: {
    getFile(event) {
      this.file = event.target.files[0];
      console.log(this.file);
      this.filename = this.file.name;
    },
    submit(event) {
      event.preventDefault();
      let formData = new FormData();
      formData.append("file", this.file);

      let config = {
        headers: {
          "Content-Type": "multipart/form-data"
        }
      };

      myAxios
        .post("api/v1/img/upload", formData, config)
        .then(response => {
          console.log(response.data);
          // this.info = response.data;
          this.result = response.data;
          this.relatedImgs = []; //清空上次的结果
          for (let item of this.result.relatedImgs) {
            this.relatedImgs.push(imgBaseUrl + item.subDir + item.sourcename);
          }
        })
        .catch(error => {
          console.log(error);
          this.errored = true;
        })
        .finally(() => (this.loading = false));
    }
  }
});
