/*
 * Copyright (C) 2021 xiaoxiao(1018982338@qq.com)
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
 *
 */

package com.ecust.projectsubmit.adapter.entity;


/**
 * @author xiaoxiao
 * @since 2021/4/19
 */
public class Job {
    /**
     * ID
     */
    private int ID;

    /**
     * 标题
     */
    private String Title;

    /**
     * 类型
     */
    private int Type;

    /**
     * 过期时间
     */
    private String DueDate;

    /**
     * 创建时间
     */
    private String CreateDate;

    /**
     * 题目数量
     */
    private int Count;

    /**
     * 是否完成此作业
     */
    private boolean Status;


    public Job(int id, String title, int type, String duedata, String createdate,int count){
        this.ID = id;
        this.Title = title;
        this.Type = type;
        this.DueDate = duedata;
        this.CreateDate = createdate;
        this.Count = count;
    }
    public int getID() {
        return ID;
    }

    public Job setID(int ID) {
        this.ID = ID;
        return this;
    }

    public String getTitle() {
        return Title;
    }

    public Job setTitle(String title) {
        Title = title;
        return this;
    }

    public int getType() {
        return Type;
    }

    public Job setType(int type) {
        Type = type;
        return this;
    }

    public String getDueDate() {
        return DueDate;
    }

    public Job setDueDate(String dueDate) {
        DueDate = dueDate;
        return this;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public Job setCreateDate(String createDate) {
        CreateDate = createDate;
        return this;
    }

    public int getCount() {
        return Count;
    }

    public Job setCount(int count) {
        Count = count;
        return this;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        this.Status = status;
    }

    @Override
    public String toString(){
        return "Job{" +
                "ID='" + ID + '\'' +
                ", Title='" + Title + '\'' +
                ", Type='" + Type + '\'' +
                ", DueDate='" + DueDate + '\'' +
                ", CreateDate='" + CreateDate + '\'' +
                ", Count='" + Count + '\'' +
                ", Status='" + Status + '\'' +
                '}';
    }



}
