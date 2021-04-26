/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ecust.projectsubmit.core.http.interceptor;

import com.ecust.projectsubmit.core.manager.TokenManager;
import com.xuexiang.xhttp2.interceptor.BaseDynamicInterceptor;
import com.xuexiang.xutil.data.DateUtils;

import java.util.TreeMap;

import okhttp3.Request;

/**
 * 自定义动态添加请求参数的拦截器
 *
 * @author xuexiang
 * @since 2018/8/6 上午11:37
 */
public class CustomDynamicInterceptor extends BaseDynamicInterceptor<CustomDynamicInterceptor> {

    //================下面是动态更新请求参数,按需求添加======================//

    @Override
    protected TreeMap<String, Object> updateDynamicParams(TreeMap<String, Object> dynamicMap) {
        if (isAccessToken()) {
            //是否添加token
            dynamicMap.put("token", TokenManager.getInstance().getToken());
        }
        if (isSign()) {
            //是否添加签名
            dynamicMap.put("sign", TokenManager.getInstance().getSign());
        }
        if (isTimeStamp()) {
            //是否添加请求时间戳
            dynamicMap.put("timeStamp", DateUtils.getNowMills());
        }
        //dynamicMap:是原有的全局参数+局部参数+新增的动态参数
        return dynamicMap;
    }


    //================下面是动态更新请求头,按需求添加======================//


    protected Request.Builder updateHeaders(Request.Builder builder) {
        if (isAccessToken()) {
            //是否添加token
            builder.addHeader("token", TokenManager.getInstance().getToken());
        }
        if (isSign()) {
            //是否添加签名
            builder.addHeader("sign", TokenManager.getInstance().getSign());
        }
        if (isTimeStamp()) {
            //是否添加请求时间戳
            builder.addHeader("timeStamp", String.valueOf(DateUtils.getNowMills()));
        }
        return builder;
    }
}
