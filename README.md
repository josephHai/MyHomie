# myhomie app 开发文档
## 功能说明
    本app的功能包括登录注册功能，查看帖子列表及帖子详情，搜索功能等。
## 难点亮点
### 难点
1. 组件化过程中的文件配置
    > manifest文件合并冲突问题
2. 网络请求功能的封装
    > okhttp3,retorfit,fastjson
    
    > 支持单个参数，多个参数，携带参数的文件上传的方式
3. 本地文件的获取
    > 需要相机访问权限以及文件读写权限
4. fragment与activity之间数据传输的实现
    > 通过实现监听接口的方式实现
5. 模块间跳转
    > ARouter(资源冲突的问题)
6. 首页抽屉图片加载
    > MaterialDrawer
7. 
### 亮点
1. 组件化开发
2. 采用material design风格
    > 版本迁移至androidx
3. 与后台通信采用jwt方式，并通过拦截器携带token进行网络请求
4. 数据库orm的实现
5. fragment的使用
6. 适配器
    > BaseRecyclerViewAdapterHelper
7. 全局变量依赖Application以及单例设计模式实现
8. 登录与注册功能的实现采用了观察者模式
9. 首页下拉刷新，上拉加载的实现
    > EasyRefresh
