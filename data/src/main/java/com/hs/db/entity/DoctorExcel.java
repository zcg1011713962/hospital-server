package com.hs.db.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DoctorExcel {
    // 用户ID
    @ExcelIgnore
    private Long userId;
    // 用户类型
    @ExcelIgnore
    private String userType;
    // 真实姓名
    @ExcelProperty("姓名")
    private String realName;

    @ExcelProperty("性别")
    private String gender;
    // 手机号
    @ExcelProperty("手机号")
    private String phoneNum;
    // 邮箱
    @ExcelProperty("邮箱")
    private String email;
    // 科室
    @ExcelProperty("科室")
    private String department;
    // 职位
    @ExcelProperty("职位")
    private String position;
    // 医生简介
    @ExcelProperty("医生简介")
    private String biography;
    // 擅长领域
    @ExcelProperty("擅长领域")
    private String specialization;
    // 可预约时间段
    @ExcelProperty("可预约时间段")
    private String availableSlots;
}
