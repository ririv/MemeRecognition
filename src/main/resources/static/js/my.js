export const imgBaseURL = "file/img/"
export const baseURL =  "http://localhost:8080/"

export let myAxios = axios.create({
    baseURL,
    // timeout: 1000,
    headers: {'X-Requested-With': 'XMLHttpRequest'}
});
