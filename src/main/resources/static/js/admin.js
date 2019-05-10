import {myAxios} from "./my.js";


new Vue({
    el: '#app',
    data: {
        content: null,
        contentList: [],
        totalPages: null,
        currentPage: 1,
        lastPage: null,
        targetPage: null,
        user_class: {active: true},
        img_class: {active: false},
        url: null,
        adminUserQueryUrl: "api/v1/admin/user/query",
        adminImgQueryUrl: "api/v1/admin/img/query",
        flagUp: false,
        flagDown: true
    },
    methods: {
        shift_user() {
            this.user_class.active = true
            this.img_class.active = false
            this.currentPage = 1
            myAxios
                .get(this.adminUserQueryUrl)
                .then(response => {
                    this.contentList = response.data.content
                    this.totalPages = response.data.totalPages
                    // let [...contentListCopy] = this.contentList
                    //
                    // console.log(contentListCopy)
                    // console.log(this.contentList)
                    // for (let i in this.contentList) {
                    //     this.contentList[i].roles = null
                    //         console.log(contentListCopy[i])
                    //     for (let role of contentListCopy[i].roles) {
                    //         console.log(1)
                    //         this.contentList[i].roles.push(role.name)
                    //     }
                    // }
                })
                .catch(error => {
                    console.log(error)
                    this.errored = true
                })
                .finally(() => this.loading = false)
        },

        shift_img() {
            this.user_class.active = false
            this.img_class.active = true
            this.currentPage = 1
            myAxios
                .get(this.adminImgQueryUrl)
                .then(response => {
                    this.contentList = response.data.content
                    this.totalPages = response.data.totalPages
                })
                .catch(error => {
                    console.log(error)
                    this.errored = true
                })
                .finally(() => this.loading = false)
        },

        paging(pageNum) {
            let targetPage
            let el = event.currentTarget
            this.lastPage = this.currentPage

            if (this.user_class.active === true) {
                this.url = this.adminUserQueryUrl
            } else if (this.img_class.active === true) {
                this.url = this.adminImgQueryUrl
            }

            if (el.id === "pageDown") {
                targetPage = this.currentPage
                this.currentPage++
            } else if (el.id === "pageUp") {
                targetPage = this.currentPage - 2
                this.currentPage--
            } else { //否则则是普通页码元素
                targetPage = pageNum - 1
                this.currentPage = pageNum
            }

            // //设置首页与最后一页不可点样式
            // this.currentPageEl.parentElement.classList.remove("active")
            // console.log(this.currentPageEl)
            // console.log(el)
            // if (this.currentPage === 0) {
            //     el.parentElement.classList.add("disabled")
            // } else if (this.currentPage === this.totalPages) {
            //     el.parentElement.classList.add("disabled")
            // } else {
            //     el.parentElement.classList.add("active")
            //     el.parentElement.parentElement.lastElementChild.classList.remove("disabled","active")
            // }
            // this.currentPageEl = el

            myAxios
                .get(this.url, {
                    params: {
                        page: targetPage
                    }
                })
                .then(response => {
                    this.contentList = response.data.content
                    this.totalPages = response.data.totalPages
                })
                .catch(error => {
                    console.log(error)
                    this.errored = true
                })
                .finally(() => this.loading = false)
        },

        pageHighlight() {
            //设置当前页码高亮,lastPage用来清除上次的页码高亮
            // console.log(this.totalPages)
            if (this.lastPage != null) { //初次载入时lastPage为null，此时支持语句会报错
                document.getElementsByClassName("page-num-item")[this.lastPage - 1].classList.remove("active")
            }
            document.getElementsByClassName("page-num-item")[this.currentPage - 1].classList.add("active") //这里不要else
            //设置当页面为首页或末页时，上翻页或下翻页不可点击
            if (this.currentPage === 1 && this.currentPage === this.totalPages) { //既为首页又为末页时，即只有一页的情况
                this.flagUp = false
                this.flagDown = false
                document.getElementById("pageUp").parentElement.classList.add("disabled")
                document.getElementById("pageDown").parentElement.classList.add("disabled")
            } else if (this.currentPage === 1) { //当为首页时
                document.getElementById("pageUp").parentElement.classList.add("disabled")
                this.flagUp = false
            } else if (this.currentPage === this.totalPages) { //当为末页时
                document.getElementById("pageDown").parentElement.classList.add("disabled")
                this.flagDown = false
            } else {
                this.flagUp = true
                this.flagDown = true
                document.getElementById("pageUp").parentElement.classList.remove("disabled")
                document.getElementById("pageDown").parentElement.classList.remove("disabled")
            }
        }
    },

    mounted() {
        this.shift_user()

    },
    updated() {
        this.pageHighlight()
    }
})
