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

package com.ecust.projectsubmit.core.http;


import com.ecust.projectsubmit.adapter.entity.User;

/**
 * @author xuexiang
 * @since 2018/7/16 下午6:53
 */
public class ApiProvider {


    public static TestApi.UserService_AddUser getAddUserReq(User user) {
        TestApi.UserService_AddUser req = new TestApi.UserService_AddUser();
        req.request = user;
        return req;
    }

    public static TestApi.UserService_findUsers getUsersReq(int pageNum, int pageSize) {
        TestApi.UserService_findUsers req = new TestApi.UserService_findUsers();
        req.pageNum = pageNum;
        req.pageSize = pageSize;
        return req;
    }

}
