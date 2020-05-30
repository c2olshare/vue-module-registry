## 微前端模块注册中心

### 演示环境
[https://vmr.itmajing.com](https://vmr.itmajing.com)  
用户名/密码: admin/vmr123

### 接口列表
- 模块查询  
URL: /api/v1/open/module/{code}  
Method：GET  
Response： 
```
{
    code: "00000",
    data: {
        id: "e508ad02bff94974a549c272e5c842f7",
        code: "demoComponent",
        name: "demoComponent",
        current: "1.0.1",
        namespace: "default",
        url: "http://vmr.itmajing.com:80/vmr/content/demo/1.0.1/demo.umd.min.js?namespace=default",
        createTime: "2020-05-30 20:32:32",
        updateTime: "2020-05-30 21:19:28"
    }
}
```

## 微前端模块加载器
[vue-module-loader](https://mqhe2007.github.io/vue-module-loader/)

## Vue-CLI构建目标
[构建目标](https://cli.vuejs.org/zh/guide/build-targets.html)