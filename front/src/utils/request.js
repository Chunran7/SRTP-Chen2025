//定制请求的实例

//导入axios  npm install axios
import axios from 'axios';
//定义一个变量,记录公共的前缀  ,  baseURL
const baseURL = '/api';
const instance = axios.create({baseURL})


//添加响应拦截器
instance.interceptors.response.use(
    result=>{
        // 修复逻辑：当code为0时代表成功，否则代表失败
        if(result.data.code === 0){
           // 成功情况，返回数据
           return result.data;
        }else{
           // 失败情况，拒绝Promise，让业务代码处理错误提示
           return Promise.reject(result.data);
        }
    },
    err=>{
        return Promise.reject(err);//异步的状态转化成失败的状态
    }
)

export default instance;