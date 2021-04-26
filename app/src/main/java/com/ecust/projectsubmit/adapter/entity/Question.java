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
 * @since 2021/4/20
 */
public class Question {
    /**
     * 问题标号
     */
    private int ID;

    /**
     * 问题次序
     */
    private int No;
    /**
     * 问题类型
     */
    private int Type;

    /**
     * 问题内容
     */
    private String Content;

    /**
     * 问题回答
     */
    private String Answer;

    /**
     * 你的作答
     */
    private String YourAnswer;

    /**
     * 选项个数
     */
    private int Option;


    public int getID() {
        return ID;
    }

    public Question setID(int ID) {
        this.ID = ID;
        return this;
    }

    public int getNo() {
        return No;
    }

    public void setNo(int no) {
        No = no;
    }

    public int getType() {
        return Type;
    }

    public Question setType(int type) {
        Type = type;
        return this;
    }

    public String getContent() {
        return Content;
    }

    public Question setContent(String content) {
        Content = content;
        return this;
    }

    public String getAnswer() {
        return Answer;
    }

    public Question setAnswer(String answer) {
        Answer = answer;
        return this;
    }

    public String getYourAnswer() {
        return YourAnswer;
    }

    public Question setYourAnswer(String yourAnswer) {
        YourAnswer = yourAnswer;
        return this;
    }

    public int getOption() {
        return Option;
    }

    public void setOption(Integer option) {
        this.Option = option;
    }

    public Question(int id,int no, int type, String content, String answer){
        this.ID = id;
        this.No = no;
        this.Type = type;
        this.Content = content;
        this.Answer = answer;
    }

    @Override
    public String toString(){
        return "Question{" +
                "ID='" + ID + '\'' +
                ", No='" + No + '\'' +
                ", Type='" + Type + '\'' +
                ", Content='" + Content + '\'' +
                ", Answer='" + Answer + '\'' +
                ", YourAnswer='" + YourAnswer + '\'' +
                ", Option='" + Option + '\'' +
                '}';
    }

}
