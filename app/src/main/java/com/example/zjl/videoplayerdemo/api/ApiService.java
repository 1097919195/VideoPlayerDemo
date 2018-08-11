package com.example.zjl.videoplayerdemo.api;




import com.example.zjl.videoplayerdemo.bean.LoginTokenData;
import com.example.zjl.videoplayerdemo.bean.TestBean;
import com.example.zjl.videoplayerdemo.bean.VideoData;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * des:ApiService
 * Created by xsf
 * on 2016.06.15:47
 */
//        https://www.jianshu.com/p/7687365aa946
//@Path： URL中有参数,如：
//        http://102.10.10.132/api/Accounts/{accountId}
//@Query：参数在URL问号之后,如：
//        http://102.10.10.132/api/Comments ? access_token={access_token}
//@QueryMap：相当于多个@Query
//@Field：用于POST请求，提交单个数据（不显示在网址中）
//@Body： 相当于多个@Field，以对象的形式提交
// Tip1
//        使用@Field时记得添加@FormUrlEncoded
// Tip2
//        若需要重新定义接口地址，可以使用@Url，将地址以参数的形式传入即可
public interface ApiService {

//    @GET("login")
//    Observable<BaseRespose<User>> login(@Query("username") String username, @Query("password") String password);
//
//    //新闻详情
//    @GET("nc/article/{postId}/full.html")
//    Observable<Map<String, NewsDetail>> getNewDetail(
//            @Header("Cache-Control") String cacheControl,//添加响应头（缓存的方式）
//            @Path("postId") String postId);
//
//    //新闻列表
//    //http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html
//    @GET("nc/article/{type}/{id}/{startPage}-20.html")
//    Observable<Map<String, List<NewsSummary>>> getNewsList(
//            @Header("Cache-Control") String cacheControl,
//            @Path("type") String type, @Path("id") String id,
//            @Path("startPage") int startPage);
//
//    @GET
//    Observable<ResponseBody> getNewsBodyHtmlPhoto(
//            @Header("Cache-Control") String cacheControl,
//            @Url String photoPath);
//    //@Url，它允许我们直接传入一个请求的URL。这样以来我们可以将上一个请求的获得的url直接传入进来，baseUrl将被无视
//    // baseUrl 需要符合标准，为空、""、或不合法将会报错
//
//
//    @GET("data/福利/{size}/{page}")
//    Observable<GirlData> getPhotoList(
//            @Header("Cache-Control") String cacheControl,
//            @Path("size") int size,
//            @Path("page") int page);
//
//    //视频
//    @GET("nc/video/list/{type}/n/{startPage}-10.html")
//    Observable<Map<String, List<VideoData>>> getVideoList(
//            @Header("Cache-Control") String cacheControl,
//            @Path("type") String type,
//            @Path("startPage") int startPage);

    /**
     * Test Api
     */

//    @GET("clo/quality")
//    Observable<QualityData> getQuality(
//            @Query("id") String id
//    );
//
//    @FormUrlEncoded
//    @POST("clo/compare")
//    Observable<RetQuality> getUpLoadAfterChecked(
//            @Field("list") Object[][] qualityDataList
//    );

    /**
     * Release Api
     */

//    //质检项目
//    @GET("api/qc/itemsingle/{id}")
//    Observable<HttpResponse<QualityData>> getQuality(
//            @Path("id") String id
//    );
//


//    //根据卡号获取成衣详情
//    @FormUrlEncoded
//    @POST("api/cards/get_clothes")
//    Observable<HttpResponse<ProductDetails>> getProductDetails(
//            @Field("card_num") String num
//    );
//
//    //根据二维码获取成衣详情
//    @GET("api/third/samples/parts")
//    Observable<HttpResponse<ProductDetails>> getProductDetailsWithScan(
//            @Query("content") String content
//    );


    //视频  http://c.m.163.com/nc/video/list/V9LG4B3A0/n/0-10.html
    @GET("nc/video/list/{type}/n/{startPage}-10.html")
    Observable<TestBean> getVideoList(
            @Header("Cache-Control") String cacheControl,
            @Path("type") String type,
            @Path("startPage") int startPage);

    //mock 登录
    @FormUrlEncoded
    @POST("api/login")
    Observable<LoginTokenData> login(
            @Field("userName") String name,
            @Field("passWord") String pass
    );

}
