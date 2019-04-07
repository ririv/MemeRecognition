export const imgBaseUrl = "file/img/"

export let myAxios = axios.create({
    baseURL: "http://localhost:8080/",
    // timeout: 1000,
    headers: {'X-Requested-With': 'XMLHttpRequest'}
});
